package by.space.mediacontent.content.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface MinioStorageService {
    String upload(MultipartFile file, String objectName);

    InputStream download(String objectName);
}
