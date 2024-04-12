package vlad.mester.syncroflowbe.Triggers;

import lombok.Getter;
import org.json.simple.JSONObject;
import vlad.mester.syncroflowbe.RuleController;
import vlad.mester.syncroflowbe.base.Triggers;

@Getter
public class NOT extends Triggers {
    private final String trigger;
    private final RuleController ruleController;
    public static final String type = "NOT";

    public NOT(String name, String trigger, String id) {
        super(name, type, "NOT " + trigger);
        this.trigger = trigger;
        this.ruleController = RuleController.getInstance(id);
    }

    @Override
    public boolean evaluate() {
        return !ruleController.getTriggerByName(trigger).evaluate();
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject trigger = super.getJSONObject();
        trigger.put("trigger", this.trigger);
        return trigger;
    }
}
