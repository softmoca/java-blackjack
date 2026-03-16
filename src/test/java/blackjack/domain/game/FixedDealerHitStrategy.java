package blackjack.domain.game;

import blackjack.domain.participant.Dealer;

public class FixedDealerHitStrategy implements DealerHitStrategy {
    private final boolean answer;

    public FixedDealerHitStrategy(boolean answer) {
        this.answer = answer;
    }

    @Override
    public boolean shouldHit(Dealer dealer) {
        return answer;
    }
}
