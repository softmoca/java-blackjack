package blackjack.domain.state;

import blackjack.domain.card.Hand;

public class Bust extends Finished {

    public Bust(Hand hand) {
        super(hand);
    }
}
