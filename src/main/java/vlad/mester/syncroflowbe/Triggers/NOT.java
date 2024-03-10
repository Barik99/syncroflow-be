package vlad.mester.syncroflowbe.Triggers;

import lombok.Getter;
import vlad.mester.syncroflowbe.Enums.TypesOfTriggers;
import vlad.mester.syncroflowbe.RuleController;
import vlad.mester.syncroflowbe.base.Triggers;

import java.util.HashMap;

@Getter
public class NOT extends Triggers {
    private final Triggers trigger;
    private final RuleController ruleController;

    public NOT(String name, Triggers trigger) {
        super(name, TypesOfTriggers.NOT.name(), "NOT " + trigger.getName());
        this.trigger = trigger;
        this.ruleController = RuleController.getInstance();
        this.ruleController.addUsedTrigger(trigger.getName());
    }

    public NOT(String name, String triggerName) {
        super(name, TypesOfTriggers.NOT.name(), "NOT " + triggerName);
        this.ruleController = RuleController.getInstance();
        this.trigger = ruleController.getTriggerByName(triggerName);
        this.ruleController.addUsedTrigger(triggerName);
    }

    @Override
    public boolean evaluate() {
        return !trigger.evaluate();
    }
}
