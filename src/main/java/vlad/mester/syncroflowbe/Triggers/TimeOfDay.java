package vlad.mester.syncroflowbe.Triggers;

import lombok.Getter;
import vlad.mester.syncroflowbe.Enums.TypesOfTriggers;
import vlad.mester.syncroflowbe.base.Triggers;

import java.time.LocalTime;

@Getter
public class TimeOfDay extends Triggers {
    private final int hours;
    private final int minutes;

    public TimeOfDay(String name, int hours, int minutes) {
        super(name, TypesOfTriggers.TIME_OF_DAY.name(), hours + ": " + minutes);
        this.hours = hours;
        this.minutes = minutes;
    }

    @Override
    public boolean evaluate() {
        LocalTime currentTime = LocalTime.now();
        return currentTime.getHour() >= hours && currentTime.getMinute() >= minutes;
    }
}
