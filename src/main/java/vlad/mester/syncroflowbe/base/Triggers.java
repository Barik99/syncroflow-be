package vlad.mester.syncroflowbe.base;

import lombok.Data;

import java.io.IOException;

@Data
public abstract class Triggers {
    private String name;
    private String type;
    private String value;

    public Triggers(String name, String type, String value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString(){return name;}

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Triggers triggers) {
            return this.name.equals(triggers.name);
        }
        return false;
    }

    public abstract boolean evaluate();
}
