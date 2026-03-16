package blackjack.domain.game;

import blackjack.domain.participant.Player;

public interface PlayerHitStrategy {
    boolean shouldHit(Player player);
}
