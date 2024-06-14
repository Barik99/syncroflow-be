package vlad.mester.syncroflowbe.controllers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vlad.mester.syncroflowbe.requests.AddDirectoryRequest;
import vlad.mester.syncroflowbe.FileController;
import vlad.mester.syncroflowbe.RuleController;
import vlad.mester.syncroflowbe.Scheduler;
import vlad.mester.syncroflowbe.base.Actions;
import vlad.mester.syncroflowbe.base.Rule;
import vlad.mester.syncroflowbe.base.Triggers;
import vlad.mester.syncroflowbe.requests.RemoveDirectoryRequest;
import vlad.mester.syncroflowbe.requests.RemoveFileRequest;
import vlad.mester.syncroflowbe.services.LoginService;
import xyz.capybara.clamav.ClamavClient;
import xyz.capybara.clamav.commands.scan.result.ScanResult;

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

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/addRule/{email}")
    public String addRule(@PathVariable String email, @RequestBody String rule) {
        RuleController ruleController = RuleController.getInstance(email);
        return ruleController.addRule(Rule.fromJSONObject(rule));
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/addTrigger/{email}")
    public String addTrigger(@PathVariable String email, @RequestBody String trigger) throws IOException {
        RuleController ruleController = RuleController.getInstance(email);
        return ruleController.addTrigger(Triggers.fromJSONObject(trigger, email));
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/addAction/{email}")
    public String addAction(@PathVariable String email, @RequestBody String action) throws IOException {
        RuleController ruleController = RuleController.getInstance(email);
        return ruleController.addAction(Actions.fromJSONObject(action, email));
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @DeleteMapping("/removeRule/{email}/{name}")
    public String removeRule(@PathVariable String email, @PathVariable String name) {
        RuleController ruleController = RuleController.getInstance(email);
        return ruleController.deleteRule(name);
    }

    @CrossOrigin(origins = "http://localhost:5173")
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

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/schedulerStart/{email}/{interval}")
    public String startScheduler(@PathVariable String email, @PathVariable int interval) {
        if (schedulers.containsKey(email))
            return "Planificatorul deja a fost pornit.";
        Scheduler scheduler = new Scheduler(interval, email);
        scheduler.start();
        schedulers.put(email, scheduler);
        return "Planificator a pornit cu succes!";
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/schedulerStop/{email}")
    public String stopScheduler(@PathVariable String email) {
        if (!schedulers.containsKey(email))
            return "Planificatorul nu a fost pornit.";
        schedulers.get(email).stop();
        schedulers.remove(email);
        return "Planificator a fost oprit cu succes!";
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/register/{email}/{password}")
    public String register(@PathVariable String email, @PathVariable String password) {
        return LoginService.register(email, password);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/login/{email}/{password}")
    public boolean login(@PathVariable String email, @PathVariable String password) {
        return LoginService.login(email, password);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/triggerTypes")
    public String getTriggerTypes() {
        return Triggers.getAllTriggerTypes();
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/actionTypes")
    public String getActionTypes() {
        return Actions.getAllActionTypes();
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/addFile")
    public String addFile(@RequestParam("file") MultipartFile file, @RequestParam("path") String path) {
        FileController fileController = new FileController();
        try {
            //creaza un client pentru clamav
            ClamavClient clamavClient = new ClamavClient("localhost", 3310);

            //scanare fisier
            ScanResult scanResult = clamavClient.scan(file.getInputStream());

            //verifica rezultatul scanarii
            if (scanResult.getStatus() == ScanResult.Status.OK) {
                return fileController.addFile(path, file);
            } else if (scanResult.getStatus() == ScanResult.Status.VIRUS_FOUND) {
                return "Fișierul conține un virus";
            } else {
                return "Nu s-a putut scana fișierul";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Fișierul nu a putut fi adăugat";
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @DeleteMapping("/removeFile")
    public String removeFile(@RequestBody RemoveFileRequest file) {
        FileController fileController = new FileController();
        return fileController.removeFile(file.getFile());
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/addDirectory")
    public String addDirectory(@RequestBody AddDirectoryRequest addDirectoryRequest) {
        FileController fileController = new FileController();
        return fileController.addDirectory(addDirectoryRequest.getParentDirectory(), addDirectoryRequest.getDirectory());
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @DeleteMapping("/removeDirectory")
    public String removeDirectory(@RequestBody RemoveDirectoryRequest removeDirectoryRequest) {
        FileController fileController = new FileController();
        return fileController.removeDirectory(removeDirectoryRequest.getDirectory());
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/getDirectory")
    public JSONObject getDirectory() {
        FileController fileController = new FileController();
        return fileController.getDirectoryStructure(FileController.root.getPath());
    }
}