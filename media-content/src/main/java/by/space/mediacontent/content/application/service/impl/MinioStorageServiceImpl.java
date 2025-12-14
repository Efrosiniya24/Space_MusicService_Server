package by.space.mediacontent.content.application.service.impl;

import by.space.mediacontent.content.application.service.MinioStorageService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class MinioStorageServiceImpl implements MinioStorageService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    @Override
    public String upload(final MultipartFile file, final String objectName) {
        try {
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build()
            );
            return objectName;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InputStream download(String objectName) {
        try {
            return minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .build()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
