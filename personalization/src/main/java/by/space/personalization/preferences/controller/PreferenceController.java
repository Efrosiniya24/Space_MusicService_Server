package by.space.personalization.preferences.controller;

import by.space.personalization.preferences.dto.CreatePreferenceRequest;
import by.space.personalization.preferences.dto.CreatePreferenceResponse;
import by.space.personalization.preferences.service.PreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/preferences")
public class PreferenceController {
    private final PreferenceService preferenceService;

    @PostMapping
    public ResponseEntity<CreatePreferenceResponse> create(@RequestBody CreatePreferenceRequest request) {
        final Long preferenceId = preferenceService.create(request);
        return ResponseEntity.ok(new CreatePreferenceResponse(preferenceId));
    }
}
