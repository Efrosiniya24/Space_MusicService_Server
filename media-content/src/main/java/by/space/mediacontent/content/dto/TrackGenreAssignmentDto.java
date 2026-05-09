package by.space.mediacontent.content.dto;

import by.space.mediacontent.content.domain.enums.GenreSource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackGenreAssignmentDto {
    private Long genreId;
    private GenreSource source;
    private BigDecimal confidence;
}
