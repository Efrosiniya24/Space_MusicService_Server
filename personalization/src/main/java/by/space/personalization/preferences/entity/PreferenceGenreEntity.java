package by.space.personalization.preferences.entity;

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
@Table(name = "preference_genre")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PreferenceGenreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long genreId;
    private Long preferenceId;
    private LocalDateTime createdAt = LocalDateTime.now();
}
