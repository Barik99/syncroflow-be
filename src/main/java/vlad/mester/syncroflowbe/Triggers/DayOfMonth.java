package vlad.mester.syncroflowbe.Triggers;

import lombok.Getter;
import vlad.mester.syncroflowbe.Enums.TypesOfTriggers;
import vlad.mester.syncroflowbe.base.Triggers;

import java.time.LocalDate;

@Getter
public class DayOfMonth extends Triggers {
    private final int day;

    public DayOfMonth(String name, int day) {
        super(name, TypesOfTriggers.DAY_OF_MONTH.name(), "Day " + day);
        this.day = day;
    }

    @Override
    public boolean evaluate() {
        return day == LocalDate.now().getDayOfMonth();
    }
}
