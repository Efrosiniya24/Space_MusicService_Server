package by.space.personalization.preferences.entity;

import by.space.personalization.preferences.enums.VolumeLevel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
		name = "preference_volume",
		uniqueConstraints = @UniqueConstraint(
				name = "uk_preference_volume_pref_level",
				columnNames = {"preference_id", "volume_level"}
		)
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreferenceVolumeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long preferenceId;

	@Enumerated(EnumType.STRING)
	@Column(name = "volume_level", nullable = false, length = 16)
	private VolumeLevel volumeLevel;

	private LocalDateTime createdAt = LocalDateTime.now();
}
