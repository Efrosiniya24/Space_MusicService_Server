package by.space.mediacontent.content.application.service;

import by.space.mediacontent.content.application.dto.ImageDto;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    /**
     * sane image in db and minio
     *
     * @param file    file for saving
     * @param ownerId person who save the file
     * @return saving image data
     */
    ImageDto addImage(MultipartFile file, Long ownerId);
}
