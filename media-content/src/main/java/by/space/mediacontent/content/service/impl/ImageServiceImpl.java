package by.space.mediacontent.content.service.impl;

import by.space.mediacontent.content.domain.entity.ImageEntity;
import by.space.mediacontent.content.dto.ImageDto;
import by.space.mediacontent.content.infrastructure.mapper.ImageMapper;
import by.space.mediacontent.content.infrastructure.repository.ImageRepository;
import by.space.mediacontent.content.service.ImageService;
import by.space.mediacontent.content.service.MinioStorageService;
import by.space.mediacontent.content.util.ObjectKeyGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageMapper imageMapper;
    private final MinioStorageService minioStorageService;
    private final ImageRepository imageRepository;

    @Value("${minio.bucket}")
    private String bucket;

    @Override
    @Transactional
    public ImageDto addImage(final MultipartFile file, final Long ownerId) {
        final String originalFilename = file.getOriginalFilename();
        final String objectKey = ObjectKeyGenerator.generate(
            "images",
            ownerId,
            originalFilename
        );
        minioStorageService.upload(file, objectKey);

        final ImageEntity savedImage = imageRepository.save(
            ImageEntity.builder()
                .idOwner(ownerId)
                .bucket(bucket)
                .objectKey(objectKey)
                .fileName(originalFilename)
                .createdAt(LocalDateTime.now())
                .build());

        return imageMapper.toImageDto(savedImage);
    }
}
