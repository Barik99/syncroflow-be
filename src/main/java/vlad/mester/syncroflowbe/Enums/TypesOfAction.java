package vlad.mester.syncroflowbe.Enums;

public enum TypesOfAction {
    DELETE_FILE("Delete File"),
    START_EXTERNAL_PROGRAM("Start External Program"),
    MOVE_FILE("Move File"),
    PASTE_FILE("Paste File"),
    COMBINED_ACTIONS("Combined Actions"),
    APPEND_STRING_TO_FILE("Append String To File");

    private final String actionType;

    TypesOfAction(String actionType) {
        this.actionType = actionType;
    }
}
