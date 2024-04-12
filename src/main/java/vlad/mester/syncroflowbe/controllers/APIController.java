package vlad.mester.syncroflowbe.controllers;

import org.json.simple.JSONArray;
import org.springframework.web.bind.annotation.*;
import vlad.mester.syncroflowbe.RuleController;
import vlad.mester.syncroflowbe.Scheduler;
import vlad.mester.syncroflowbe.base.Actions;
import vlad.mester.syncroflowbe.base.Rule;
import vlad.mester.syncroflowbe.base.Triggers;
import vlad.mester.syncroflowbe.services.LoadRuleControllerService;
import vlad.mester.syncroflowbe.services.LoginService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class APIController {

    private Map<String, Scheduler> schedulers = new HashMap<>();

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/rules/{email}")
    public JSONArray getRulesByEmail(@PathVariable String email) {
        RuleController ruleController = RuleController.getInstance(email);
        return ruleController.getRulesAsJson();
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/actions/{email}")
    public JSONArray getActionsByEmail(@PathVariable String email) {
        RuleController ruleController = RuleController.getInstance(email);
        return ruleController.getActionsAsJson();
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/triggers/{email}")
    public JSONArray getTriggersByEmail(@PathVariable String email) {
        RuleController ruleController = RuleController.getInstance(email);
        return ruleController.getTriggersAsJson();
    }

    @PostMapping("/addRule/{email}")
    public String addRule(@PathVariable String email, @RequestBody String rule) {
        RuleController ruleController = RuleController.getInstance(email);
        return ruleController.addRule(Rule.fromJSONObject(rule));
    }

    @PostMapping("/addTrigger/{email}")
    public String addTrigger(@PathVariable String email, @RequestBody String trigger) throws IOException {
        RuleController ruleController = RuleController.getInstance(email);
        return ruleController.addTrigger(Triggers.fromJSONObject(trigger, email));
    }

    @PostMapping("/addAction/{email}")
    public String addAction(@PathVariable String email, @RequestBody String action) throws IOException {
        RuleController ruleController = RuleController.getInstance(email);
        return ruleController.addAction(Actions.fromJSONObject(action, email));
    }

    @DeleteMapping("/removeRule/{email}/{name}")
    public String removeRule(@PathVariable String email, @PathVariable String name) {
        RuleController ruleController = RuleController.getInstance(email);
        return ruleController.deleteRule(name);
    }

    @DeleteMapping("/removeTrigger/{email}/{name}")
    public String removeTrigger(@PathVariable String email, @PathVariable String name) {
        RuleController ruleController = RuleController.getInstance(email);
        return ruleController.deleteTrigger(name);
    }

    @DeleteMapping("/removeAction/{email}/{name}")
    public String removeAction(@PathVariable String email, @PathVariable String name) {
        RuleController ruleController = RuleController.getInstance(email);
        return ruleController.deleteActions(name);
    }

    @PostMapping("/schedulerStart/{email}/{interval}")
    public String startScheduler(@PathVariable String email, @PathVariable int interval) {
        Scheduler scheduler = new Scheduler(interval, email);
        scheduler.start();
        schedulers.put(email, scheduler);
        return "Scheduler started";
    }

    @PostMapping("/schedulerStop/{email}")
    public String stopScheduler(@PathVariable String email) {
        schedulers.get(email).stop();
        schedulers.remove(email);
        return "Scheduler stopped";
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/register/{email}/{password}")
    public boolean register(@PathVariable String email, @PathVariable String password) {
        return LoginService.register(email, password);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/login/{email}/{password}")
    public boolean login(@PathVariable String email, @PathVariable String password) {
        if(LoginService.login(email, password)) {
            LoadRuleControllerService loadRuleControllerService = new LoadRuleControllerService(email);
            loadRuleControllerService.load();
            return true;
        }else {
            return false;
        }
    }
}