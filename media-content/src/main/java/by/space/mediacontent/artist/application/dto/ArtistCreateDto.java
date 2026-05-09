package by.space.mediacontent.artist.application.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ArtistCreateDto {
    private Long id;
    private String name;
    private String cover;
    private Long idCover;
    private String description;
    private List<String> roles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
