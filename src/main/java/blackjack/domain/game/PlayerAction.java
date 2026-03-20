package blackjack.domain.game;

public enum PlayerAction {
    HIT("y"),
    STAY("n"),
    SURRENDER("s");

    private final String command;

    PlayerAction(String command) {
        this.command = command;
    }

    public static PlayerAction from(String input) {
        for (PlayerAction action : values()) {
            if (action.command.equals(input)) {
                return action;
            }
        }
        throw new IllegalArgumentException("[ERROR] y, n, s 중 하나를 입력해주세요.");
    }
}
