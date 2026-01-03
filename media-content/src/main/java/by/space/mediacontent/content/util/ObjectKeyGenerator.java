package by.space.mediacontent.content.util;

import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class ObjectKeyGenerator {

    /**
     * Generates objectKey for MinIO storage.
     */
    public static String generate(
        final String baseDir,
        final Long ownerId,
        final String originalFilename
    ) {
        final String original = Optional.ofNullable(originalFilename).orElse("file");

        final String baseName = StringUtils.stripFilenameExtension(original);
        final String extension = StringUtils.getFilenameExtension(original);

        String safeBase = baseName
            .toLowerCase()
            .replaceAll("[^a-z0-9]+", "-")
            .replaceAll("(^-|-$)", "");

        if (safeBase.isBlank()) {
            safeBase = "file";
        }

        final String uuid = UUID.randomUUID().toString();

        return baseDir + "/" + ownerId + "/" +
            safeBase + "-" + uuid +
            (extension != null ? "." + extension : "");
    }
}
