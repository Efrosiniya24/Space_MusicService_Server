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

import java.time.LocalDate;

@Entity
@Table(
		name = "preference_schedule_date",
		uniqueConstraints = @UniqueConstraint(
				name = "uk_pref_sched_date_block_date",
				columnNames = {"block_id", "specific_date"}
		)
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreferenceScheduleDateEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "block_id", nullable = false)
	private Long blockId;

	@Column(name = "specific_date", nullable = false)
	private LocalDate specificDate;
}
