package by.space.mediacontent.content.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class Id3v1GenreNames {

    private static volatile String[] cached;

    private Id3v1GenreNames() {
    }

    public static String nameForIndex(final int index) {
        if (index < 0) {
            return null;
        }
        final String[] arr = load();
        if (index >= arr.length) {
            return null;
        }
        final String n = arr[index];
        return n != null && !n.isBlank() ? n.trim() : null;
    }

    private static String[] load() {
        String[] local = cached;
        if (local != null) {
            return local;
        }
        synchronized (Id3v1GenreNames.class) {
            local = cached;
            if (local != null) {
                return local;
            }
            try (InputStream in = Id3v1GenreNames.class.getResourceAsStream("/id3v1-genre-names.txt")) {
                if (in == null) {
                    cached = new String[0];
                    return cached;
                }
                final String text = new String(in.readAllBytes(), StandardCharsets.UTF_8);
                cached = text.split("\\R", -1);
                return cached;
            } catch (final IOException e) {
                cached = new String[0];
                return cached;
            }
        }
    }

    public static void expandNumericTokens(final java.util.Set<String> genrePieces) {
        if (genrePieces == null || genrePieces.isEmpty()) {
            return;
        }
        for (final String token : new java.util.ArrayList<>(genrePieces)) {
            if (token == null || token.isBlank()) {
                continue;
            }
            final String trimmed = token.trim();
            final java.util.regex.Matcher m = java.util.regex.Pattern
                .compile("^\\(?\\s*(\\d{1,3})\\s*\\)?$")
                .matcher(trimmed);
            if (!m.matches()) {
                continue;
            }
            try {
                final int idx = Integer.parseInt(m.group(1));
                final String name = nameForIndex(idx);
                if (name != null) {
                    genrePieces.add(name);
                }
            } catch (final NumberFormatException ignored) {
                /* skip */
            }
        }
    }
}
