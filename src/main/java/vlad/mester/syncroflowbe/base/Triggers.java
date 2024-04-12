package vlad.mester.syncroflowbe.base;

import lombok.Data;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import vlad.mester.syncroflowbe.Enums.DaysOfWeekEnum;
import vlad.mester.syncroflowbe.Triggers.*;

import java.io.File;
import java.io.IOException;

@Data
public abstract class Triggers {
    private String name;
    private String type;
    private String value;

    public Triggers(String name, String type, String value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString(){return name;}

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Triggers triggers) {
            return this.name.equals(triggers.name);
        }
        return false;
    }

    public abstract boolean evaluate();

    public JSONObject getJSONObject() {
        JSONObject trigger = new JSONObject();
        trigger.put("name", this.name);
        trigger.put("type", this.type);
        trigger.put("value", this.value);
        return trigger;
    }

    public static Triggers fromJSONObject(String jsonTrigger, String id) throws IOException {
        JSONObject trigger = (JSONObject) JSONValue.parse(jsonTrigger);
        String type = (String) trigger.get("type");
        switch (type) {
            case TimeOfDay.type:
                return new TimeOfDay((String) trigger.get("name"), ((Long) trigger.get("hours")).intValue(), ((Long) trigger.get("minutes")).intValue());
            case OR.type:
                return new OR((String) trigger.get("name"), (String) trigger.get("firstTrigger"), (String) trigger.get("secondTrigger"), id);
            case NOT.type:
                return new NOT((String) trigger.get("name"), (String) trigger.get("trigger"), id);
            case AND.type:
                return new AND((String) trigger.get("name"), (String) trigger.get("firstTrigger"), (String) trigger.get("secondTrigger"), id);
            case DayOfMonth.type:
                return new DayOfMonth((String) trigger.get("name"), ((Long) trigger.get("day")).intValue());
            case DayOfWeek.type:
                return new DayOfWeek((String) trigger.get("name"), (DaysOfWeekEnum.valueOf((String) trigger.get("day"))));
            case ExternalProgram.type:
                return new ExternalProgram((String) trigger.get("name"), new File((String) trigger.get("externalProgram")), (String) trigger.get("commandLineArguments"), ((Long) trigger.get("exitStatus")).intValue());
            case FileExistence.type:
                return new FileExistence((String) trigger.get("name"), new File((String) trigger.get("file")));
            case FileSize.type:
                return new FileSize((String) trigger.get("name"), new File((String) trigger.get("file")), ((Long) trigger.get("sizeThreshold")).intValue());
            default:
                throw new IOException("Invalid trigger type");
        }
    }
}
