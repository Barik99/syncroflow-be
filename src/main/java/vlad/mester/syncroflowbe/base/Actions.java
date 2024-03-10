package vlad.mester.syncroflowbe.base;

import lombok.Data;


@Data
public abstract class Actions {

    private String name;
    private String type;
    private String value;

    public Actions(String name, String type, String value){
        this.name = name;
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object object){
        if(object instanceof Actions actions){
            return this.name.equals(actions.name);
        }
        return false;
    }

    public abstract boolean execute();

    public boolean isActionUsedIn(){
        boolean result = false;
        return result;
    }
}
