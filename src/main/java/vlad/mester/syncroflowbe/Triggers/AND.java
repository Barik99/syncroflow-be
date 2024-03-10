package vlad.mester.syncroflowbe.Triggers;

import lombok.Getter;
import vlad.mester.syncroflowbe.Enums.TypesOfTriggers;
import vlad.mester.syncroflowbe.RuleController;
import vlad.mester.syncroflowbe.base.Triggers;

@Getter
public class AND extends Triggers {
    private final Triggers firstTrigger;
    private final Triggers secondTrigger;
    private final RuleController ruleController;

    public AND(String name, Triggers firstTrigger, Triggers secondTrigger) {
        super(name, TypesOfTriggers.AND.name(), firstTrigger.getName() + "AND" + secondTrigger.getName());
        this.firstTrigger = firstTrigger;
        this.secondTrigger = secondTrigger;
        this.ruleController = RuleController.getInstance();
        this.ruleController.addUsedTrigger(firstTrigger.getName());
        this.ruleController.addUsedTrigger(secondTrigger.getName());
    }

    public AND(String name, String firstTriggerName, String secondTriggerName) {
        super(name, TypesOfTriggers.AND.name(), firstTriggerName + "AND" + secondTriggerName);
        this.ruleController = RuleController.getInstance();
        this.firstTrigger = ruleController.getTriggerByName(firstTriggerName);
        this.secondTrigger = ruleController.getTriggerByName(secondTriggerName);
        this.ruleController.addUsedTrigger(firstTriggerName);
        this.ruleController.addUsedTrigger(secondTriggerName);
    }

    @Override
    public boolean evaluate() {
        return firstTrigger.evaluate() && secondTrigger.evaluate();
    }
}
