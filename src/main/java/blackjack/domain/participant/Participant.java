package blackjack.domain.participant;

import blackjack.domain.card.Card;
import blackjack.domain.state.State;
import blackjack.domain.state.StateFactory;

public abstract class Participant {

    private final Name name;
    private State state;

    protected Participant(Name name) {
        this.name = name;
    }

    public void initDeal(Card first, Card second) {
        this.state = StateFactory.createInitialState(first, second);
    }

    public void draw(Card card) {
        this.state = state.draw(card);
    }

    public void stay() {
        this.state = state.stay();
    }

    public void surrender() {
        this.state = state.surrender();
    }

    public boolean isFinished() {
        return state.isFinished();
    }

    public boolean isBust() {
        return state.isBust();
    }

    public boolean isBlackjack() {
        return state.isBlackjack();
    }

    public boolean isSurrender() {
        return state.isSurrender();
    }

    public int getTotalPoint() {
        return state.score();
    }

    public String getName() {
        return name.getValue();
    }

    public State getState() {
        return state;
    }

    public String getCardNames() {
        return state.hand().getCardNames();
    }

    public String getFirstCardName() {
        return state.hand().getFirstCardName();
    }

    public int getCardCount() {
        return state.hand().getCount();
    }
}
