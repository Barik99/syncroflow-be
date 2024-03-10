package vlad.mester.syncroflowbe.base;


import javax.persistence.Entity;

@Entity
public class ConcreteActions extends Actions {
    public ConcreteActions() {
        super(null, null, null);
    }

    public ConcreteActions(String name, String type, String value) {
        super(name, type, value);
    }

    @Override
    public boolean execute() {
        // Implement this method
        return false;
    }
}