package vlad.mester.syncroflowbe;


import lombok.Data;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import vlad.mester.syncroflowbe.Actions.*;
import vlad.mester.syncroflowbe.Triggers.*;
import vlad.mester.syncroflowbe.base.Actions;
import vlad.mester.syncroflowbe.base.Rule;
import vlad.mester.syncroflowbe.base.Triggers;
import vlad.mester.syncroflowbe.services.ActionService;
import vlad.mester.syncroflowbe.services.RuleService;
import vlad.mester.syncroflowbe.services.TriggerService;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class RuleController {
    public static Map<String, RuleController> instance = new HashMap<>();
    private List<Rule> rules;
    private Map<Triggers, Integer> triggers;
    private Map<Actions, Integer> actions;
    private List<RuleControllerObserver> controllerObservers;
    private final TriggerService triggerService = new TriggerService();
    private final RuleService ruleService = new RuleService();
    private final ActionService actionService = new ActionService();
    private String id;
    private JSONParser parser = new JSONParser();

    private RuleController() {
        this.rules = new ArrayList<>();
        this.triggers = new HashMap<>();
        this.actions = new HashMap<>();
        this.controllerObservers = new ArrayList<>();
    }

    public static synchronized RuleController createInstance(String id) {
        if (!instance.containsKey(id) || instance.get(id) == null) {
            instance.put(id, new RuleController());
            instance.get(id).setId(id);
        }
        return instance.get(id);
    }

    public static synchronized RuleController getInstance(String id) {
        try {
            if (!instance.containsKey(id) || instance.get(id) == null) {
                throw new Exception("RuleController with id " + id + " does not exist");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance.get(id);
    }

    public static synchronized List<RuleController> getAllInstances() {
        List<RuleController> ruleControllers = new ArrayList<>();
        for (String id : instance.keySet()) {
            ruleControllers.add(instance.get(id));
        }
        return ruleControllers;
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

    public String addRule(Rule rule) {
        if (!containsRule(rule) && triggers.containsKey(getTriggerByName(rule.getTrigger())) && actions.containsKey(getActionByName(rule.getAction()))){
            if (ruleService.addRule(rule, id)) {
                rules.add(rule);
                addUsedTrigger(rule.getTrigger());
                addUsedAction(rule.getAction());
                notifyObservers();
                return "Rule added";
            }
            return "Rule could not be added";
        }
        return "Rule already exists or trigger or action does not exist";
    }

    public boolean containsRule(Rule rule) {
        for (Rule r : rules) {
            if (r.getName().equals(rule.getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean containsTrigger(Triggers trigger) {
        for (Triggers t : triggers.keySet()) {
            if (t.getName().equals(trigger.getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean containsAction(Actions action) {
        for (Actions a : actions.keySet()) {
            if (a.getName().equals(action.getName())) {
                return true;
            }
        }
        return false;
    }

    public void addUsedTrigger(String triggerName) {
        Triggers trigger = getTriggerByName(triggerName);
        triggers.replace(trigger, triggers.get(trigger) + 1);
    }

    public void removeUsedTrigger(String triggerName) {
        Triggers trigger = getTriggerByName(triggerName);
        triggers.replace(trigger, triggers.get(trigger) - 1);
    }

    public void removeUsedAction(String actionName) {
        Actions action = getActionByName(actionName);
        actions.replace(action, actions.get(action) - 1);
    }

    public void addUsedAction(String actionName) {
        Actions action = getActionByName(actionName);
        actions.replace(action, actions.get(action) + 1);
    }

    public String deleteRule(String ruleName) {
        Rule rule = getRuleByName(ruleName);
        if (rules.contains(rule) && rule != null) {
            if (ruleService.deleteRule(ruleName)) {
                removeUsedTrigger(rule.getTrigger());
                removeUsedAction(rule.getAction());
                rules.remove(rule);
                notifyObservers();
                return "Rule removed";
            }
            return "Rule could not be removed";
        }
        return "Rule does not exist";
    }

    public Rule getRuleByName(String ruleName){
        for (Rule rule : rules){
            if (rule.getName().equals(ruleName)){
                return rule;
            }
        }
        return null;
    }

    public String addTrigger(Triggers trigger) {
        if (!containsTrigger(trigger)) {
            switch (trigger.getType()) {
                case AND.type:
                    AND and = (AND) trigger;
                    if (!triggers.containsKey(getTriggerByName(and.getFirstTrigger())) || !triggers.containsKey(getTriggerByName(and.getSecondTrigger()))) {
                        return "Triggers do not exist";
                    }
                    break;
                case OR.type:
                    OR or = (OR) trigger;
                    if (!triggers.containsKey(getTriggerByName(or.getFirstTrigger())) || !triggers.containsKey(getTriggerByName(or.getSecondTrigger()))) {
                        return "Triggers do not exist";
                    }
                    break;
                case NOT.type:
                    NOT not = (NOT) trigger;
                    if (!triggers.containsKey(getTriggerByName(not.getTrigger()))) {
                        return "Trigger does not exist";
                    }
                    break;
            }
            if (triggerService.addTrigger(trigger, id)) {
                switch (trigger.getType()) {
                    case AND.type:
                        AND and = (AND) trigger;
                        addUsedTrigger(and.getFirstTrigger());
                        addUsedTrigger(and.getSecondTrigger());
                        break;
                    case OR.type:
                        OR or = (OR) trigger;
                        addUsedTrigger(or.getFirstTrigger());
                        addUsedTrigger(or.getSecondTrigger());
                        break;
                    case NOT.type:
                        NOT not = (NOT) trigger;
                        addUsedTrigger(not.getTrigger());
                        break;
                }
                this.triggers.put(trigger, 0);
                notifyObservers();
                return "Trigger added";
            }
            return "Trigger could not be added";
        }
        return "Trigger already exists";
    }

    public String deleteTrigger(String triggerName) {
        Triggers trigger = getTriggerByName(triggerName);
        if (triggers.containsKey(trigger) && trigger != null) {
            if(triggers.get(trigger) == 0){
                if (triggerService.deleteTrigger(triggerName)) {
                    switch (trigger.getType()) {
                        case AND.type:
                            AND and = (AND) trigger;
                            removeUsedTrigger(and.getFirstTrigger());
                            removeUsedTrigger(and.getSecondTrigger());
                            break;
                        case OR.type:
                            OR or = (OR) trigger;
                            removeUsedTrigger(or.getFirstTrigger());
                            removeUsedTrigger(or.getSecondTrigger());
                            break;
                        case NOT.type:
                            NOT not = (NOT) trigger;
                            removeUsedTrigger(not.getTrigger());
                            break;
                    }
                    triggers.remove(trigger);
                    notifyObservers();
                    return "Trigger removed";
                }
                return "Trigger could not be removed";
            }
            return "Trigger is used in a rule or in another trigger";
        }
        return "Trigger does not exist";
    }

    public String addAction(Actions action) {
        if (!containsAction(action)) {
            if (action.getType().equals(CombinedActions.type)) {
                CombinedActions combinedActions = (CombinedActions) action;
                if (!actions.containsKey(getActionByName(combinedActions.getFirstAction())) || !actions.containsKey(getActionByName(combinedActions.getSecondAction()))) {
                    return "Actions do not exist";
                }
            }
            if (actionService.addAction(action, id)) {
                if (action.getType().equals(CombinedActions.type)) {
                    CombinedActions combinedActions = (CombinedActions) action;
                    addUsedAction(combinedActions.getFirstAction());
                    addUsedAction(combinedActions.getSecondAction());
                }
                this.actions.put(action, 0);
                notifyObservers();
                return "Action added";
            }
            return "Action could not be added";
        }
        return "Action already exists";
    }

    public String deleteActions(String actionName) {
        Actions action = getActionByName(actionName);
        if (actions.containsKey(action) && action != null) {
            if(actions.get(action) == 0){
                if (actionService.deleteAction(actionName)) {
                    if (action.getType().equals(CombinedActions.type)) {
                        CombinedActions combinedActions = (CombinedActions) action;
                        removeUsedAction(combinedActions.getFirstAction());
                        removeUsedAction(combinedActions.getSecondAction());
                    }
                    actions.remove(action);
                    notifyObservers();
                    return "Action removed";
                }
                return "Action could not be removed";
            }
            return "Action is used in a rule or in another action";
        }
        return "Action does not exist";
    }

    public Triggers getTriggerByName(String triggerName){
        for (Triggers trigger : triggers.keySet()){
            if (trigger.getName().equals(triggerName)){
                return trigger;
            }
        }
        return null;
    }

    public Actions getActionByName(String actionName){
        for (Actions action : actions.keySet()){
            if (action.getName().equals(actionName)){
                return action;
            }
        }
        return null;
    }

    public JSONArray getRulesAsJson() {
        List<JSONObject> rulesJson = new ArrayList<>();
        for (Rule rule : rules) {
            rulesJson.add(rule.getJSONObject());
        }
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(rulesJson);
        return jsonArray;
    }

    public JSONArray getTriggersAsJson() {
        List<JSONObject> triggersJson = new ArrayList<>();
        for (Triggers trigger : triggers.keySet()) {
            triggersJson.add(trigger.getJSONObject());
        }
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(triggersJson);
        return jsonArray;
    }

    public JSONArray getActionsAsJson() {
        List<JSONObject> actionsJson = new ArrayList<>();
        for (Actions action : actions.keySet()) {
            actionsJson.add(action.getJSONObject());
        }
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(actionsJson);
        return jsonArray;
    }

    public boolean fileIsUsed(File file) {
        for (Triggers trigger : triggers.keySet()) {
            switch (trigger.getType()) {
                case FileExistence.type:
                    FileExistence fileExistence = (FileExistence) trigger;
                    if (fileExistence.getFile().equals(file)) {
                        return true;
                    }
                    break;
                case FileSize.type:
                    FileSize fileSize = (FileSize) trigger;
                    if (fileSize.getFile().equals(file)) {
                        return true;
                    }
                    break;
                case ExternalProgram.type:
                    ExternalProgram externalProgram = (ExternalProgram) trigger;
                    if (externalProgram.getExternalProgram().equals(file)) {
                        return true;
                    }
                    break;
            }
        }
        for (Actions action : actions.keySet()) {
            switch (action.getType()) {
                case AppendStringToFile.type:
                    AppendStringToFile fileAction = (AppendStringToFile) action;
                    if (fileAction.getFile().equals(file)) {
                        return true;
                    }
                    break;
                case DeleteFile.type:
                    DeleteFile deleteFile = (DeleteFile) action;
                    if (deleteFile.getFileToDelete().equals(file)) {
                        return true;
                    }
                    break;
                case MoveFile.type:
                    MoveFile moveFile = (MoveFile) action;
                    if (moveFile.getFileToMove().equals(file)) {
                        return true;
                    }
                    break;
                case PasteFile.type:
                    PasteFile pasteFile = (PasteFile) action;
                    if (pasteFile.getFileToPaste().equals(file)) {
                        return true;
                    }
                    break;
                case StartExternalProgram.type:
                    StartExternalProgram startExternalProgram = (StartExternalProgram) action;
                    if (startExternalProgram.getExternalProgram().equals(file)) {
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    public boolean directoryIsUsed(File directory) {
        for (Actions action : actions.keySet()) {
            switch (action.getType()) {
                case MoveFile.type:
                    MoveFile moveFile = (MoveFile) action;
                    if (moveFile.getFileToMove().equals(directory)) {
                        return true;
                    }
                    break;
                case PasteFile.type:
                    PasteFile pasteFile = (PasteFile) action;
                    if (pasteFile.getFileToPaste().equals(directory)) {
                        return true;
                    }
                    break;
            }
        }
        return false;
    }
}
