package blackjack.domain.game;

import blackjack.domain.participant.Player;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FixedPlayerHitStrategy implements PlayerHitStrategy {
    private final Queue<Boolean> answers;

    public FixedPlayerHitStrategy(Boolean... answers) {
        this.answers = new LinkedList<>(List.of(answers));
    }

    @Override
    public boolean shouldHit(Player player) {
        // 준비된 답변이 없으면 false (더 이상 안 받겠다)
        if (answers.isEmpty()) {
            return false;
        }
        return answers.poll();
    }
}
