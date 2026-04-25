package blackjack.application;

import blackjack.domain.deck.Deck;
import blackjack.domain.game.BlackJackGame;
import blackjack.domain.game.FinalIncome;
import blackjack.domain.participant.Dealer;
import blackjack.domain.participant.Players;
import blackjack.repository.BlackJackGameRepository;

public class BlackJackService {

    private final BlackJackGameRepository gameRepository;

    public BlackJackService(BlackJackGameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public GameStartResult startGame(Players players) {
        Long gameId = gameRepository.nextId();
        Dealer dealer = new Dealer();
        Deck deck = new Deck();
        BlackJackGame game = new BlackJackGame(gameId, players, dealer, deck);
        game.initDraw();
        gameRepository.save(game);
        return new GameStartResult(gameId, players, dealer);
    }

    public TurnState getTurnState(Long gameId) {
        BlackJackGame game = gameRepository.findById(gameId);
        if (game.isAllPlayersFinished()) {
            return TurnState.allFinished();
        }
        return TurnState.of(game.getCurrentPlayer());
    }

    public TurnState playerHit(Long gameId) {
        BlackJackGame game = gameRepository.findById(gameId);
        game.playerHit();
        gameRepository.save(game);
        return toTurnState(game);
    }

    public TurnState playerStand(Long gameId) {
        BlackJackGame game = gameRepository.findById(gameId);
        game.playerStand();
        gameRepository.save(game);
        return toTurnState(game);
    }

    public DealerHitResult dealerHitOnce(Long gameId) {
        BlackJackGame game = gameRepository.findById(gameId);
        boolean hit = game.dealerHitOnce();
        gameRepository.save(game);
        return new DealerHitResult(hit);
    }

    public FinalIncome finishGame(Long gameId) {
        BlackJackGame game = gameRepository.findById(gameId);
        return game.judgeGameResult();
    }

    private TurnState toTurnState(BlackJackGame game) {
        if (game.isAllPlayersFinished()) {
            return TurnState.allFinished();
        }
        return TurnState.of(game.getCurrentPlayer());
    }
}
