package by.space.users_service.service.search;

import by.space.users_service.model.elasticsearch.VenueSearchDocument;
import by.space.users_service.model.elasticsearch.VenueSearchRepository;
import by.space.users_service.model.mysql.domain.venue.VenueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class VenueSearchSync {
    private static final int MAX_ATTEMPTS = 90;
    private static final long SLEEP_MS = 2000L;

    private final VenueRepository venueRepository;
    private final VenueSearchRepository venueSearchRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    @EventListener(ApplicationReadyEvent.class)
    public void rebuildIndex() {
        final var docs = venueRepository.findAll().stream()
            .map(venue -> VenueSearchDocument.builder()
                .id(venue.getId())
                .name(venue.getName())
                .deleted(venue.isDeleted())
                .build())
            .toList();

        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            try {
                final IndexOperations indexOps = elasticsearchOperations.indexOps(VenueSearchDocument.class);
                if (!indexOps.exists()) {
                    indexOps.createWithMapping();
                }
                venueSearchRepository.saveAll(docs);
                log.info("Venues search index synced ({} documents)", docs.size());
                return;
            } catch (Exception ex) {
                log.warn("Elasticsearch not ready for venues index sync (attempt {}/{}): {}",
                    attempt, MAX_ATTEMPTS, ex.toString());
                sleepQuiet();
            }
        }
        log.error("Giving up syncing venues search index after {} attempts", MAX_ATTEMPTS);
    }

    private static void sleepQuiet() {
        try {
            Thread.sleep(SLEEP_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
