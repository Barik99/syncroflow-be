package vlad.mester.syncroflowbe.Triggers;

import lombok.Getter;
import org.json.simple.JSONObject;
import vlad.mester.syncroflowbe.base.Triggers;

import java.time.LocalDate;

@Getter
public class DayOfMonth extends Triggers {
    private final int day;
    public static final String type = "Day Of Month";

    public DayOfMonth(String name, int day) {
        super(name, type, "Ziua curentă este " + day);
        this.day = day;
    }

    @Override
    public boolean evaluate() {
        return day == LocalDate.now().getDayOfMonth();
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject trigger = super.getJSONObject();
        trigger.put("day", this.day);
        return trigger;
    }
}
