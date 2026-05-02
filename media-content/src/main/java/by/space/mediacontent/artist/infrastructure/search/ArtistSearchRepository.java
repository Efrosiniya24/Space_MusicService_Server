package by.space.mediacontent.artist.infrastructure.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistSearchRepository extends ElasticsearchRepository<ArtistSearchDocument, Long> {
    List<ArtistSearchDocument> findByNameContainingIgnoreCaseAndDeletedFalse(String name);
}
