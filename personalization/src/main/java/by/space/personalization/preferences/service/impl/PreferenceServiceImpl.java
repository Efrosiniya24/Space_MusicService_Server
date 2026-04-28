package by.space.personalization.preferences.service.impl;

import by.space.personalization.preferences.dto.CreatePreferenceRequest;
import by.space.personalization.preferences.dto.CustomIntervalRequest;
import by.space.personalization.preferences.dto.ScheduleBlockRequest;
import by.space.personalization.preferences.entity.PreferenceAddressEntity;
import by.space.personalization.preferences.entity.PreferenceDayEntity;
import by.space.personalization.preferences.entity.PreferenceEntity;
import by.space.personalization.preferences.entity.PreferenceGenreEntity;
import by.space.personalization.preferences.entity.PreferenceScheduleBlockEntity;
import by.space.personalization.preferences.entity.PreferenceScheduleDateEntity;
import by.space.personalization.preferences.entity.PreferenceTimeEntity;
import by.space.personalization.preferences.entity.PreferenceVolumeEntity;
import by.space.personalization.preferences.enums.PreferenceTimeKind;
import by.space.personalization.preferences.enums.VolumeLevel;
import by.space.personalization.preferences.repository.PreferenceAddressRepository;
import by.space.personalization.preferences.repository.PreferenceDayRepository;
import by.space.personalization.preferences.repository.PreferenceGenreRepository;
import by.space.personalization.preferences.repository.PreferenceRepository;
import by.space.personalization.preferences.repository.PreferenceScheduleBlockRepository;
import by.space.personalization.preferences.repository.PreferenceScheduleDateRepository;
import by.space.personalization.preferences.repository.PreferenceTimeRepository;
import by.space.personalization.preferences.repository.PreferenceVolumeRepository;
import by.space.personalization.preferences.service.PreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PreferenceServiceImpl implements PreferenceService {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final PreferenceRepository preferenceRepository;
    private final PreferenceAddressRepository preferenceAddressRepository;
    private final PreferenceGenreRepository preferenceGenreRepository;
    private final PreferenceVolumeRepository preferenceVolumeRepository;
    private final PreferenceScheduleBlockRepository preferenceScheduleBlockRepository;
    private final PreferenceDayRepository preferenceDayRepository;
    private final PreferenceTimeRepository preferenceTimeRepository;
    private final PreferenceScheduleDateRepository preferenceScheduleDateRepository;

    @Override
    @Transactional
    public Long create(CreatePreferenceRequest request) {
        final PreferenceEntity preference = PreferenceEntity.builder()
            .userId(request.getUserId())
            .venueId(request.getVenueId())
            .timeIrrelevant(hasGlobalTimeIrrelevant(request.getScheduleBlocks()))
            .deleted(false)
            .createdAt(LocalDateTime.now())
            .build();

        final PreferenceEntity savedPreference = preferenceRepository.save(preference);
        final Long preferenceId = savedPreference.getId();

        saveAddresses(preferenceId, request.getAddressIds());
        saveGenres(preferenceId, request.getGenreIds());
        saveVolumes(preferenceId, request.getVolumeLevels());
        saveSchedule(preferenceId, request.getScheduleBlocks());

        return preferenceId;
    }

    private void saveAddresses(Long preferenceId, List<Long> addressIds) {
        Set<Long> uniqueAddressIds = toUniqueLongSet(addressIds);
        List<PreferenceAddressEntity> entities = uniqueAddressIds.stream()
            .map(addressId -> PreferenceAddressEntity.builder()
                .preferenceId(preferenceId)
                .addressId(addressId)
                .createdAt(LocalDateTime.now())
                .build())
            .toList();
        preferenceAddressRepository.saveAll(entities);
    }

    private void saveGenres(Long preferenceId, List<Long> genreIds) {
        Set<Long> uniqueGenreIds = toUniqueLongSet(genreIds);
        List<PreferenceGenreEntity> entities = uniqueGenreIds.stream()
            .map(genreId -> PreferenceGenreEntity.builder()
                .preferenceId(preferenceId)
                .genreId(genreId)
                .createdAt(LocalDateTime.now())
                .build())
            .toList();
        preferenceGenreRepository.saveAll(entities);
    }

    private void saveVolumes(Long preferenceId, List<String> volumeLevels) {
        Set<VolumeLevel> uniqueLevels = new LinkedHashSet<>();
        if (volumeLevels != null) {
            for (String raw : volumeLevels) {
                if (raw == null || raw.isBlank()) {
                    continue;
                }
                uniqueLevels.add(VolumeLevel.valueOf(raw.trim().toUpperCase(Locale.ROOT)));
            }
        }
        List<PreferenceVolumeEntity> entities = uniqueLevels.stream()
            .map(level -> PreferenceVolumeEntity.builder()
                .preferenceId(preferenceId)
                .volumeLevel(level)
                .createdAt(LocalDateTime.now())
                .build())
            .toList();
        preferenceVolumeRepository.saveAll(entities);
    }

    private void saveSchedule(Long preferenceId, List<ScheduleBlockRequest> scheduleBlocks) {
        if (scheduleBlocks == null || scheduleBlocks.isEmpty()) {
            return;
        }
        for (int i = 0; i < scheduleBlocks.size(); i++) {
            ScheduleBlockRequest block = scheduleBlocks.get(i);
            PreferenceScheduleBlockEntity savedBlock = preferenceScheduleBlockRepository.save(
                PreferenceScheduleBlockEntity.builder()
                    .preferenceId(preferenceId)
                    .sortOrder(i)
                    .build()
            );
            Long blockId = savedBlock.getId();

            saveWeekDays(blockId, block.getWeekDays());
            saveTimeRows(blockId, block.getTimePresets(), block.getCustomIntervals(), Boolean.TRUE.equals(block.getTimeIrrelevant()));
            saveSpecificDates(blockId, block.getSpecificDates());
        }
    }

    private void saveWeekDays(Long blockId, List<Integer> weekDays) {
        if (weekDays == null || weekDays.isEmpty()) {
            return;
        }
        Set<Integer> uniqueDays = new LinkedHashSet<>(weekDays);
        List<PreferenceDayEntity> entities = uniqueDays.stream()
            .map(day -> PreferenceDayEntity.builder()
                .blockId(blockId)
                .weekday(day.byteValue())
                .build())
            .toList();
        preferenceDayRepository.saveAll(entities);
    }

    private void saveTimeRows(Long blockId, List<String> presets, List<CustomIntervalRequest> intervals, boolean timeIrrelevant) {
        if (timeIrrelevant) {
            return;
        }

        int sortOrder = 0;
        if (presets != null) {
            Set<String> uniquePresets = new LinkedHashSet<>(presets);
            for (String preset : uniquePresets) {
                if (preset == null || preset.isBlank()) {
                    continue;
                }
                preferenceTimeRepository.save(
                    PreferenceTimeEntity.builder()
                        .blockId(blockId)
                        .timeKind(PreferenceTimeKind.PRESET)
                        .presetCode(preset.trim())
                        .sortOrder(sortOrder++)
                        .build()
                );
            }
        }

        if (intervals != null) {
            for (CustomIntervalRequest interval : intervals) {
                if (interval == null || interval.getFrom() == null || interval.getTo() == null) {
                    continue;
                }
                preferenceTimeRepository.save(
                    PreferenceTimeEntity.builder()
                        .blockId(blockId)
                        .timeKind(PreferenceTimeKind.CUSTOM)
                        .timeFrom(LocalTime.parse(interval.getFrom(), TIME_FORMATTER))
                        .timeTo(LocalTime.parse(interval.getTo(), TIME_FORMATTER))
                        .sortOrder(sortOrder++)
                        .build()
                );
            }
        }
    }

    private void saveSpecificDates(Long blockId, List<String> specificDates) {
        if (specificDates == null || specificDates.isEmpty()) {
            return;
        }
        Set<String> uniqueDates = new LinkedHashSet<>(specificDates);
        List<PreferenceScheduleDateEntity> entities = uniqueDates.stream()
            .filter(value -> value != null && !value.isBlank())
            .map(value -> PreferenceScheduleDateEntity.builder()
                .blockId(blockId)
                .specificDate(LocalDate.parse(value))
                .build())
            .toList();
        preferenceScheduleDateRepository.saveAll(entities);
    }

    private Set<Long> toUniqueLongSet(List<Long> values) {
        Set<Long> result = new LinkedHashSet<>();
        if (values == null) {
            return result;
        }
        for (Long value : values) {
            if (value != null) {
                result.add(value);
            }
        }
        return result;
    }

    private boolean hasGlobalTimeIrrelevant(List<ScheduleBlockRequest> scheduleBlocks) {
        if (scheduleBlocks == null) {
            return false;
        }
        for (ScheduleBlockRequest scheduleBlock : scheduleBlocks) {
            if (scheduleBlock != null && Boolean.TRUE.equals(scheduleBlock.getTimeIrrelevant())) {
                return true;
            }
        }
        return false;
    }
}
