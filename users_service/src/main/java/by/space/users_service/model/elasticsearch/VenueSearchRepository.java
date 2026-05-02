package by.space.users_service.model.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VenueSearchRepository extends ElasticsearchRepository<VenueSearchDocument, Long> {
    List<VenueSearchDocument> findByNameContainingIgnoreCaseAndDeletedFalse(String name);
}
