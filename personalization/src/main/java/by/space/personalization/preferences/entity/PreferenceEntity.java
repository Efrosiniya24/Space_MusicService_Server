package by.space.personalization.preferences.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "preference")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PreferenceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long venueId;
    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    private boolean timeIrrelevant = false;
    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    private boolean deleted = false;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
}
