package by.space.mediacontent.artist.infrastructure.search;

import by.space.mediacontent.artist.infrastructure.repository.ArtistRepository;
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
public class ArtistSearchSync {
    private static final int MAX_ATTEMPTS = 90;
    private static final long SLEEP_MS = 2000L;

    private final ArtistRepository artistRepository;
    private final ArtistSearchRepository artistSearchRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    @EventListener(ApplicationReadyEvent.class)
    public void rebuildIndex() {
        final var docs = artistRepository.findAll().stream()
            .map(artist -> ArtistSearchDocument.builder()
                .id(artist.getId())
                .name(artist.getName())
                .deleted(artist.isDeleted())
                .build())
            .toList();

        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            try {
                final IndexOperations indexOps = elasticsearchOperations.indexOps(ArtistSearchDocument.class);
                if (!indexOps.exists()) {
                    indexOps.createWithMapping();
                }
                artistSearchRepository.saveAll(docs);
                log.info("Artists search index synced ({} documents)", docs.size());
                return;
            } catch (Exception ex) {
                log.warn("Elasticsearch not ready for artists index sync (attempt {}/{}): {}",
                    attempt, MAX_ATTEMPTS, ex.toString());
                sleepQuiet();
            }
        }
        log.error("Giving up syncing artists search index after {} attempts", MAX_ATTEMPTS);
    }

    private static void sleepQuiet() {
        try {
            Thread.sleep(SLEEP_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
