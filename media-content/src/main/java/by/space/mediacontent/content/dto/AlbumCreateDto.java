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
public class AlbumCreateDto {
    private String name;
    private Long idCover;
    private List<Long> trackIds;
}
