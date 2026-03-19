package blackjack.domain.state;

import static org.assertj.core.api.Assertions.assertThat;

import blackjack.domain.card.Card;
import blackjack.domain.card.CardPattern;
import blackjack.domain.card.CardPoint;
import org.junit.jupiter.api.Test;

class StateFactoryTest {

    @Test
    void 두_장의_합이_21이면_블랙잭_상태를_반환한다() {
        Card ace = new Card(CardPoint.ACE, CardPattern.SPADE);
        Card king = new Card(CardPoint.KING, CardPattern.HEART);

        State state = StateFactory.createInitialState(ace, king);

        assertThat(state).isInstanceOf(Blackjack.class);
        assertThat(state.isFinished()).isTrue();
        assertThat(state.isBlackjack()).isTrue();
    }

    @Test
    void 두_장의_합이_21이_아니면_히트_상태를_반환한다() {
        Card two = new Card(CardPoint.TWO, CardPattern.SPADE);
        Card three = new Card(CardPoint.THREE, CardPattern.HEART);

        State state = StateFactory.createInitialState(two, three);

        assertThat(state).isInstanceOf(Hit.class);
        assertThat(state.isFinished()).isFalse();
        assertThat(state.isBlackjack()).isFalse();
    }
}
