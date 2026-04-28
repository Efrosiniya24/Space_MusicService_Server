package by.space.personalization.preferences.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Entity
@Table(
		name = "preference_day",
		uniqueConstraints = @UniqueConstraint(
				name = "uk_preference_day_block_weekday",
				columnNames = {"block_id", "weekday"}
		)
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreferenceDayEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "block_id", nullable = false)
	private Long blockId;

	@Column(nullable = false)
	private Byte weekday;
}
