package blackjack.domain.game;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class PlayerActionTest {

    @Test
    void y를_입력하면_HIT이다() {
        assertThat(PlayerAction.from("y")).isEqualTo(PlayerAction.HIT);
    }

    @Test
    void n을_입력하면_STAY이다() {
        assertThat(PlayerAction.from("n")).isEqualTo(PlayerAction.STAY);
    }

    @Test
    void s를_입력하면_SURRENDER이다() {
        assertThat(PlayerAction.from("s")).isEqualTo(PlayerAction.SURRENDER);
    }

    @Test
    void 잘못된_입력이면_예외가_발생한다() {
        assertThatThrownBy(() -> PlayerAction.from("x"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 빈_문자열이면_예외가_발생한다() {
        assertThatThrownBy(() -> PlayerAction.from(""))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
