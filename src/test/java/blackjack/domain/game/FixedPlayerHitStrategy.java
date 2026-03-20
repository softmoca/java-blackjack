package blackjack.domain.game;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FixedPlayerHitStrategy implements PlayerHitStrategy {
    private final Queue<PlayerAction> actions;

    public FixedPlayerHitStrategy(PlayerAction... actions) {
        this.actions = new LinkedList<>(List.of(actions));
    }

    @Override
    public PlayerAction chooseAction(String playerName) {
        if (actions.isEmpty()) {
            return PlayerAction.STAY;
        }
        return actions.poll();
    }
}
