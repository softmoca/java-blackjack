package blackjack.domain.state;

import blackjack.domain.card.Hand;

public class Surrender extends Finished {

    public Surrender(Hand hand) {
        super(hand);
    }

    @Override
    public boolean isSurrender() {
        return true;
    }
}
