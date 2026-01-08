package game;

public enum ShotResult {
    PUDLO,
    TRAFIONY,
    ZATOPIONY,
    O_ZATOPIONY;

    public String toString() {
        return switch(this) {
            case PUDLO -> "pudło";
            case TRAFIONY -> "trafiony";
            case ZATOPIONY -> "trafiony zatopiony";
            case O_ZATOPIONY -> "ostatni zatopiony";
        };
    }

    public static ShotResult fromString(String s) {
        return switch (s) {
            case "pudło" -> PUDLO;
            case "trafiony" -> TRAFIONY;
            case "trafiony zatopiony" -> ZATOPIONY;
            case "ostatni zatopiony" -> O_ZATOPIONY;
            default -> throw new IllegalArgumentException("Nieznany wynik strzału: " + s);
        };
    }
}
