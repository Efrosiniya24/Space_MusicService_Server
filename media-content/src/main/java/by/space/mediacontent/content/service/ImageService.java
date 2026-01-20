package by.space.mediacontent.content.service;

import by.space.mediacontent.content.dto.ImageDto;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    /**
     * save image in db and minio
     *
     * @param file    file for saving
     * @param ownerId person who save the file
     * @return saving image data
     */
    ImageDto addImage(MultipartFile file, Long ownerId);

    /**
     * add venue cover
     *
     * @param file    file for saving
     * @param ownerId person who save the file
     * @param venueId venue of saving cover
     */
    void addVenueCover(MultipartFile file, Long ownerId, Long venueId);
}
