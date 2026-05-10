package by.space.mediacontent.content.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TrackCoArtistsDto {
    private List<Long> coArtistIds = new ArrayList<>();
}
