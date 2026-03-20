package blackjack.domain.game;

public interface PlayerHitStrategy {
    PlayerAction chooseAction(String playerName);
}
