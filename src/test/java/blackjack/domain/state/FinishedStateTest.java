package blackjack.domain.state;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import blackjack.domain.card.Card;
import blackjack.domain.card.CardPattern;
import blackjack.domain.card.CardPoint;
import org.junit.jupiter.api.Test;

class FinishedStateTest {

    @Test
    void 블랙잭_상태에서_카드를_뽑으면_예외가_발생한다() {
        State blackjack = StateFactory.createInitialState(
                new Card(CardPoint.ACE, CardPattern.SPADE),
                new Card(CardPoint.KING, CardPattern.HEART)
        );

        assertThatThrownBy(() -> blackjack.draw(new Card(CardPoint.TWO, CardPattern.CLUB)))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void 블랙잭_상태에서_스테이하면_예외가_발생한다() {
        State blackjack = StateFactory.createInitialState(
                new Card(CardPoint.ACE, CardPattern.SPADE),
                new Card(CardPoint.KING, CardPattern.HEART)
        );

        assertThatThrownBy(blackjack::stay)
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void 버스트_상태에서_카드를_뽑으면_예외가_발생한다() {
        State hit = StateFactory.createInitialState(
                new Card(CardPoint.QUEEN, CardPattern.SPADE),
                new Card(CardPoint.KING, CardPattern.HEART)
        );
        State bust = hit.draw(new Card(CardPoint.FIVE, CardPattern.CLUB));

        assertThatThrownBy(() -> bust.draw(new Card(CardPoint.TWO, CardPattern.DIAMOND)))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void 스테이_상태에서_카드를_뽑으면_예외가_발생한다() {
        State hit = StateFactory.createInitialState(
                new Card(CardPoint.QUEEN, CardPattern.SPADE),
                new Card(CardPoint.EIGHT, CardPattern.HEART)
        );
        State stay = hit.stay();

        assertThatThrownBy(() -> stay.draw(new Card(CardPoint.TWO, CardPattern.DIAMOND)))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void 블랙잭_상태의_점수는_21이다() {
        State blackjack = StateFactory.createInitialState(
                new Card(CardPoint.ACE, CardPattern.SPADE),
                new Card(CardPoint.KING, CardPattern.HEART)
        );

        assertThat(blackjack.score()).isEqualTo(21);
        assertThat(blackjack.isBlackjack()).isTrue();
        assertThat(blackjack.isBust()).isFalse();
    }
}
