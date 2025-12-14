package by.space.users_service.model.mysql.venue.curators;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "venue_curator")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VenueCuratorsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long curatorId;
    private Long venueId;
    private Long addressId;
    private Boolean userOwner;
    private boolean deleted = false;
}
