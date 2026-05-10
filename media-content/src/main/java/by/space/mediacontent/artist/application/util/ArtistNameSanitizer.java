package by.space.mediacontent.artist.application.util;

import java.util.regex.Pattern;


public final class ArtistNameSanitizer {

    private static final Pattern TRAILING_BRACKET_WAREZ = Pattern.compile(
        "(?i)\\s*\\[[a-z0-9][a-z0-9-]{0,48}\\.(?:cc|ru|su|biz|xyz|tk|ml|ga|cf|gq|pw|top|fun|club|site|online|icu|cfd)\\]\\s*$"
    );

    private static final Pattern LEADING_BRACKET_WAREZ = Pattern.compile(
        "(?i)^\\s*\\[[a-z0-9][a-z0-9-]{0,48}\\.(?:cc|ru|su|biz|xyz|tk|ml|ga|cf|gq|pw|top|fun|club|site|online|icu|cfd)\\]\\s*"
    );

    private ArtistNameSanitizer() {
    }

    public static String sanitizeTagArtistName(final String raw) {
        if (raw == null || raw.isBlank()) {
            return "";
        }
        String s = raw.trim();
        while (s.startsWith("\uFEFF")) {
            s = s.substring(1).trim();
        }
        s = s.replaceAll("\\s+", " ").trim();
        String prev;
        do {
            prev = s;
            s = TRAILING_BRACKET_WAREZ.matcher(s).replaceFirst("").trim();
            s = LEADING_BRACKET_WAREZ.matcher(s).replaceFirst("").trim();
            s = s.replaceAll("\\s+", " ").trim();
        } while (!s.equals(prev));
        return s;
    }
}
