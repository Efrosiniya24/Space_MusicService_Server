package by.space.users_service.service;

import by.space.users_service.enums.StatusVenue;
import by.space.users_service.model.dto.VenueConfirmDto;

public interface VenueStatusService {

    /**
     * method for confirmation new venue
     *
     * @param id venue id
     * @return venue data and new status
     */
    VenueConfirmDto confirmVenue(Long id, String status);

    /**
     * Get count of venues by venue status
     *
     * @param statusVenue venue status (PENDING, CONFIRMED, PROCESSING, APPROVED)
     * @return the count of venue with status
     */
    Long getCountOfVenueByStatus(StatusVenue statusVenue);
}
