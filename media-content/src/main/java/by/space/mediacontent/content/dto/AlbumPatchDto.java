package by.space.mediacontent.content.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumPatchDto {
    private Integer year;
    private String title;
    private Long idCover;
}
