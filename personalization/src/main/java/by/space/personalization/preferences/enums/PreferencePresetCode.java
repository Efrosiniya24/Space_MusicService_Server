package by.space.personalization.preferences.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum PreferencePresetCode {
    MORNING("morning"),
    DAY("day"),
    EVENING("evening"),
    NIGHT("night");

    private final String code;

    public static PreferencePresetCode fromWire(final String raw) {
        if (Objects.isNull(raw) || raw.isBlank()) {
            throw new IllegalArgumentException("time preset is blank");
        }
        final String s = raw.trim();
        for (final PreferencePresetCode c : values()) {
            if (c.name().equalsIgnoreCase(s) || c.code.equalsIgnoreCase(s)) {
                return c;
            }
        }
        throw new IllegalArgumentException("unknown time preset: " + raw);
    }

    public String toWireValue() {
        return code;
    }
}
