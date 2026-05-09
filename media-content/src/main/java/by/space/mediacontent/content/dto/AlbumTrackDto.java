package by.space.mediacontent.content.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlbumTrackDto {
    private Long id;
    private String name;
    private Long idCover;
    private Long durationSeconds;
    private boolean deleted;
    @Builder.Default
    private List<Long> genreIds = new ArrayList<>();
}
