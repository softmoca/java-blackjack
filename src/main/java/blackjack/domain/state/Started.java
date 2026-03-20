package blackjack.domain.state;

import blackjack.domain.card.Hand;

public abstract class Started implements State {

    protected final Hand hand;

    protected Started(Hand hand) {
        this.hand = hand;
    }

    @Override
    public Hand hand() {
        return hand;
    }

    @Override
    public boolean isBust() {
        return hand.isBust();
    }

    @Override
    public boolean isSurrender() {
        return false;
    }

    @Override
    public boolean isBlackjack() {
        return hand.isBlackJack();
    }

    @Override
    public int score() {
        return hand.getTotalPoint();
    }
}
