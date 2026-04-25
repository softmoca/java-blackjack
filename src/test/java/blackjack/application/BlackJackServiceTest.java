package blackjack.application;

import static org.assertj.core.api.Assertions.assertThat;

import blackjack.domain.participant.BetAmount;
import blackjack.domain.participant.Name;
import blackjack.domain.participant.Player;
import blackjack.domain.participant.Players;
import blackjack.repository.BlackJackGameRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BlackJackServiceTest {

    private BlackJackService service;

    @BeforeEach
    void setUp() {
        service = new BlackJackService(new BlackJackGameRepository());
    }

    private Players createPlayers(String... names) {
        return new Players(
                java.util.Arrays.stream(names)
                        .map(n -> new Player(new Name(n), new BetAmount(1000)))
                        .toList()
        );
    }

    @Test
    void 게임을_시작하면_각_플레이어와_딜러에게_2장씩_배분된다() {
        GameStartResult result = service.startGame(createPlayers("pobi"));

        assertThat(result.getGameId()).isNotNull();
        assertThat(result.getPlayers().getPlayers().get(0).getCardCount()).isEqualTo(2);
        assertThat(result.getDealer().getCardCount()).isEqualTo(2);
    }

    @Test
    void 플레이어가_stand하면_다음_플레이어로_넘어간다() {
        GameStartResult start = service.startGame(createPlayers("pobi", "crong"));

        TurnState state = service.getTurnState(start.getGameId());
        assertThat(state.getCurrentPlayer().getName()).isEqualTo("pobi");

        service.playerStand(start.getGameId());

        TurnState afterStand = service.getTurnState(start.getGameId());
        assertThat(afterStand.getCurrentPlayer().getName()).isEqualTo("crong");
    }

    @Test
    void 마지막_플레이어가_stand하면_모든_플레이어의_턴이_끝난다() {
        GameStartResult start = service.startGame(createPlayers("pobi"));

        service.playerStand(start.getGameId());

        TurnState state = service.getTurnState(start.getGameId());
        assertThat(state.isAllPlayersFinished()).isTrue();
    }

    @Test
    void 존재하지_않는_게임_조회시_예외가_발생한다() {
        org.assertj.core.api.Assertions.assertThatThrownBy(
                () -> service.getTurnState(999L)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
