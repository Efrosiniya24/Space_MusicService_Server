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
public class TrackResponseDto {
    private Long id;
    private String name;
    private Long idCover;
    private String originalFileName;
    private Long durationSeconds;
    private boolean single;
    private boolean deleted;
    @Builder.Default
    private List<Long> genreIds = new ArrayList<>();
    @Builder.Default
    private List<TrackGenreAssignmentDto> genres = new ArrayList<>();
    @Builder.Default
    private List<String> artistNames = new ArrayList<>();
}
