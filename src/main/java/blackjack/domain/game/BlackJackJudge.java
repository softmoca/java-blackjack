package blackjack.domain.game;

import blackjack.domain.state.State;

public class BlackJackJudge {

    public GameResult judge(State playerState, State dealerState) {
        if (playerState.isBust()) {
            return GameResult.LOSE;
        }
        if (dealerState.isBust()) {
            return GameResult.WIN;
        }
        if (playerState.isBlackjack() && dealerState.isBlackjack()) {
            return GameResult.TIE;
        }
        if (playerState.isBlackjack()) {
            return GameResult.BLACKJACK_WIN;
        }
        return compareScore(playerState, dealerState);
    }

    private GameResult compareScore(State playerState, State dealerState) {
        if (playerState.score() > dealerState.score()) {
            return GameResult.WIN;
        }
        if (playerState.score() == dealerState.score()) {
            return GameResult.TIE;
        }
        return GameResult.LOSE;
    }
}
