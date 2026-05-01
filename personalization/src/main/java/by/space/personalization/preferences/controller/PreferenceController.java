package by.space.personalization.preferences.controller;

import by.space.personalization.preferences.dto.CreatePreferenceRequest;
import by.space.personalization.preferences.dto.CreatePreferenceResponse;
import by.space.personalization.preferences.dto.PreferenceDetailsResponse;
import by.space.personalization.preferences.service.PreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("/venue/{venueId}")
    public ResponseEntity<List<PreferenceDetailsResponse>> findByVenue(
        @PathVariable Long venueId,
        @RequestParam(required = false) Long userId
    ) {
        return ResponseEntity.ok(preferenceService.findByVenue(venueId, userId));
    }

    @DeleteMapping("/{preferenceId}")
    public ResponseEntity<Void> delete(
        @PathVariable Long preferenceId,
        @RequestParam(required = false) Long userId
    ) {
        preferenceService.delete(preferenceId, userId);
        return ResponseEntity.noContent().build();
    }
}
