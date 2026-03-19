package blackjack.view;

import blackjack.domain.game.PlayerHitStrategy;

public class ConsolePlayerHitStrategy implements PlayerHitStrategy {
    private final InputView inputView;

    public ConsolePlayerHitStrategy(InputView inputView) {
        this.inputView = inputView;
    }

    @Override
    public boolean shouldHit(String playerName) {
        return inputView.readHitAnswer(playerName);
    }
}
