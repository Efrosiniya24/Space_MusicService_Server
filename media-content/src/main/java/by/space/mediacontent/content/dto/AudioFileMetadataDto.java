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
public class AudioFileMetadataDto {
    private String artist;
    private String title;
    private String album;
    @Builder.Default
    private List<String> genres = new ArrayList<>();
    private Long durationSeconds;
}
