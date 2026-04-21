package by.space.mediacontent.content.service.impl;

import by.space.mediacontent.content.domain.entity.DocumentsVenueEntity;
import by.space.mediacontent.content.dto.VenueDocumentDto;
import by.space.mediacontent.content.dto.VenueDocumentStream;
import by.space.mediacontent.content.mapper.VenueDocumentMapper;
import by.space.mediacontent.content.repository.VenueDocumentsRepository;
import by.space.mediacontent.content.service.FileService;
import by.space.mediacontent.content.service.MinioStorageService;
import by.space.mediacontent.content.util.MediaTypeUtil;
import by.space.mediacontent.content.util.ObjectKeyGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final MinioStorageService minioStorageService;
    private final VenueDocumentsRepository venueDocumentsRepository;
    private final VenueDocumentMapper venueDocumentMapper;

    @Value("${minio.bucket}")
    private String bucket;

    @Override
    @Transactional
    public void uploadDocuments(
        final Long venueId,
        final List<MultipartFile> files,
        final List<Long> venueAddressIds
    ) {
        for (int i = 0; i < files.size(); i++) {
            final MultipartFile file = files.get(i);
            final String originalFilename = file.getOriginalFilename();

            final String objectKey = ObjectKeyGenerator.generate(
                "venue documents",
                venueId,
                originalFilename
            );

            minioStorageService.upload(file, objectKey);

            final Long addressId =
                (venueAddressIds != null && i < venueAddressIds.size())
                    ? venueAddressIds.get(i)
                    : null;

            final DocumentsVenueEntity document = DocumentsVenueEntity.builder()
                .venueId(venueId)
                .venueAddressId(addressId)
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

    @Override
    @Transactional(readOnly = true)
    public List<VenueDocumentDto> listVenueDocuments(final Long venueId, final Long venueAddressId) {
        final List<DocumentsVenueEntity> documents =
            venueAddressId == null
                ? venueDocumentsRepository.findAllByVenueIdAndRemovedIsFalse(venueId)
                : venueDocumentsRepository.findAllByVenueIdAndVenueAddressIdAndRemovedIsFalse(venueId, venueAddressId);

        return documents.stream()
            .map(venueDocumentMapper::toDto)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VenueDocumentStream downloadDocument(final Long documentId) {
        final DocumentsVenueEntity doc = venueDocumentsRepository.findById(documentId)
            .orElseThrow(() -> new NoSuchElementException("document not found"));
        if (doc.isRemoved()) {
            throw new NoSuchElementException("document removed");
        }
        return new VenueDocumentStream(
            new InputStreamResource(minioStorageService.download(doc.getObjectKey())),
            MediaTypeUtil.guessFromFileName(doc.getTitle()),
            doc.getTitle()
        );
    }
}
