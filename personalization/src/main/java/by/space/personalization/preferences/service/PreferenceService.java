package by.space.personalization.preferences.service;

import by.space.personalization.preferences.dto.CreatePreferenceRequest;
import by.space.personalization.preferences.dto.PreferenceDetailsResponse;

import java.util.List;

public interface PreferenceService {
    Long create(CreatePreferenceRequest request);

    List<PreferenceDetailsResponse> findByVenue(Long venueId, Long userId);

    void delete(Long preferenceId, Long userId);
}
