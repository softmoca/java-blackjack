package blackjack.domain.state;

import static org.assertj.core.api.Assertions.assertThat;

import blackjack.domain.card.Card;
import blackjack.domain.card.CardPattern;
import blackjack.domain.card.CardPoint;
import org.junit.jupiter.api.Test;

class HitTest {

    @Test
    void 카드를_뽑아도_버스트가_아니면_히트_상태를_유지한다() {
        State state = StateFactory.createInitialState(
                new Card(CardPoint.TWO, CardPattern.SPADE),
                new Card(CardPoint.THREE, CardPattern.HEART)
        );

        State next = state.draw(new Card(CardPoint.FOUR, CardPattern.CLUB));

        assertThat(next).isInstanceOf(Hit.class);
        assertThat(next.isFinished()).isFalse();
        assertThat(next.score()).isEqualTo(9);
    }

    @Test
    void 카드를_뽑아서_버스트되면_버스트_상태가_된다() {
        State state = StateFactory.createInitialState(
                new Card(CardPoint.QUEEN, CardPattern.SPADE),
                new Card(CardPoint.KING, CardPattern.HEART)
        );

        State next = state.draw(new Card(CardPoint.FIVE, CardPattern.CLUB));

        assertThat(next).isInstanceOf(Bust.class);
        assertThat(next.isFinished()).isTrue();
        assertThat(next.isBust()).isTrue();
    }

    @Test
    void 스테이를_선택하면_스테이_상태가_된다() {
        State state = StateFactory.createInitialState(
                new Card(CardPoint.QUEEN, CardPattern.SPADE),
                new Card(CardPoint.EIGHT, CardPattern.HEART)
        );

        State next = state.stay();

        assertThat(next).isInstanceOf(Stay.class);
        assertThat(next.isFinished()).isTrue();
        assertThat(next.score()).isEqualTo(18);
    }
}
