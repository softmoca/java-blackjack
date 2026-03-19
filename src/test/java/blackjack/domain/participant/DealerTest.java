package blackjack.domain.participant;

import static org.assertj.core.api.Assertions.assertThat;

import blackjack.domain.card.Card;
import blackjack.domain.card.CardPattern;
import blackjack.domain.card.CardPoint;
import org.junit.jupiter.api.Test;

class DealerTest {

    @Test
    void 딜러가_초기_두_장을_받는다() {
        Dealer dealer = new Dealer();

        dealer.initDeal(
                new Card(CardPoint.THREE, CardPattern.DIAMOND),
                new Card(CardPoint.FIVE, CardPattern.CLUB)
        );

        assertThat(dealer.getCardCount()).isEqualTo(2);
        assertThat(dealer.getTotalPoint()).isEqualTo(8);
    }

    @Test
    void 딜러_점수가_16이하이면_더_뽑아야_한다() {
        Dealer dealer = new Dealer();
        dealer.initDeal(
                new Card(CardPoint.THREE, CardPattern.DIAMOND),
                new Card(CardPoint.FIVE, CardPattern.CLUB)
        );

        assertThat(dealer.shouldDraw()).isTrue();
    }

    @Test
    void 딜러_점수가_17이상이면_더_뽑지_않는다() {
        Dealer dealer = new Dealer();
        dealer.initDeal(
                new Card(CardPoint.TEN, CardPattern.DIAMOND),
                new Card(CardPoint.SEVEN, CardPattern.CLUB)
        );

        assertThat(dealer.shouldDraw()).isFalse();
    }

    @Test
    void 딜러가_블랙잭이면_더_뽑지_않는다() {
        Dealer dealer = new Dealer();
        dealer.initDeal(
                new Card(CardPoint.ACE, CardPattern.DIAMOND),
                new Card(CardPoint.KING, CardPattern.CLUB)
        );

        assertThat(dealer.shouldDraw()).isFalse();
        assertThat(dealer.isBlackjack()).isTrue();
    }
}
