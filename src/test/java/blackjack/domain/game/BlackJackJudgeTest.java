package blackjack.domain.game;

import static org.assertj.core.api.Assertions.assertThat;

import blackjack.domain.card.Card;
import blackjack.domain.card.CardPattern;
import blackjack.domain.card.CardPoint;
import blackjack.domain.state.State;
import blackjack.domain.state.StateFactory;
import org.junit.jupiter.api.Test;

class BlackJackJudgeTest {

    private final BlackJackJudge judge = new BlackJackJudge();

    @Test
    void 플레이어가_버스트이면_패배다() {
        State playerState = StateFactory.createInitialState(
                new Card(CardPoint.QUEEN, CardPattern.SPADE),
                new Card(CardPoint.KING, CardPattern.HEART)
        );
        playerState = playerState.draw(new Card(CardPoint.FIVE, CardPattern.CLUB));

        State dealerState = StateFactory.createInitialState(
                new Card(CardPoint.TEN, CardPattern.DIAMOND),
                new Card(CardPoint.NINE, CardPattern.CLUB)
        );
        dealerState = dealerState.stay();

        assertThat(judge.judge(playerState, dealerState)).isEqualTo(GameResult.LOSE);
    }

    @Test
    void 딜러가_버스트이면_플레이어_승리다() {
        State playerState = StateFactory.createInitialState(
                new Card(CardPoint.TEN, CardPattern.SPADE),
                new Card(CardPoint.NINE, CardPattern.HEART)
        );
        playerState = playerState.stay();

        State dealerState = StateFactory.createInitialState(
                new Card(CardPoint.QUEEN, CardPattern.CLUB),
                new Card(CardPoint.KING, CardPattern.DIAMOND)
        );
        dealerState = dealerState.draw(new Card(CardPoint.TWO, CardPattern.HEART));

        assertThat(judge.judge(playerState, dealerState)).isEqualTo(GameResult.WIN);
    }

    @Test
    void 플레이어와_딜러_모두_블랙잭이면_무승부다() {
        State playerState = StateFactory.createInitialState(
                new Card(CardPoint.ACE, CardPattern.SPADE),
                new Card(CardPoint.KING, CardPattern.HEART)
        );

        State dealerState = StateFactory.createInitialState(
                new Card(CardPoint.ACE, CardPattern.CLUB),
                new Card(CardPoint.KING, CardPattern.DIAMOND)
        );

        assertThat(judge.judge(playerState, dealerState)).isEqualTo(GameResult.TIE);
    }

    @Test
    void 플레이어만_블랙잭이면_블랙잭_승리다() {
        State playerState = StateFactory.createInitialState(
                new Card(CardPoint.ACE, CardPattern.SPADE),
                new Card(CardPoint.KING, CardPattern.HEART)
        );

        State dealerState = StateFactory.createInitialState(
                new Card(CardPoint.TEN, CardPattern.CLUB),
                new Card(CardPoint.NINE, CardPattern.DIAMOND)
        );
        dealerState = dealerState.stay();

        assertThat(judge.judge(playerState, dealerState)).isEqualTo(GameResult.BLACKJACK_WIN);
    }

    @Test
    void 플레이어_점수가_딜러보다_높으면_승리다() {
        State playerState = StateFactory.createInitialState(
                new Card(CardPoint.TEN, CardPattern.SPADE),
                new Card(CardPoint.NINE, CardPattern.HEART)
        );
        playerState = playerState.stay();

        State dealerState = StateFactory.createInitialState(
                new Card(CardPoint.TEN, CardPattern.CLUB),
                new Card(CardPoint.EIGHT, CardPattern.DIAMOND)
        );
        dealerState = dealerState.stay();

        assertThat(judge.judge(playerState, dealerState)).isEqualTo(GameResult.WIN);
    }

    @Test
    void 플레이어_점수가_딜러보다_낮으면_패배다() {
        State playerState = StateFactory.createInitialState(
                new Card(CardPoint.TEN, CardPattern.SPADE),
                new Card(CardPoint.SEVEN, CardPattern.HEART)
        );
        playerState = playerState.stay();

        State dealerState = StateFactory.createInitialState(
                new Card(CardPoint.TEN, CardPattern.CLUB),
                new Card(CardPoint.NINE, CardPattern.DIAMOND)
        );
        dealerState = dealerState.stay();

        assertThat(judge.judge(playerState, dealerState)).isEqualTo(GameResult.LOSE);
    }

    @Test
    void 동점이면_무승부다() {
        State playerState = StateFactory.createInitialState(
                new Card(CardPoint.TEN, CardPattern.SPADE),
                new Card(CardPoint.NINE, CardPattern.HEART)
        );
        playerState = playerState.stay();

        State dealerState = StateFactory.createInitialState(
                new Card(CardPoint.TEN, CardPattern.CLUB),
                new Card(CardPoint.NINE, CardPattern.DIAMOND)
        );
        dealerState = dealerState.stay();

        assertThat(judge.judge(playerState, dealerState)).isEqualTo(GameResult.TIE);
    }

    @Test
    void 블랙잭_승리에_1000원_베팅하면_수익은_1500원이다() {
        GameResult result = GameResult.BLACKJACK_WIN;

        assertThat(result.calculateIncome(1000)).isEqualTo(1500);
    }

    @Test
    void 세_장으로_21이면_블랙잭이_아니라_일반_승리다() {
        State playerState = StateFactory.createInitialState(
                new Card(CardPoint.SEVEN, CardPattern.SPADE),
                new Card(CardPoint.SEVEN, CardPattern.HEART)
        );
        playerState = playerState.draw(new Card(CardPoint.SEVEN, CardPattern.CLUB));
        playerState = playerState.stay();

        State dealerState = StateFactory.createInitialState(
                new Card(CardPoint.TEN, CardPattern.DIAMOND),
                new Card(CardPoint.NINE, CardPattern.CLUB)
        );
        dealerState = dealerState.stay();

        assertThat(judge.judge(playerState, dealerState)).isEqualTo(GameResult.WIN);
    }
}
