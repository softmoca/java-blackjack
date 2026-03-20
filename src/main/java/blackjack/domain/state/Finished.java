package blackjack.domain.state;

import blackjack.domain.card.Card;
import blackjack.domain.card.Hand;

public abstract class Finished extends Started {

    protected Finished(Hand hand) {
        super(hand);
    }

    @Override
    public State draw(Card card) {
        throw new UnsupportedOperationException("이미 종료된 상태에서는 카드를 뽑을 수 없습니다.");
    }

    @Override
    public State stay() {
        throw new UnsupportedOperationException("이미 종료된 상태에서는 스테이할 수 없습니다.");
    }

    @Override
    public State surrender() {        // ← 추가
        throw new UnsupportedOperationException("이미 종료된 상태에서는 서렌더할 수 없습니다.");
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
