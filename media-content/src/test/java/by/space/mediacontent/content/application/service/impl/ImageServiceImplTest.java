package by.space.mediacontent.content.application.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import by.space.mediacontent.content.domain.entity.ImageEntity;
import by.space.mediacontent.content.dto.ImageDto;
import by.space.mediacontent.content.infrastructure.mapper.ImageMapper;
import by.space.mediacontent.content.infrastructure.repository.ImageRepository;
import by.space.mediacontent.content.service.MinioStorageService;
import by.space.mediacontent.content.service.impl.ImageServiceImpl;
import by.space.mediacontent.content.util.ObjectKeyGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {

    @Mock
    ImageMapper imageMapper;
    @Mock
    MinioStorageService minioStorageService;
    @Mock
    ImageRepository imageRepository;
    @Mock
    MultipartFile file;

    @Test
    void addImage_generates_key_uploads_saves_entity_and_returns_dto() {
        ImageServiceImpl service = new ImageServiceImpl(imageMapper, minioStorageService, imageRepository);
        ReflectionTestUtils.setField(service, "bucket", "bucket-test");

        Long ownerId = 10L;
        when(file.getOriginalFilename()).thenReturn("img.png");

        String generatedKey = "images/10/img.png";

        try (MockedStatic<ObjectKeyGenerator> mocked = mockStatic(ObjectKeyGenerator.class)) {
            mocked.when(() -> ObjectKeyGenerator.generate("images", ownerId, "img.png"))
                .thenReturn(generatedKey);

            ImageEntity savedEntity = ImageEntity.builder()
                .idOwner(ownerId)
                .bucket("bucket-test")
                .objectKey(generatedKey)
                .fileName("img.png")
                .createdAt(LocalDateTime.now())
                .build();

            when(imageRepository.save(any(ImageEntity.class))).thenReturn(savedEntity);

            ImageDto expectedDto = mock(ImageDto.class);
            when(imageMapper.toImageDto(savedEntity)).thenReturn(expectedDto);

            ImageDto result = service.addImage(file, ownerId);

            assertSame(expectedDto, result);

            verify(minioStorageService).upload(file, generatedKey);

            ArgumentCaptor<ImageEntity> captor = ArgumentCaptor.forClass(ImageEntity.class);
            verify(imageRepository).save(captor.capture());

            ImageEntity toSave = captor.getValue();
            assertEquals(ownerId, toSave.getIdOwner());
            assertEquals("bucket-test", toSave.getBucket());
            assertEquals(generatedKey, toSave.getObjectKey());
            assertEquals("img.png", toSave.getFileName());
            assertNotNull(toSave.getCreatedAt());

            verify(imageMapper).toImageDto(savedEntity);
            verifyNoMoreInteractions(minioStorageService, imageRepository, imageMapper);
        }
    }

    @Test
    void addImage_when_upload_fails_does_not_save_entity() {
        ImageServiceImpl service = new ImageServiceImpl(imageMapper, minioStorageService, imageRepository);
        ReflectionTestUtils.setField(service, "bucket", "bucket-test");

        Long ownerId = 10L;
        when(file.getOriginalFilename()).thenReturn("img.png");

        String generatedKey = "images/10/img.png";

        try (MockedStatic<ObjectKeyGenerator> mocked = mockStatic(ObjectKeyGenerator.class)) {
            mocked.when(() -> ObjectKeyGenerator.generate("images", ownerId, "img.png"))
                .thenReturn(generatedKey);

            doThrow(new RuntimeException("minio down"))
                .when(minioStorageService).upload(file, generatedKey);

            RuntimeException ex = assertThrows(RuntimeException.class, () -> service.addImage(file, ownerId));
            assertEquals("minio down", ex.getMessage());

            verify(minioStorageService).upload(file, generatedKey);
            verifyNoInteractions(imageRepository);
            verifyNoInteractions(imageMapper);
        }
    }
}