package by.space.mediacontent.content.service.impl;

import by.space.mediacontent.content.domain.entity.DocumentsVenueEntity;
import by.space.mediacontent.content.repository.VenueDocumentsRepository;
import by.space.mediacontent.content.service.FileService;
import by.space.mediacontent.content.service.MinioStorageService;
import by.space.mediacontent.content.util.ObjectKeyGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final MinioStorageService minioStorageService;
    private final VenueDocumentsRepository venueDocumentsRepository;

    @Value("${minio.bucket}")
    private String bucket;

    @Override
    @Transactional
    public void uploadDocuments(final Long venueId, final List<MultipartFile> files) {
        for (final MultipartFile file : files) {
            final String originalFilename = file.getOriginalFilename();
            final String objectKey = ObjectKeyGenerator.generate(
                "venue documents",
                venueId,
                originalFilename
            );
            minioStorageService.upload(file, objectKey);

            final DocumentsVenueEntity document = DocumentsVenueEntity.builder()
                .venueId(venueId)
                .title(originalFilename)
                .bucket(bucket)
                .objectKey(objectKey)
                .url(objectKey)
                .createdAt(LocalDateTime.now())
                .removed(false)
                .build();

            venueDocumentsRepository.save(document);
        }
    }
}
