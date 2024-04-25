package vlad.mester.syncroflowbe.Triggers;

import lombok.Getter;
import org.json.simple.JSONObject;
import vlad.mester.syncroflowbe.RuleController;
import vlad.mester.syncroflowbe.base.Triggers;

@Getter
public class OR extends Triggers {
    private final String firstTrigger;
    private final String secondTrigger;
    private final RuleController ruleController;
    public static final String type = "OR";

    public OR(String name, String firstTrigger, String secondTrigger, String id) {
        super(name, type, firstTrigger + " OR " + secondTrigger);
        this.firstTrigger = firstTrigger;
        this.secondTrigger = secondTrigger;
        this.ruleController = RuleController.getInstance(id);
    }

    @Override
    public boolean evaluate() {
        return ruleController.getTriggerByName(firstTrigger).evaluate() || ruleController.getTriggerByName(secondTrigger).evaluate();
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject trigger = super.getJSONObject();
        trigger.put("firstTrigger", this.firstTrigger);
        trigger.put("secondTrigger", this.secondTrigger);
        return trigger;
    }
}
