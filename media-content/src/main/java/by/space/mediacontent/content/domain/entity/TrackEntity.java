package by.space.mediacontent.content.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "track")
public class TrackEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long duration;
    private Long idCover;
    private String bucket;
    private String objectKey;
    /** Original upload filename for admin display. */
    private String originalFileName;
    @Column(name = "is_single")
    private boolean single;
    private boolean removed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
