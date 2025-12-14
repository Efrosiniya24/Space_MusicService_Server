package by.space.mediacontent.artist.domain.entity;

import jakarta.persistence.Column;
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
@Table(name = "artist")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(nullable = false)
    private String name;
    private String cover;
    private String description;
    @Column(name = "is_deleted")
    private boolean deleted = false;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
