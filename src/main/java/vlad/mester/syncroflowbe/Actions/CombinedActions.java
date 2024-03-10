package vlad.mester.syncroflowbe.Actions;

import lombok.Getter;
import vlad.mester.syncroflowbe.Enums.TypesOfAction;
import vlad.mester.syncroflowbe.RuleController;
import vlad.mester.syncroflowbe.base.Actions;

@Getter
public class CombinedActions extends Actions {
    private final Actions firstAction;
    private final Actions secondAction;
    private final RuleController ruleController;

    public CombinedActions(String name, Actions firstAction, Actions secondAction) {
        super(name, TypesOfAction.COMBINED_ACTIONS.name(), firstAction.getName() + " + " + secondAction.getName());
        this.firstAction = firstAction;
        this.secondAction = secondAction;
        this.ruleController = RuleController.getInstance();
        this.ruleController.addUsedAction(firstAction.getName());
        this.ruleController.addUsedAction(secondAction.getName());
    }

    public CombinedActions(String name, String firstAction, String secondAction) {
        super(name, TypesOfAction.COMBINED_ACTIONS.name(), firstAction + " + " + secondAction);
        this.ruleController = RuleController.getInstance();
        this.firstAction = ruleController.getActionByName(firstAction);
        this.secondAction = ruleController.getActionByName(secondAction);
        this.ruleController.addUsedAction(firstAction);
        this.ruleController.addUsedAction(secondAction);
    }

    @Override
    public boolean execute() {
        try{
            firstAction.execute();
            secondAction.execute();
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
