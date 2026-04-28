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
public class ScheduleBlockRequest {
    private List<Integer> weekDays;
    private Boolean timeIrrelevant;
    private List<String> timePresets;
    private List<CustomIntervalRequest> customIntervals;
    private List<String> specificDates;
}
