package by.space.users_service.service;

import by.space.users_service.enums.StatusVenue;
import by.space.users_service.model.dto.VenueConfirmDto;

public interface VenueStatusService {

    /**
     * Update moderation status for one venue address row.
     *
     * @param id     venue_address id (not venue id)
     * @param status new status name (StatusVenue enum value)
     * @return venue with refreshed addresses, updated address row, and new status
     */
    VenueConfirmDto confirmVenue(Long id, String status);

    /**
     * Count active venue addresses in the given moderation status.
     *
     * @param statusVenue status filter
     * @return number of non-deleted addresses with that status
     */
    Long getCountOfVenueByStatus(StatusVenue statusVenue);
}
