package blackjack.domain.state;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import blackjack.domain.card.Card;
import blackjack.domain.card.CardPattern;
import blackjack.domain.card.CardPoint;
import org.junit.jupiter.api.Test;

class SurrenderTest {

    @Test
    void 히트_상태에서_서렌더하면_서렌더_상태가_된다() {
        State hit = StateFactory.createInitialState(
                new Card(CardPoint.TEN, CardPattern.SPADE),
                new Card(CardPoint.SIX, CardPattern.HEART)
        );

        State surrendered = hit.surrender();

        assertThat(surrendered).isInstanceOf(Surrender.class);
        assertThat(surrendered.isFinished()).isTrue();
        assertThat(surrendered.isSurrender()).isTrue();
        assertThat(surrendered.isBust()).isFalse();
        assertThat(surrendered.isBlackjack()).isFalse();
    }

    @Test
    void 서렌더_상태에서_카드를_뽑으면_예외가_발생한다() {
        State hit = StateFactory.createInitialState(
                new Card(CardPoint.TEN, CardPattern.SPADE),
                new Card(CardPoint.SIX, CardPattern.HEART)
        );
        State surrendered = hit.surrender();

        assertThatThrownBy(() -> surrendered.draw(new Card(CardPoint.TWO, CardPattern.CLUB)))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void 서렌더_상태에서_스테이하면_예외가_발생한다() {
        State hit = StateFactory.createInitialState(
                new Card(CardPoint.TEN, CardPattern.SPADE),
                new Card(CardPoint.SIX, CardPattern.HEART)
        );
        State surrendered = hit.surrender();

        assertThatThrownBy(surrendered::stay)
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void 서렌더_상태에서_다시_서렌더하면_예외가_발생한다() {
        State hit = StateFactory.createInitialState(
                new Card(CardPoint.TEN, CardPattern.SPADE),
                new Card(CardPoint.SIX, CardPattern.HEART)
        );
        State surrendered = hit.surrender();

        assertThatThrownBy(surrendered::surrender)
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void 블랙잭_상태에서_서렌더하면_예외가_발생한다() {
        State blackjack = StateFactory.createInitialState(
                new Card(CardPoint.ACE, CardPattern.SPADE),
                new Card(CardPoint.KING, CardPattern.HEART)
        );

        assertThatThrownBy(blackjack::surrender)
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void 히트가_아닌_상태에서는_isSurrender가_false다() {
        State hit = StateFactory.createInitialState(
                new Card(CardPoint.TWO, CardPattern.SPADE),
                new Card(CardPoint.THREE, CardPattern.HEART)
        );

        assertThat(hit.isSurrender()).isFalse();

        State stay = hit.stay();
        assertThat(stay.isSurrender()).isFalse();
    }
}
