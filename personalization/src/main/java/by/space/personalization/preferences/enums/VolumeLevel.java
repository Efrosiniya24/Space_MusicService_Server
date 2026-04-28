package by.space.personalization.preferences.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VolumeLevel {
	QUIET("quiet"),
	MEDIUM("medium"),
	LOUD("loud");

	private final String code;
}
