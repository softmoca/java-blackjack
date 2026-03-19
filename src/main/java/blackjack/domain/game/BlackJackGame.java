package blackjack.domain.game;

import blackjack.domain.deck.Deck;
import blackjack.domain.participant.Dealer;
import blackjack.domain.participant.Player;
import blackjack.domain.participant.Players;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class BlackJackGame {

    private final Players players;
    private final Dealer dealer;
    private final Deck deck;

    public BlackJackGame(Players players, Dealer dealer, Deck deck) {
        this.players = players;
        this.dealer = dealer;
        this.deck = deck;
    }

    public void initDeal() {
        players.initDeal(deck);
        dealer.initDeal(deck.draw(), deck.draw());
    }

    public void proceedAllPlayersTurn(PlayerHitStrategy hitStrategy,
                                      Consumer<Player> onCardReceived) {
        for (Player player : players.getPlayers()) {
            proceedPlayerTurn(player, hitStrategy, onCardReceived);
        }
    }

    private void proceedPlayerTurn(Player player, PlayerHitStrategy hitStrategy,
                                   Consumer<Player> onCardReceived) {
        while (!player.isFinished() && hitStrategy.shouldHit(player.getName())) {
            player.draw(deck.draw());
            onCardReceived.accept(player);
        }
        if (!player.isFinished()) {
            player.stay();
        }
    }

    public void proceedDealerTurn(Runnable onCardReceived) {
        while (dealer.shouldDraw()) {
            dealer.draw(deck.draw());
            onCardReceived.run();
        }
        if (!dealer.isFinished()) {
            dealer.stay();
        }
    }

    public FinalIncome judgeGameResult() {
        BlackJackJudge judge = new BlackJackJudge();
        Map<Player, Integer> incomeResult = new LinkedHashMap<>();
        int dealerIncome = 0;

        for (Player player : players.getPlayers()) {
            GameResult gameResult = judge.judge(player.getState(), dealer.getState());
            int income = gameResult.calculateIncome(player.getBetAmount());
            incomeResult.put(player, income);
            dealerIncome -= income;
        }

        return new FinalIncome(dealerIncome, incomeResult);
    }
}
