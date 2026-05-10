package by.space.mediacontent.content.service;

import by.space.mediacontent.artist.application.util.ArtistNameSanitizer;
import by.space.mediacontent.content.dto.AudioFileMetadataDto;
import by.space.mediacontent.content.util.Id3v1GenreNames;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Service
@Slf4j
public class AudioMetadataProbeService {

    /** Одно слово вида «site.tld» в поле genre часто — реклама заливщика, не музыкальный жанр. */
    private static final Pattern DOMAIN_LIKE_GENRE_SPAM = Pattern.compile(
        "(?i)[a-z0-9][a-z0-9.-]{0,120}\\.[a-z]{2,63}"
    );

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${media.ffprobe.executable:ffprobe}")
    private String ffprobeExecutable;

    @Value("${media.ffprobe.timeout-seconds:45}")
    private int timeoutSeconds;

    public AudioFileMetadataDto probeUploadedFile(final MultipartFile file)
        throws IOException, InterruptedException {
        return probeUploadedFile(file, null);
    }

    /**
     * @param importFolderBasename необязательно: базовое имя каталога импорта (напр. {@code баста}),
     *                             для согласования с префиксом исполнителя в имени файла
     */
    public AudioFileMetadataDto probeUploadedFile(
        final MultipartFile file,
        final String importFolderBasename
    )
        throws IOException, InterruptedException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("file required");
        }
        Path tmp = null;
        try {
            final String orig =
                file.getOriginalFilename() != null ? file.getOriginalFilename() : "audio";
            tmp = Files.createTempFile("ffprobe-", tempSuffix(orig));
            try (InputStream in = file.getInputStream()) {
                Files.copy(in, tmp, StandardCopyOption.REPLACE_EXISTING);
            }
            final AudioFileMetadataDto raw = probe(tmp);
            return FilenameProbeAugmentation.augment(raw, orig, importFolderBasename);
        } finally {
            if (tmp != null) {
                try {
                    Files.deleteIfExists(tmp);
                } catch (final IOException ignored) {
                    log.debug("probe temp delete failed: {}", tmp);
                }
            }
        }
    }

    private static String tempSuffix(final String originalFilename) {
        final String orig = originalFilename != null ? originalFilename : "";
        final int dot = orig.lastIndexOf('.');
        if (dot >= 0 && dot < orig.length() - 1) {
            return orig.substring(dot);
        }
        return ".bin";
    }

    public AudioFileMetadataDto probe(final Path audioPath) throws IOException, InterruptedException {
        final List<String> command = List.of(
            ffprobeExecutable,
            "-v", "quiet",
            "-print_format", "json",
            "-show_format",
            "-show_streams",
            audioPath.toAbsolutePath().normalize().toString()
        );
        final ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        final Process process = pb.start();
        final byte[] rawOut = process.getInputStream().readAllBytes();
        final boolean finished = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);
        if (!finished) {
            process.destroyForcibly();
            throw new IOException("ffprobe timeout after " + timeoutSeconds + "s");
        }
        if (process.exitValue() != 0) {
            final String err = new String(rawOut, StandardCharsets.UTF_8);
            log.warn("ffprobe exit {}: {}", process.exitValue(), truncate(err, 500));
            throw new IOException("ffprobe failed with exit " + process.exitValue());
        }
        final String json = new String(rawOut, StandardCharsets.UTF_8).trim();
        if (json.isEmpty()) {
            throw new IOException("ffprobe produced empty output");
        }
        return parseFfprobeJson(json);
    }

    private static String truncate(final String s, final int max) {
        if (s == null || s.length() <= max) {
            return s;
        }
        return s.substring(0, max) + "…";
    }

    AudioFileMetadataDto parseFfprobeJson(final String json) throws IOException {
        final JsonNode root = objectMapper.readTree(json);
        final Map<String, String> tags = new LinkedHashMap<>();

        final JsonNode format = root.path("format");
        mergeTags(format.path("tags"), tags);

        if (format.hasNonNull("duration")) {
            mergeDuration(tags, format.get("duration").asText());
        }

        final JsonNode streams = root.path("streams");
        if (streams.isArray()) {
            for (final JsonNode stream : streams) {
                if (!"audio".equals(stream.path("codec_type").asText())) {
                    continue;
                }
                mergeTags(stream.path("tags"), tags);
                break;
            }
        }

        final String artist = firstNonBlank(
            tags.get("artist"),
            tags.get("album_artist"),
            tags.get("albumartist"),
            tags.get("performer"),
            tags.get("band"),
            tags.get("tpe1")
        );
        final String title = firstNonBlank(
            tags.get("title"),
            tags.get("tit2"),
            tags.get("song"),
            tags.get("name")
        );
        final String album = firstNonBlank(
            tags.get("album"),
            tags.get("talb"),
            tags.get("albumtitle")
        );

        final Set<String> genrePieces = new LinkedHashSet<>();
        collectGenresFromTags(tags, genrePieces);
        Id3v1GenreNames.expandNumericTokens(genrePieces);

        Long durationSeconds = null;
        final String durStr = tags.get("__duration_seconds");
        if (durStr != null) {
            try {
                final double d = Double.parseDouble(durStr);
                if (Double.isFinite(d) && d > 0 && d < 86400L * 7) {
                    durationSeconds = Math.round(d);
                }
            } catch (final NumberFormatException ignored) {
                /* skip */
            }
        }

        return AudioFileMetadataDto.builder()
            .artist(blankToNull(ArtistNameSanitizer.sanitizeTagArtistName(artist != null ? artist : "")))
            .title(blankToNull(title))
            .album(blankToNull(album))
            .genres(new ArrayList<>(genrePieces))
            .durationSeconds(durationSeconds)
            .build();
    }

    private static void mergeDuration(final Map<String, String> tags, final String durationText) {
        if (durationText == null || durationText.isBlank()) {
            return;
        }
        tags.putIfAbsent("__duration_seconds", durationText.trim());
    }

    /**
     * ID3 иногда отдаёт UTF-8 байты как ISO-8859-1; в JSON это строка «искажённой» латиницы без кириллицы.
     * Перечитываем как UTF-8 только если результат содержит кириллицу и без явной порчи.
     */
    private static String repairUtf8MisreadAsLatin1(final String input) {
        if (input == null || input.isBlank()) {
            return input;
        }
        if (containsCyrillicLetter(input)) {
            return input;
        }
        try {
            final byte[] raw = input.getBytes(StandardCharsets.ISO_8859_1);
            final String repaired = new String(raw, StandardCharsets.UTF_8);
            if (!containsCyrillicLetter(repaired)) {
                return input;
            }
            final long replacementChars = repaired.codePoints().filter(cp -> cp == 0xFFFD).count();
            if (replacementChars > 1L) {
                return input;
            }
            return repaired;
        } catch (final Exception ignored) {
            return input;
        }
    }

    private static boolean containsCyrillicLetter(final String s) {
        return s.codePoints().anyMatch(cp ->
            (cp >= 0x0400 && cp <= 0x04FF)
                || (cp >= 0x0500 && cp <= 0x052F)
        );
    }

    private static void mergeTags(final JsonNode tagsNode, final Map<String, String> into) {
        if (tagsNode == null || !tagsNode.isObject()) {
            return;
        }
        tagsNode.fields().forEachRemaining(entry -> {
            final JsonNode v = entry.getValue();
            if (v == null || v.isNull()) {
                return;
            }
            if (v.isTextual()) {
                final String text = v.asText();
                if (text != null && !text.isBlank()) {
                    into.put(
                        entry.getKey().toLowerCase(Locale.ROOT),
                        repairUtf8MisreadAsLatin1(text.trim())
                    );
                }
                return;
            }
            if (v.isIntegralNumber()) {
                into.putIfAbsent(
                    entry.getKey().toLowerCase(Locale.ROOT),
                    String.valueOf(v.longValue())
                );
            }
        });
    }

    /**
     * Жанр в ffprobe может лежать под разными ключами (MP3, MP4/©gen, WMA, iTunes freeform, ICY).
     */
    private static void collectGenresFromTags(final Map<String, String> tags, final Set<String> genrePieces) {
        addSplitGenres(genrePieces, tags.get("genre"));
        addSplitGenres(genrePieces, tags.get("tcon"));
        addSplitGenres(genrePieces, tags.get("wm/genre"));
        addSplitGenres(genrePieces, tags.get("\u00a9gen"));
        addSplitGenres(genrePieces, tags.get("----:com.apple.itunes:genre"));
        addSplitGenres(genrePieces, tags.get("icy-genre"));
        addSplitGenres(genrePieces, tags.get("musicbrainz_genre"));
        for (final Map.Entry<String, String> e : tags.entrySet()) {
            final String k = e.getKey();
            final String v = e.getValue();
            if (v == null || v.isBlank()) {
                continue;
            }
            if ("genre".equals(k) || "tcon".equals(k) || "wm/genre".equals(k)
                || "\u00a9gen".equals(k) || "----:com.apple.itunes:genre".equals(k) || "icy-genre".equals(k)
                || "musicbrainz_genre".equals(k)) {
                continue;
            }
            if (k.endsWith(":genre") || k.endsWith("/genre")) {
                addSplitGenres(genrePieces, v);
            }
        }
    }

    private static void addSplitGenres(final Set<String> out, final String raw) {
        if (raw == null || raw.isBlank()) {
            return;
        }
        final String s = raw.trim();
        if (s.startsWith("(") && s.contains(")")) {
            final int closing = s.indexOf(')');
            if (closing > 1) {
                final String rest = s.substring(closing + 1).trim();
                if (!rest.isEmpty()) {
                    splitGenreTokens(out, rest);
                    return;
                }
            }
        }
        splitGenreTokens(out, s);
    }

    private static void splitGenreTokens(final Set<String> out, final String s) {
        for (final String piece : s.split("[/;,|]+")) {
            final String t = piece.trim();
            if (!t.isEmpty() && !isLikelyDomainLikeGenreSpam(t)) {
                out.add(t);
            }
        }
    }

    private static boolean isLikelyDomainLikeGenreSpam(final String token) {
        final String t = token.trim();
        if (t.indexOf(' ') >= 0) {
            return false;
        }
        if (t.chars().allMatch(Character::isDigit)) {
            return false;
        }
        return DOMAIN_LIKE_GENRE_SPAM.matcher(t).matches();
    }

    private static String firstNonBlank(final String... values) {
        if (values == null) {
            return null;
        }
        for (final String v : values) {
            if (v != null && !v.isBlank()) {
                return v.trim();
            }
        }
        return null;
    }

    private static String blankToNull(final String s) {
        return s == null || s.isBlank() ? null : s.trim();
    }
}
