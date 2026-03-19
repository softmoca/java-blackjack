package blackjack.domain.game;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FixedPlayerHitStrategy implements PlayerHitStrategy {
    private final Queue<Boolean> answers;

    public FixedPlayerHitStrategy(Boolean... answers) {
        this.answers = new LinkedList<>(List.of(answers));
    }

    @Override
    public boolean shouldHit(String playerName) {
        if (answers.isEmpty()) {
            return false;
        }
        return answers.poll();
    }
}
