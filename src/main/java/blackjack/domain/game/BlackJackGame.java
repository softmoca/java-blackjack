package blackjack.domain.game;

import blackjack.domain.deck.Deck;
import blackjack.domain.participant.Dealer;
import blackjack.domain.participant.Player;
import blackjack.domain.participant.Players;
import java.util.function.Consumer;

public class BlackJackGame {
    private static final int INIT_DRAW_CARD_COUNT = 2;
    private final Players players;
    private final Dealer dealer;
    private final Deck deck;

    public BlackJackGame(Players players, Dealer dealer, Deck deck) {
        this.players = players;
        this.dealer = dealer;
        this.deck = deck;
    }

    public void initDraw() {
        for (int i = 0; i < INIT_DRAW_CARD_COUNT; i++) {
            players.recieveCard(deck);
            dealer.recieveCard(deck.draw());
        }
    }

    public void proceedAllPlayersTurn(PlayerHitStrategy hitStrategy,
                                      Consumer<Player> onCardReceived) {
        for (Player player : players.getPlayers()) {
            proceedPlayerTurn(player, hitStrategy,
                    () -> onCardReceived.accept(player));
        }
    }

    private void proceedPlayerTurn(Player player, PlayerHitStrategy hitStrategy,
                                   Runnable onCardReceived) {
        while (player.shouldDraw() && hitStrategy.shouldHit(player)) {
            player.recieveCard(deck.draw());
            onCardReceived.run();
        }
    }

    public void proceedDealerTurn(DealerHitStrategy hitStrategy,
                                  Runnable onCardReceived) {
        while (hitStrategy.shouldHit(dealer)) {
            dealer.recieveCard(deck.draw());
            onCardReceived.run();
        }
    }

    public FinalIncome judgeGameResult() {
        BlackJackJudge blackJackJudge = new BlackJackJudge();
        return blackJackJudge.judge(players, dealer);
    }

}
