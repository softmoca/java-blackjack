package blackjack.domain.participant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import blackjack.domain.card.Card;
import blackjack.domain.card.CardPattern;
import blackjack.domain.card.CardPoint;
import blackjack.domain.state.Blackjack;
import blackjack.domain.state.Bust;
import blackjack.domain.state.Hit;
import blackjack.domain.state.Stay;
import org.junit.jupiter.api.Test;

class PlayerTest {

    private static final BetAmount DEFAULT_BET = new BetAmount(1000);

    @Test
    void 초기_두_장을_받으면_카드가_2장이다() {
        Player player = new Player(new Name("pobi"), DEFAULT_BET);

        player.initDeal(
                new Card(CardPoint.TWO, CardPattern.SPADE),
                new Card(CardPoint.THREE, CardPattern.HEART)
        );

        assertThat(player.getCardCount()).isEqualTo(2);
        assertThat(player.getTotalPoint()).isEqualTo(5);
    }

    @Test
    void 초기_두_장이_블랙잭이면_Finished_상태다() {
        Player player = new Player(new Name("pobi"), DEFAULT_BET);

        player.initDeal(
                new Card(CardPoint.ACE, CardPattern.SPADE),
                new Card(CardPoint.KING, CardPattern.HEART)
        );

        assertThat(player.isBlackjack()).isTrue();
        assertThat(player.isFinished()).isTrue();
        assertThat(player.getState()).isInstanceOf(Blackjack.class);
    }

    @Test
    void 카드를_추가로_뽑을_수_있다() {
        Player player = new Player(new Name("pobi"), DEFAULT_BET);
        player.initDeal(
                new Card(CardPoint.TWO, CardPattern.SPADE),
                new Card(CardPoint.THREE, CardPattern.HEART)
        );

        player.draw(new Card(CardPoint.FOUR, CardPattern.CLUB));

        assertThat(player.getCardCount()).isEqualTo(3);
        assertThat(player.getTotalPoint()).isEqualTo(9);
        assertThat(player.getState()).isInstanceOf(Hit.class);
    }

    @Test
    void 카드를_뽑아서_버스트되면_Bust_상태다() {
        Player player = new Player(new Name("pobi"), DEFAULT_BET);
        player.initDeal(
                new Card(CardPoint.QUEEN, CardPattern.SPADE),
                new Card(CardPoint.KING, CardPattern.HEART)
        );

        player.draw(new Card(CardPoint.FIVE, CardPattern.CLUB));

        assertThat(player.isBust()).isTrue();
        assertThat(player.isFinished()).isTrue();
        assertThat(player.getState()).isInstanceOf(Bust.class);
    }

    @Test
    void 스테이를_선택하면_Stay_상태다() {
        Player player = new Player(new Name("pobi"), DEFAULT_BET);
        player.initDeal(
                new Card(CardPoint.QUEEN, CardPattern.SPADE),
                new Card(CardPoint.EIGHT, CardPattern.HEART)
        );

        player.stay();

        assertThat(player.isFinished()).isTrue();
        assertThat(player.getState()).isInstanceOf(Stay.class);
        assertThat(player.getTotalPoint()).isEqualTo(18);
    }

    @Test
    void 버스트_상태에서_카드를_뽑으면_예외가_발생한다() {
        Player player = new Player(new Name("pobi"), DEFAULT_BET);
        player.initDeal(
                new Card(CardPoint.QUEEN, CardPattern.SPADE),
                new Card(CardPoint.KING, CardPattern.HEART)
        );
        player.draw(new Card(CardPoint.FIVE, CardPattern.CLUB));

        assertThatThrownBy(() -> player.draw(new Card(CardPoint.TWO, CardPattern.DIAMOND)))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
