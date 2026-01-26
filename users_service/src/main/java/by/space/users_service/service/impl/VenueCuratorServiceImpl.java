package by.space.users_service.service.impl;

import by.space.users_service.mapper.VenueMapper;
import by.space.users_service.model.mysql.venue.curators.VenueCuratorRepository;
import by.space.users_service.model.mysql.venue.curators.VenueCuratorsEntity;
import by.space.users_service.service.VenueCuratorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VenueCuratorServiceImpl implements VenueCuratorService {

    private final VenueCuratorRepository venueCuratorRepository;
    private final VenueMapper venueMapper;

    @Override
    public List<Long> getAllActiveUserVenue(final Long userId) {
        final List<VenueCuratorsEntity> venues = venueCuratorRepository.findAllByCuratorIdAndDeletedIsFalse(userId);
        return venues.stream()
            .map(VenueCuratorsEntity::getVenueId)
            .collect(Collectors.toList());
    }

    @Override
    public void createVenueCurator(final Long curatorId, final Long venueId, final List<Long> addressesId, final Boolean isUserAdmin) {
        final List<VenueCuratorsEntity> venueCurators = new ArrayList<>();
        addressesId.forEach(address -> {
            venueCurators.add(VenueCuratorsEntity
                .builder()
                .curatorId(curatorId)
                .venueId(venueId)
                .addressId(address)
                .isUserAdmin(isUserAdmin)
                .build());
        });
        venueCuratorRepository.saveAll(venueCurators);
    }
}
