package blackjack.domain.game;

import blackjack.domain.participant.Dealer;

public interface DealerHitStrategy {
    boolean shouldHit(Dealer dealer);
}
