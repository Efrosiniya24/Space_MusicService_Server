package by.space.personalization.preferences.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePreferenceRequest {
    private Long userId;
    private Long venueId;
    private List<Long> addressIds;
    private List<String> volumeLevels;
    private List<Long> genreIds;
    private List<ScheduleBlockRequest> scheduleBlocks;
}
