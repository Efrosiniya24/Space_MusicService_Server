package by.space.personalization.preferences.service;

import by.space.personalization.preferences.dto.CreatePreferenceRequest;

public interface PreferenceService {
    Long create(CreatePreferenceRequest request);
}
