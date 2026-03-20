package blackjack.view;

import blackjack.domain.game.PlayerAction;
import blackjack.domain.game.PlayerHitStrategy;

public class ConsolePlayerHitStrategy implements PlayerHitStrategy {
    private final InputView inputView;

    public ConsolePlayerHitStrategy(InputView inputView) {
        this.inputView = inputView;
    }

    @Override
    public PlayerAction chooseAction(String playerName) {
        return inputView.readPlayerAction(playerName);
    }
}
