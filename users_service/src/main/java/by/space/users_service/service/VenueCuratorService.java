package by.space.users_service.service;

import java.util.List;

public interface VenueCuratorService {
    /**
     * getting all active admin venues
     *
     * @param userId admin id
     * @return id of all active admin venues
     */
    List<Long> getAllActiveUserVenue(Long userId);

    /**
     * create venue curator after venue registration
     *
     * @param curatorId   id of venue curator
     * @param venueId     venue id
     * @param addressesId addresses of curator
     * @param isUserAdmin is curator admin
     */
    void createVenueCurator(Long curatorId, Long venueId, List<Long> addressesId, Boolean isUserAdmin);
}
