package blackjack.domain.game;

public interface PlayerHitStrategy {
    boolean shouldHit(String playerName);
}
