package by.space.personalization.preferences.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PreferenceDetailsResponse {
    private Long preferenceId;
    private Long userId;
    private Long venueId;
    private Boolean timeIrrelevant;
    private LocalDateTime createdAt;
    private List<Long> addressIds;
    private List<Long> genreIds;
    private List<Long> trackIds;
    private List<Long> playlistIds;
    private List<String> volumeLevels;
    private List<PreferenceScheduleBlockResponse> scheduleBlocks;
}
