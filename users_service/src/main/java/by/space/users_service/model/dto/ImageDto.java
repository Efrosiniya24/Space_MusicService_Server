package by.space.users_service.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ImageDto {
    private Long id;
    private Long idOwner;
    private String bucket;
    private String fileName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}