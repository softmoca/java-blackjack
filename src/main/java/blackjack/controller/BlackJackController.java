package blackjack.controller;

import blackjack.domain.deck.Deck;
import blackjack.domain.game.BlackJackGame;
import blackjack.domain.game.FinalIncome;
import blackjack.domain.participant.Dealer;
import blackjack.domain.participant.Players;
import blackjack.view.ConsolePlayerHitStrategy;
import blackjack.view.InputView;
import blackjack.view.OutputView;

public class BlackJackController {
    private final InputView inputView = new InputView();
    private final OutputView outputView = new OutputView();

    public void run() {
        Players players = inputView.readPlayers();
        Dealer dealer = new Dealer();
        Deck deck = new Deck();

        BlackJackGame game = new BlackJackGame(players, dealer, deck);
        startGame(game, players, dealer);
        playGame(game);
        showGameResult(game, dealer, players);
    }

    private void startGame(BlackJackGame game, Players players, Dealer dealer) {
        game.initDeal();
        outputView.printInitDeal(players, dealer);
    }

    private void playGame(BlackJackGame game) {
        game.proceedAllPlayersTurn(
                new ConsolePlayerHitStrategy(inputView),
                outputView::printCard
        );

        game.proceedDealerTurn(outputView::printDealerDraw);
    }

    private void showGameResult(BlackJackGame game, Dealer dealer, Players players) {
        outputView.printFinalCardResult(dealer, players);
        FinalIncome result = game.judgeGameResult();
        outputView.printFinalGameResult(result);
    }
}
