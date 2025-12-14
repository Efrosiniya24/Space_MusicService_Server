package by.space.mediacontent.artist.application.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArtistCreateDto {
    private Long id;
    private String name;
    private String cover;
    private String description;
}
