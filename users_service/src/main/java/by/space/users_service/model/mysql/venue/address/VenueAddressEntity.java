package by.space.users_service.model.mysql.venue.address;

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

import java.time.LocalDateTime;

@Entity
@Table(name = "venue_address")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VenueAddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String country;
    private String city;
    private String addressCity;
    private Long venueId;
    private boolean deleted = false;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
}
