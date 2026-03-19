package blackjack.domain.participant;

public class Dealer extends Participant {

    private static final int DEALER_STAND_POINT = 17;
    private static final String DEALER_NAME = "딜러";

    public Dealer() {
        super(new Name(DEALER_NAME));
    }

    public boolean shouldDraw() {
        return !isFinished() && getTotalPoint() < DEALER_STAND_POINT;
    }

    public String getFirstCardNames() {
        return getFirstCardName();
    }
}
