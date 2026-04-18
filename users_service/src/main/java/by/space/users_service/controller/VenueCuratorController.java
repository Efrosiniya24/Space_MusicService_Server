package by.space.users_service.controller;

import by.space.users_service.model.dto.VenueCuratorDto;
import by.space.users_service.service.VenueCuratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author e.zinkovskaya
 * @since 11.04.2026
 */
@RestController
@RequiredArgsConstructor
public class VenueCuratorController {
    private final VenueCuratorService venueCuratorService;

    @GetMapping("/venueCurators/{venueId}")
    public ResponseEntity<List<VenueCuratorDto>> getVenueCurators(@PathVariable Long venueId) {
        return ResponseEntity.ok(venueCuratorService.getAllVenueCurators(venueId));
    }
}
