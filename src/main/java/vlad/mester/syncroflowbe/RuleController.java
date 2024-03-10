package vlad.mester.syncroflowbe;


import lombok.Data;
import vlad.mester.syncroflowbe.base.Actions;
import vlad.mester.syncroflowbe.base.Rule;
import vlad.mester.syncroflowbe.base.Triggers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class RuleController {
    public static RuleController instance;
    private List<Rule> rules;
    private List<Triggers> triggers;
    private List<Actions> actions;
    private List<RuleControllerObserver> controllerObservers;
    private Map<String, Integer> usedInAction = new HashMap<>();
    private Map<String, Integer> usedInTrigger = new HashMap<>();

    private RuleController() {
        this.rules = new ArrayList<>();
        this.triggers = new ArrayList<>();
        this.actions = new ArrayList<>();
        this.controllerObservers = new ArrayList<>();
    }

    public static synchronized RuleController getInstance() {
        if (instance == null) {
            instance = new RuleController();
        }
        return instance;
    }

    public void addObserver(RuleControllerObserver observer) {
        controllerObservers.add(observer);
    }

    public void removeObserver(RuleControllerObserver observer) {
        controllerObservers.remove(observer);
    }

    public void notifyObservers() {
        for (RuleControllerObserver observer : controllerObservers) {
            observer.update(this);
        }
    }

    public void addRule(Rule rule) {
        if (!rules.contains(rule)) {
            rules.add(rule);
            notifyObservers();
        }
    }


    public void addUsedTrigger(String triggerName) {
        if (!usedInTrigger.containsKey(triggerName)) {
            usedInTrigger.put(triggerName, 0);
        }
        usedInTrigger.replace(triggerName, usedInTrigger.get(triggerName) + 1);
        notifyObservers();
    }


    public void addUsedAction(String actionName) {
        if (!usedInAction.containsKey(actionName)) {
            usedInAction.put(actionName, 0);
        }
        usedInAction.replace(actionName, usedInAction.get(actionName) + 1);
        notifyObservers();
    }

    public void deleteRule(String ruleName) {
        rules.removeIf(rule -> rule.getName().equals(ruleName));
        notifyObservers();
    }

    public void addTrigger(Triggers triggers) {
        if (!this.triggers.contains(triggers)) {
            this.triggers.add(triggers);
            notifyObservers();
        }
    }

    public void deleteTrigger(String triggerName) {
        if (usedInTrigger.containsKey(triggerName)) {
            usedInTrigger.replace(triggerName, usedInTrigger.get(triggerName) - 1);
            if (usedInTrigger.get(triggerName)==0){
                usedInTrigger.remove(triggerName);
            }
        }
        if (!usedInTrigger.containsKey(triggerName)){
            triggers.removeIf(triggers -> triggers.getName().equals(triggerName));
        }
        notifyObservers();
    }

    public void addAction(Actions action) {
        if (!actions.contains(action)) {
            actions.add(action);
            notifyObservers();
        }
    }

    public void deleteActions(String actionName) {
        if (usedInAction.containsKey(actionName)) {
            usedInAction.replace(actionName, usedInAction.get(actionName) - 1);
            if (usedInAction.get(actionName)==0){
                usedInAction.remove(actionName);
            }
        }
        if (!usedInAction.containsKey(actionName)){
            actions.removeIf(action -> action.getName().equals(actionName));
        }
        notifyObservers();
    }

    public boolean isTriggerUsed(Triggers selectedTrigger){
        for(Rule rule : rules){
            if (rule.getTrigger().equals(selectedTrigger)){
                return true;
            }
        }
        return false;
    }

    public boolean isActionUsed(Actions selectedAction){
        for(Rule rule : rules){
            if (rule.getAction().equals(selectedAction)){
                return true;
            }
        }
        return false;
    }

    public Triggers getTriggerByName(String triggerName){
        for (Triggers trigger : triggers){
            if (trigger.getName().equals(triggerName)){
                return trigger;
            }
        }
        return null;
    }

    public Actions getActionByName(String actionName){
        for (Actions action : actions){
            if (action.getName().equals(actionName)){
                return action;
            }
        }
        return null;
    }
}
