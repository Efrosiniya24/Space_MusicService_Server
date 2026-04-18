package by.space.users_service.service.impl;

import by.space.users_service.mapper.VenueCuratorMapper;
import by.space.users_service.model.dto.UserAuthDto;
import by.space.users_service.model.dto.VenueCuratorDto;
import by.space.users_service.model.mysql.domain.venue.curators.VenueCuratorRepository;
import by.space.users_service.model.mysql.domain.venue.curators.VenueCuratorsEntity;
import by.space.users_service.service.UserService;
import by.space.users_service.service.VenueCuratorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VenueCuratorServiceImpl implements VenueCuratorService {

    private final VenueCuratorRepository venueCuratorRepository;
    private final VenueCuratorMapper venueCuratorMapper;
    private final UserService userService;

    @Override
    public List<VenueCuratorDto> getAllActiveUserVenue(final Long userId) {
        final List<VenueCuratorsEntity> venues = venueCuratorRepository.findAllByCuratorIdAndDeletedIsFalse(userId);
        return venueCuratorMapper.mapToVenueCuratorDto(venues);
    }

    @Override
    public void createVenueCurator(
        final Long curatorId,
        final Long venueId,
        final List<Long> addressesId,
        final Boolean isUserAdmin
    ) {
        final List<VenueCuratorsEntity> venueCurators = new ArrayList<>();
        addressesId.forEach(address ->
            venueCurators.add(VenueCuratorsEntity
                .builder()
                .curatorId(curatorId)
                .venueId(venueId)
                .addressId(address)
                .isUserAdmin(isUserAdmin)
                .build())
        );
        venueCuratorRepository.saveAll(venueCurators);
    }

    @Transactional
    @Override
    public List<VenueCuratorDto> getAllVenueCurators(final Long venueId) {
        final List<VenueCuratorDto> venueCurators = venueCuratorMapper.mapToVenueCuratorDto(
            venueCuratorRepository.findAllByDeletedIsFalseAndVenueId(venueId)
        );

        final List<Long> curatorsId = venueCurators.stream()
            .map(VenueCuratorDto::getCuratorId)
            .distinct()
            .toList();

        if (curatorsId.isEmpty()) {
            return venueCurators;
        }

        final Map<Long, UserAuthDto> userById = userService.getAllUsersByIds(curatorsId).stream()
            .collect(Collectors.toMap(UserAuthDto::getId, Function.identity()));

        venueCurators.forEach(venueCurator -> venueCurator.setUser(userById.get(venueCurator.getCuratorId())));

        return venueCurators;
    }
}
