package blackjack.domain.state;

import blackjack.domain.card.Card;
import blackjack.domain.card.Hand;

public class StateFactory {

    public static State createInitialState
            (Card first, Card second) {
        Hand hand = new Hand();
        hand.addCard(first);
        hand.addCard(second);

        if (hand.isBlackJack()) {
            return new Blackjack(hand);
        }
        return new Hit(hand);
    }
}
