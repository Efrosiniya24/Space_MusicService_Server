package by.space.mediacontent.artist.application.service;

import by.space.mediacontent.artist.domain.enums.ArtistRole;

public interface RoleService {
    /**
     * Converts role from russian string form to ArtistRole enum form
     *
     * @param role sting of role
     * @return converted role name
     */
    ArtistRole convertArtistRole(String role);

    /**
     * Converts ArtistRole enum value to russian display string.
     *
     * @param role role enum
     * @return localized role
     */
    String convertArtistRoleToString(ArtistRole role);
}
