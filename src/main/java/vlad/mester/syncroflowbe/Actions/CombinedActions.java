package vlad.mester.syncroflowbe.Actions;

import lombok.Getter;
import org.json.simple.JSONObject;
import vlad.mester.syncroflowbe.RuleController;
import vlad.mester.syncroflowbe.base.Actions;

@Getter
public class CombinedActions extends Actions {
    private final String firstAction;
    private final String secondAction;
    private final RuleController ruleController;
    public static final String type = "Combined Actions";

    public CombinedActions(String name, String firstAction, String secondAction, String id) {
        super(name, type, "Execută acțiunea " + secondAction + " și apoi acțiunea " + firstAction);
        this.firstAction = firstAction;
        this.secondAction = secondAction;
        this.ruleController = RuleController.getInstance(id);
    }

    @Override
    public boolean execute() {
        try{
            ruleController.getActionByName(firstAction).execute();
            ruleController.getActionByName(secondAction).execute();
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject action = super.getJSONObject();
        action.put("firstAction", this.firstAction);
        action.put("secondAction", this.secondAction);
        return action;
    }
}
