package blackjack.domain.participant;

import blackjack.domain.card.Card;

public class Player extends Participant {

    private final BetAmount betAmount;

    public Player(Name name, BetAmount betAmount) {
        super(name);
        this.betAmount = betAmount;
    }

    @Override
    public void recieveCard(Card card) {
        if (isBust()) {
            throw new IllegalStateException(getName() + "는 더 이상 카드를 받을 수 없습니다.");
        }

        addCard(card);
    }

    @Override
    public boolean shouldDraw() {
        return !isBust();
    }

    public int getBetAmount() {
        return betAmount.getAmount();
    }

}
