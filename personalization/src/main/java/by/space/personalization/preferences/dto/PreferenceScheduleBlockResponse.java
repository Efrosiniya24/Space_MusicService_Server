package by.space.personalization.preferences.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PreferenceScheduleBlockResponse {
    private Long blockId;
    private Integer sortOrder;
    private List<Integer> weekDays;
    private List<String> timePresets;
    private List<CustomIntervalRequest> customIntervals;
    private List<String> specificDates;
}
