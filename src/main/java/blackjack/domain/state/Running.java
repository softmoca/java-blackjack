package blackjack.domain.state;

import blackjack.domain.card.Hand;

public abstract class Running extends Started {

    protected Running(Hand hand) {
        super(hand);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
