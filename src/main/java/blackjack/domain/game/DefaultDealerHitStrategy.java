package blackjack.domain.game;

import blackjack.domain.participant.Dealer;

public class DefaultDealerHitStrategy implements DealerHitStrategy {

    @Override
    public boolean shouldHit(Dealer dealer) {
        return dealer.shouldDraw();
    }
}
