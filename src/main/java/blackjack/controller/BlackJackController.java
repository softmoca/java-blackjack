package blackjack.controller;

import blackjack.domain.deck.Deck;
import blackjack.domain.game.BlackJackGame;
import blackjack.domain.game.DefaultDealerHitStrategy;
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

        BlackJackGame blackJackGame = startGame(players, dealer, deck);
        playGame(blackJackGame);
        showGameResult(blackJackGame, dealer, players);
    }

    private BlackJackGame startGame(Players players, Dealer dealer, Deck deck) {
        BlackJackGame blackJackGame = new BlackJackGame(players, dealer, deck);
        blackJackGame.initDraw();
        outputView.printInitDraw(players, dealer);
        return blackJackGame;
    }

    private void playGame(BlackJackGame game) {
        game.proceedAllPlayersTurn(
                new ConsolePlayerHitStrategy(inputView),
                outputView::printCard
        );

        game.proceedDealerTurn(
                new DefaultDealerHitStrategy(),
                outputView::printDealerDraw
        );
    }


    private void showGameResult(BlackJackGame blackJackGame, Dealer dealer, Players players) {
        outputView.printFinalCardResult(dealer, players);
        FinalIncome result = blackJackGame.judgeGameResult();
        outputView.printFinalGameResult(result);
    }

}
