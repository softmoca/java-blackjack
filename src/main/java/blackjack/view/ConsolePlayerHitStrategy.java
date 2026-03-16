package blackjack.view;

import blackjack.domain.game.PlayerHitStrategy;
import blackjack.domain.participant.Player;

public class ConsolePlayerHitStrategy implements PlayerHitStrategy {
    private final InputView inputView;

    public ConsolePlayerHitStrategy(InputView inputView) {
        this.inputView = inputView;
    }

    @Override
    public boolean shouldHit(Player player) {
        return inputView.readHitAnswer(player.getName());
    }
}
