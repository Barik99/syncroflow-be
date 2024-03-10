package vlad.mester.syncroflowbe.Triggers;

import lombok.Getter;
import vlad.mester.syncroflowbe.Enums.DaysOfWeekEnum;
import vlad.mester.syncroflowbe.Enums.TypesOfTriggers;
import vlad.mester.syncroflowbe.base.Triggers;

import java.time.LocalDate;

@Getter
public class DayOfWeek extends Triggers {
    private final String day;

    public DayOfWeek(String name, DaysOfWeekEnum daysOfWeek) {
        super(name, TypesOfTriggers.DAY_OF_WEEK.name(), "Day " + daysOfWeek.getDay());
        this.day = daysOfWeek.getDay();
    }

    @Override
    public boolean evaluate() {
        return LocalDate.now().getDayOfWeek().toString().equalsIgnoreCase(getDay());
    }
}
