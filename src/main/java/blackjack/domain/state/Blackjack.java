package blackjack.domain.state;

import blackjack.domain.card.Hand;

public class Blackjack extends Finished {

    public Blackjack(Hand hand) {
        super(hand);
    }
}
