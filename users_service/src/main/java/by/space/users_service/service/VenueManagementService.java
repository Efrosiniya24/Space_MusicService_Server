package by.space.users_service.service;

import by.space.users_service.model.dto.VenueConfirmDto;

public interface VenueManagementService {

    /**
     * method for confirmation new venue
     *
     * @param id venue id
     * @return venue data and new status
     */
    VenueConfirmDto confirmVenue(Long id,  String status);
}
