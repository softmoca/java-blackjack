package blackjack.application;

public class DealerHitResult {

    private final boolean hit;

    public DealerHitResult(boolean hit) {
        this.hit = hit;
    }

    public boolean isHit() {
        return hit;
    }
}
