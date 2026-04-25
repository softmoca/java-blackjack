package blackjack;

import blackjack.application.BlackJackService;
import blackjack.application.PlayersAssembler;
import blackjack.controller.BlackJackController;
import blackjack.repository.BlackJackGameRepository;
import blackjack.view.InputView;
import blackjack.view.OutputView;

public class Application {
    public static void main(String[] args) {
        InputView inputView = new InputView();
        OutputView outputView = new OutputView();
        BlackJackGameRepository repository = new BlackJackGameRepository();
        BlackJackService service = new BlackJackService(repository);
        PlayersAssembler playersAssembler = new PlayersAssembler();

        BlackJackController controller = new BlackJackController(
                inputView, outputView, service, playersAssembler
        );
        controller.run();
    }
}
