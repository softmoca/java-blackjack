package blackjack.application;

import blackjack.domain.participant.Player;

public class TurnState {

    private final boolean allPlayersFinished;
    private final Player currentPlayer;

    private TurnState(boolean allPlayersFinished, Player currentPlayer) {
        this.allPlayersFinished = allPlayersFinished;
        this.currentPlayer = currentPlayer;
    }

    public static TurnState allFinished() {
        return new TurnState(true, null);
    }

    public static TurnState of(Player currentPlayer) {
        return new TurnState(false, currentPlayer);
    }

    public boolean isAllPlayersFinished() {
        return allPlayersFinished;
    }

    public Player getCurrentPlayer() {
        if (allPlayersFinished) {
            throw new IllegalStateException("모든 플레이어의 턴이 끝났습니다.");
        }
        return currentPlayer;
    }

    public boolean isTurnEndedFor(Player player) {
        if (allPlayersFinished) {
            return true;
        }
        return !currentPlayer.equals(player);
    }
}
