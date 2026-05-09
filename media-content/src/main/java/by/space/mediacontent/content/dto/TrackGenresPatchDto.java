package by.space.mediacontent.content.dto;

import by.space.mediacontent.content.domain.enums.GenreSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackGenresPatchDto {
    private List<Long> genreIds;
    private GenreSource source;
    private BigDecimal confidence;
}
