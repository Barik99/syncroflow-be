package vlad.mester.syncroflowbe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vlad.mester.syncroflowbe.base.ConcreteActions;
import vlad.mester.syncroflowbe.services.ActionService;

import java.util.List;

@RestController
public class ActionsController {

    @Autowired
    private ActionService actionsService;

    @GetMapping("/actions")
    public List<ConcreteActions> getAllActions(){
        return actionsService.getAllActions();
    }

    @GetMapping("/actions/{name}")
    public ConcreteActions getActionByName(@PathVariable String name){
        return actionsService.getActionByName(name);
    }

    @PostMapping("/actions")
    public void addAction(@RequestBody ConcreteActions action){
        actionsService.addAction(action);
    }

    @PutMapping("/actions/{name}")
    public void updateAction(@RequestBody ConcreteActions action, @PathVariable String name){
        actionsService.updateAction(action, name);
    }

    @DeleteMapping("/actions/{name}")
    public void deleteAction(@PathVariable String name){
        actionsService.deleteAction(name);
    }
}
