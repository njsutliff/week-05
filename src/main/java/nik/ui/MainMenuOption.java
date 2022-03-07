package nik.ui;

public enum MainMenuOption {

    VIEW_RESERVATIONS(1, "View Reservations for Host", false),
    MAKE_RESERVATION(2,"View Foragers By State",false),
    EDIT_RESERVATION(3, "View Items", false),
    CANCEL_RESERVATION(4, "Add a Forage", false),
    EXIT(5, "Exit", false);


    private int value;
    private String message;
    private boolean hidden;

    private MainMenuOption(int value, String message, boolean hidden) {
        this.value = value;
        this.message = message;
        this.hidden = hidden;
    }

    public static MainMenuOption fromValue(int value) {
        for (MainMenuOption option : MainMenuOption.values()) {
            if (option.getValue() == value) {
                return option;
            }
        }
        return EXIT;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public boolean isHidden() {
        return hidden;
    }
}
