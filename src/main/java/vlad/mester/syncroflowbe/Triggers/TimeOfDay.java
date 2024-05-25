package vlad.mester.syncroflowbe.Triggers;

import lombok.Getter;
import org.json.simple.JSONObject;
import vlad.mester.syncroflowbe.base.Triggers;

import java.time.LocalTime;

@Getter
public class TimeOfDay extends Triggers {
    private final int hours;
    private final int minutes;
    public static final String type = "Time Of Day";

    public TimeOfDay(String name, int hours, int minutes) {
        super(name, type, "Ora curentă este egală cu " + hours + ":" + minutes);
        this.hours = hours;
        this.minutes = minutes;
    }

    @Override
    public boolean evaluate() {
        LocalTime currentTime = LocalTime.now();
        return currentTime.getHour() >= hours && currentTime.getMinute() >= minutes;
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject trigger = super.getJSONObject();
        trigger.put("hours", this.hours);
        trigger.put("minutes", this.minutes);
        return trigger;
    }
}
