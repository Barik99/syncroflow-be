package vlad.mester.syncroflowbe.Enums;

public enum TypesOfTriggers {
    AND("AND"),
    NOT("NOT"),
    OR("OR"),
    DAY_OF_WEEK("Day Of Week"),
    DAY_OF_MONTH("Day Of Month"),
    TIME_OF_DAY("Time Of Day"),
    FILE_EXISTENCE("File Existence"),
    EXTERNAL_PROGRAM("External Program"),
    FILE_SIZE("File Size");

    private final String triggerType;

    TypesOfTriggers(String triggerType) {
        this.triggerType = triggerType;
    }
}
