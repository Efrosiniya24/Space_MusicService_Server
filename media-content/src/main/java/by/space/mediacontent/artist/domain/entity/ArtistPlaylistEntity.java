package by.space.mediacontent.artist.domain.entity;

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
@Table(name = "artist_playlist")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistPlaylistEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private Long artistId;
    private Long playlistId;
    private boolean deleted = false;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
}
