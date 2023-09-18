package by.zgrundo.braintests.models;

public enum ActionWithBalance {

    REPLENISHMENT("Пополнение"),
    WRITE_OFF("Списание");

    private final String actionValue;

    private ActionWithBalance(String actionValue) {
        this.actionValue = actionValue;
    }

    public String getActionValue() {
        return actionValue;
    }
}

