package by.space.mediacontent.content.application.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.InputStream;

@ExtendWith(MockitoExtension.class)
class MinioStorageServiceImplTest {

    @Mock
    private MinioClient minioClient;

    @InjectMocks
    private MinioStorageServiceImpl service;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "bucket", "space-media");
    }

    @Test
    void upload_shouldCallPutObject_andReturnObjectName() throws Exception {
        // given
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "a.png",
            "image/png",
            "hello".getBytes()
        );
        when(minioClient.putObject(any(PutObjectArgs.class)))
            .thenReturn(mock(ObjectWriteResponse.class));

        // when
        String result = service.upload(file, "images/1/a.png");

        // then
        assertEquals("images/1/a.png", result);
        verify(minioClient, times(1)).putObject(any(PutObjectArgs.class));
    }

    @Test
    void upload_shouldThrowRuntimeException_whenMinioFails() throws Exception {
        // given
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "a.png",
            "image/png",
            "x".getBytes()
        );
        when(minioClient.putObject(any(PutObjectArgs.class)))
            .thenThrow(new RuntimeException("boom"));

        // when then
        assertThrows(RuntimeException.class, () -> service.upload(file, "k"));
    }

    @Test
    void download_shouldReturnInputStream_fromMinio() throws Exception {
        // given
        GetObjectResponse expected = mock(GetObjectResponse.class);
        when(minioClient.getObject(any(GetObjectArgs.class))).thenReturn(expected);

        // when
        InputStream result = service.download("images/1/a.png");

        // then
        assertSame(expected, result);

        verify(minioClient, times(1)).getObject(any(GetObjectArgs.class));
    }

    @Test
    void download_shouldThrowRuntimeException_whenMinioFails() throws Exception {
        // given
        when(minioClient.getObject(any(GetObjectArgs.class)))
            .thenThrow(new RuntimeException("nope"));

        // when then
        assertThrows(RuntimeException.class, () -> service.download("k"));
    }
}
