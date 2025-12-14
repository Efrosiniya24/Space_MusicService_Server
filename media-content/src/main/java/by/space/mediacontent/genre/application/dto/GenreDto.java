package by.space.mediacontent.genre.application.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenreDto {
    private Long id;
    private String name;
    private Boolean deleted;
}
