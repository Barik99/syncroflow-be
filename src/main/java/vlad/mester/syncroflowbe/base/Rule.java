package vlad.mester.syncroflowbe.base;

import lombok.Data;

import java.util.Date;

@Data
public class Rule {
    private String name;
    private Triggers trigger;
    private Actions action;
    private boolean multiUse;
    private Date lastUse;
    private int sleepTime;
    private boolean active;

    public Rule(String name, Triggers trigger, Actions action, boolean active, boolean multiUse, int sleepTime) {
        this.name = name;
        this.trigger = trigger;
        this.action = action;
        this.active = active;
        this.multiUse = multiUse;
        this.lastUse = null;
        this.sleepTime = sleepTime;
    }

    public Rule(String name, Triggers trigger, Actions action, boolean active, boolean multiUse, Date lastUse, int sleepTime) {
        this.name = name;
        this.trigger = trigger;
        this.action = action;
        this.active = active;
        this.multiUse = multiUse;
        this.lastUse = lastUse;
        this.sleepTime = sleepTime;
    }

    public void setLastUse() {
        this.lastUse = new Date();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Rule rule) {
            return this.name.equals(rule.name);
        }
        return false;
    }
}
