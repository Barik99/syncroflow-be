package vlad.mester.syncroflowbe.Enums;

import lombok.Getter;

@Getter
public enum DaysOfWeekEnum {
    MONDAY("MONDAY"),
    TUESDAY("TUESDAY"),
    WEDNESDAY("WEDNESDAY"),
    THURSDAY("THURSDAY"),
    FRIDAY("FRIDAY"),
    SATURDAY("SATURDAY"),
    SUNDAY("SUNDAY");

    private final String day;

    DaysOfWeekEnum(String day) {
        this.day = day;
    }

}
