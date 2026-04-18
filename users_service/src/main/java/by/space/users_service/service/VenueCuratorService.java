package by.space.users_service.service;

import by.space.users_service.model.dto.VenueCuratorDto;

import java.util.List;

public interface VenueCuratorService {
    /**
     * getting all active admin venues
     *
     * @param userId admin id
     * @return all active curator venues
     */
    List<VenueCuratorDto> getAllActiveUserVenue(Long userId);

    /**
     * create venue curator after venue registration
     *
     * @param curatorId   id of venue curator
     * @param venueId     venue id
     * @param addressesId addresses of curator
     * @param isUserAdmin is curator admin
     */
    void createVenueCurator(Long curatorId, Long venueId, List<Long> addressesId, Boolean isUserAdmin);

    /**
     * Gets all venue curators by venue id
     *
     * @param venueId venue id for getting all curators
     * @return list of all venue curators (admins and musical curator) for active addresses
     */
    List<VenueCuratorDto> getAllVenueCurators(Long venueId);
}
