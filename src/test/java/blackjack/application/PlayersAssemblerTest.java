package blackjack.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import blackjack.domain.participant.Players;
import java.util.List;
import org.junit.jupiter.api.Test;

class PlayersAssemblerTest {

    private final PlayersAssembler assembler = new PlayersAssembler();

    @Test
    void 신청_폼_목록으로부터_Players를_조립한다() {
        List<PlayerSignupForm> forms = List.of(
                new PlayerSignupForm("pobi", 1000),
                new PlayerSignupForm("crong", 2000)
        );

        Players players = assembler.assemble(forms);

        assertThat(players.getPlayers()).hasSize(2);
        assertThat(players.getPlayers().get(0).getName()).isEqualTo("pobi");
        assertThat(players.getPlayers().get(0).getBetAmount()).isEqualTo(1000);
        assertThat(players.getPlayers().get(1).getName()).isEqualTo("crong");
        assertThat(players.getPlayers().get(1).getBetAmount()).isEqualTo(2000);
    }

    @Test
    void 베팅이_0_이하이면_도메인_검증에서_예외가_발생한다() {
        List<PlayerSignupForm> forms = List.of(
                new PlayerSignupForm("pobi", 0)
        );

        assertThatThrownBy(() -> assembler.assemble(forms))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("양수");
    }

    @Test
    void 이름이_공백이면_도메인_검증에서_예외가_발생한다() {
        List<PlayerSignupForm> forms = List.of(
                new PlayerSignupForm("", 1000)
        );

        assertThatThrownBy(() -> assembler.assemble(forms))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("빈 값");
    }
}
