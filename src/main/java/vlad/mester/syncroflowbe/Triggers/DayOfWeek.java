package vlad.mester.syncroflowbe.Triggers;

import lombok.Getter;
import org.json.simple.JSONObject;
import vlad.mester.syncroflowbe.Enums.DaysOfWeekEnum;
import vlad.mester.syncroflowbe.base.Triggers;

import java.time.LocalDate;

@Getter
public class DayOfWeek extends Triggers {
    private final String day;
    public static final String type = "Day Of Week";

    public DayOfWeek(String name, DaysOfWeekEnum daysOfWeek) {
        super(name, type, "Day " + daysOfWeek.getDay());
        this.day = daysOfWeek.getDay();
    }

    @Override
    public boolean evaluate() {
        return LocalDate.now().getDayOfWeek().toString().equalsIgnoreCase(getDay());
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject trigger = super.getJSONObject();
        trigger.put("day", this.day);
        return trigger;
    }
}
