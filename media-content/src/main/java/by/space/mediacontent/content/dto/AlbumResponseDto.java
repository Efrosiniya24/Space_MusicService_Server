package by.space.mediacontent.content.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlbumResponseDto {
    private Long id;
    private String name;
    private Long idCover;
    private Long artistId;
    private boolean deleted;
    private List<AlbumTrackDto> tracks;
}
