package blackjack.application;

import blackjack.domain.participant.BetAmount;
import blackjack.domain.participant.Name;
import blackjack.domain.participant.Player;
import blackjack.domain.participant.Players;
import java.util.List;

public class PlayersAssembler {

    public Players assemble(List<PlayerSignupForm> forms) {
        List<Player> players = forms.stream()
                .map(this::toPlayer)
                .toList();
        return new Players(players);
    }

    private Player toPlayer(PlayerSignupForm form) {
        return new Player(new Name(form.name()), new BetAmount(form.betAmount()));
    }
}
