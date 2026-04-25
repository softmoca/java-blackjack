package blackjack.controller;

import blackjack.application.BlackJackService;
import blackjack.application.DealerHitResult;
import blackjack.application.GameStartResult;
import blackjack.application.PlayerSignupForm;
import blackjack.application.PlayersAssembler;
import blackjack.application.TurnState;
import blackjack.domain.game.FinalIncome;
import blackjack.domain.participant.BetAmount;
import blackjack.domain.participant.Name;
import blackjack.domain.participant.Player;
import blackjack.domain.participant.Players;
import blackjack.view.InputView;
import blackjack.view.OutputView;
import java.util.List;
import java.util.function.Supplier;

public class BlackJackController {

    private final InputView inputView;
    private final OutputView outputView;
    private final BlackJackService service;
    private final PlayersAssembler playersAssembler;

    public BlackJackController(InputView inputView,
                               OutputView outputView,
                               BlackJackService service,
                               PlayersAssembler playersAssembler) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.service = service;
        this.playersAssembler = playersAssembler;
    }

    public void run() {
        GameStartResult start = startGame();
        playAllPlayersTurn(start.getGameId());
        playDealerTurn(start.getGameId());
        showResult(start);
    }

    private GameStartResult startGame() {
        Players players = readPlayers();
        GameStartResult start = service.startGame(players);
        outputView.printInitDraw(start.getPlayers(), start.getDealer());
        return start;
    }

    private Players readPlayers() {
        List<String> validNames = retryUntilValid(this::readValidPlayerNames);
        List<PlayerSignupForm> forms = validNames.stream()
                .map(name -> new PlayerSignupForm(name, retryUntilValid(() -> readValidBetAmount(name))))
                .toList();
        return playersAssembler.assemble(forms);
    }

    private List<String> readValidPlayerNames() {
        List<String> names = inputView.readPlayerNames();
        names.forEach(Name::new);  // 도메인 검증만 위임 (버리는 호출)
        return names;
    }

    private int readValidBetAmount(String name) {
        int amount = inputView.readBetAmount(name);
        new BetAmount(amount);  // 도메인 검증만 위임 (버리는 호출)
        return amount;
    }

    private void playAllPlayersTurn(Long gameId) {
        while (true) {
            TurnState state = service.getTurnState(gameId);
            if (state.isAllPlayersFinished()) {
                return;
            }
            playOnePlayerTurn(gameId, state.getCurrentPlayer());
        }
    }

    private void playOnePlayerTurn(Long gameId, Player player) {
        while (true) {
            boolean wantsHit = retryUntilValid(() -> inputView.readHitAnswer(player.getName()));
            if (!wantsHit) {
                service.playerStand(gameId);
                return;
            }
            TurnState after = service.playerHit(gameId);
            outputView.printCard(player);
            if (after.isAllPlayersFinished() || !after.getCurrentPlayer().equals(player)) {
                return;
            }
        }
    }

    private void playDealerTurn(Long gameId) {
        while (true) {
            DealerHitResult result = service.dealerHitOnce(gameId);
            if (!result.isHit()) {
                return;
            }
            outputView.printDealerDraw();
        }
    }

    private void showResult(GameStartResult start) {
        outputView.printFinalCardResult(start.getDealer(), start.getPlayers());
        FinalIncome result = service.finishGame(start.getGameId());
        outputView.printFinalGameResult(result);
    }

    private <T> T retryUntilValid(Supplier<T> inputAction) {
        while (true) {
            try {
                return inputAction.get();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
