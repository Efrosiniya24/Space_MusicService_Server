package by.space.mediacontent.artist.application.service.impl;

import by.space.mediacontent.artist.application.service.RoleService;
import by.space.mediacontent.artist.domain.enums.ArtistRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    @Override
    public ArtistRole convertArtistRole(final String role) {
        final String r = role == null ? "" : role.trim();
        return switch (r) {
            case "Исполнитель", "SINGER" -> ArtistRole.SINGER;
            case "Композитор", "COMPOSER" -> ArtistRole.COMPOSER;
            case "Продюсер", "PRODUCER" -> ArtistRole.PRODUCER;
            case "Автор", "AUTHOR" -> ArtistRole.AUTHOR;
            case "DJ" -> ArtistRole.DJ;
            case "Инструменталист", "INSTRUMENTALIST" -> ArtistRole.INSTRUMENTALIST;
            default -> throw new IllegalStateException("Unexpected value: " + role);
        };
    }

    @Override
    public String convertArtistRoleToString(final ArtistRole role) {
        if (Objects.isNull(role)) return "";
        return switch (role) {
            case SINGER -> "Исполнитель";
            case COMPOSER -> "Композитор";
            case PRODUCER -> "Продюсер";
            case AUTHOR -> "Автор";
            case DJ -> "DJ";
            case INSTRUMENTALIST -> "Инструменталист";
        };
    }
}
