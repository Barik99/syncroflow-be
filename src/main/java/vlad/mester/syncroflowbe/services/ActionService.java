package vlad.mester.syncroflowbe.services;

import org.springframework.stereotype.Service;
import vlad.mester.syncroflowbe.base.ConcreteActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ActionService {

    List<ConcreteActions> actions = new ArrayList<>(Arrays.asList(
            new ConcreteActions("action1", "type1", "value1") {
                @Override
                public boolean execute() {
                    return false;
                }
            },
            new ConcreteActions("action2", "type2", "value2") {
                @Override
                public boolean execute() {
                    return false;
                }
            },
            new ConcreteActions("action3", "type3", "value3") {
                @Override
                public boolean execute() {
                    return false;
                }
            }));

    public List<ConcreteActions> getAllActions(){
        return actions;
    }

    public ConcreteActions getActionByName(String name){
        return actions.stream().filter(actions -> actions.getName().equals(name)).findFirst().get();
    }

    public void addAction(ConcreteActions action){
        actions.add(action);
    }

    public void updateAction(ConcreteActions action, String name){
        for(int i = 0; i < actions.size(); i++){
            ConcreteActions a = actions.get(i);
            if(a.getName().equals(name)){
                actions.set(i, action);
                return;
            }
        }
    }

    public void deleteAction(String name){
        actions.removeIf(actions -> actions.getName().equals(name));
    }
}
