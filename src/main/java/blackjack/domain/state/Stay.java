package blackjack.domain.state;

import blackjack.domain.card.Hand;

public class Stay extends Finished {

    public Stay(Hand hand) {
        super(hand);
    }
}
