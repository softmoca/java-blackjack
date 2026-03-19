package blackjack.domain.participant;

public class Player extends Participant {

    private final BetAmount betAmount;

    public Player(Name name, BetAmount betAmount) {
        super(name);
        this.betAmount = betAmount;
    }

    public int getBetAmount() {
        return betAmount.getAmount();
    }
}
