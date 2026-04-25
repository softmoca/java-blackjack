package blackjack.application;

import blackjack.domain.participant.Dealer;
import blackjack.domain.participant.Players;

public class GameStartResult {

    private final Long gameId;
    private final Players players;
    private final Dealer dealer;

    public GameStartResult(Long gameId, Players players, Dealer dealer) {
        this.gameId = gameId;
        this.players = players;
        this.dealer = dealer;
    }

    public Long getGameId() {
        return gameId;
    }

    public Players getPlayers() {
        return players;
    }

    public Dealer getDealer() {
        return dealer;
    }
}
