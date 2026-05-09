package by.space.mediacontent.content.util;

import org.springframework.http.MediaType;

import java.util.Objects;

public final class MediaTypeUtil {

    private MediaTypeUtil() {
    }

    public static MediaType guessFromFileName(final String filename) {
        if (Objects.isNull(filename)) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
        final String lower = filename.toLowerCase();
        if (lower.endsWith(".pdf")) {
            return MediaType.APPLICATION_PDF;
        }
        if (lower.endsWith(".docx")) {
            return MediaType.parseMediaType(
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            );
        }
        if (lower.endsWith(".doc")) {
            return MediaType.parseMediaType("application/msword");
        }
        if (lower.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        }
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG;
        }
        if (lower.endsWith(".gif")) {
            return MediaType.IMAGE_GIF;
        }
        if (lower.endsWith(".webp")) {
            return MediaType.parseMediaType("image/webp");
        }
        if (lower.endsWith(".mp3")) {
            return MediaType.parseMediaType("audio/mpeg");
        }
        if (lower.endsWith(".flac")) {
            return MediaType.parseMediaType("audio/flac");
        }
        if (lower.endsWith(".wav")) {
            return MediaType.parseMediaType("audio/wav");
        }
        if (lower.endsWith(".ogg")) {
            return MediaType.parseMediaType("audio/ogg");
        }
        if (lower.endsWith(".m4a") || lower.endsWith(".mp4")) {
            return MediaType.parseMediaType("audio/mp4");
        }
        if (lower.endsWith(".aac")) {
            return MediaType.parseMediaType("audio/aac");
        }
        return MediaType.APPLICATION_OCTET_STREAM;
    }
}
