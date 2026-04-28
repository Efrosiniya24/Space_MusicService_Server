package by.space.personalization.preferences.entity;

import by.space.personalization.preferences.enums.PreferenceTimeKind;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(name = "preference_time")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreferenceTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "block_id", nullable = false)
	private Long blockId;

	@Enumerated(EnumType.STRING)
	@Column(name = "time_kind", nullable = false, length = 16)
	private PreferenceTimeKind timeKind;

	@Column(name = "preset_code", length = 32)
	private String presetCode;

	@Column(name = "time_from")
	private LocalTime timeFrom;

	@Column(name = "time_to")
	private LocalTime timeTo;

	@Column(nullable = false)
	@Builder.Default
	private Integer sortOrder = 0;
}
