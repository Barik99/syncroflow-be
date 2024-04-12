package vlad.mester.syncroflowbe.base;

import lombok.Data;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.Date;

@Data
public class Rule {
    private String name;
    private String trigger;
    private String action;
    private boolean multiUse;
    private Date lastUse;
    private int sleepTime;
    private boolean active;

    public Rule(String name, String trigger, String action, boolean active, boolean multiUse, int sleepTime) {
        this.name = name;
        this.trigger = trigger;
        this.action = action;
        this.active = active;
        this.multiUse = multiUse;
        this.lastUse = null;
        this.sleepTime = sleepTime;
    }

    public Rule(String name, String trigger, String action, boolean active, boolean multiUse, Date lastUse, int sleepTime) {
        this.name = name;
        this.trigger = trigger;
        this.action = action;
        this.active = active;
        this.multiUse = multiUse;
        this.lastUse = lastUse;
        this.sleepTime = sleepTime;
    }

    public void setLastUse() {
        this.lastUse = new Date();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Rule rule) {
            return this.name.equals(rule.name);
        }
        return false;
    }

    //make a method that returns a JSON object with the rule's fields
    public JSONObject getJSONObject() {
        JSONObject rule = new JSONObject();
        rule.put("name", this.name);
        rule.put("trigger", this.trigger);
        rule.put("action", this.action);
        rule.put("active", this.active);
        rule.put("multiUse", this.multiUse);
        rule.put("lastUse", this.lastUse);
        rule.put("sleepTime", this.sleepTime);
        return rule;
    }

    //make a method that creates a Rule from json
    public static Rule fromJSONObject(String jsonRule) {
        JSONObject rule = (JSONObject) JSONValue.parse(jsonRule);
        return new Rule(
                (String) rule.get("name"),
                (String) rule.get("trigger"),
                (String) rule.get("action"),
                (boolean) rule.get("active"),
                (boolean) rule.get("multiUse"),
                ((Long) rule.get("sleepTime")).intValue()
        );
    }
}
