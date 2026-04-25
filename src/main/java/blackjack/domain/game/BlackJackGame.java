package blackjack.domain.game;

import blackjack.domain.deck.Deck;
import blackjack.domain.participant.Dealer;
import blackjack.domain.participant.Player;
import blackjack.domain.participant.Players;

public class BlackJackGame {
    private static final int INIT_DRAW_CARD_COUNT = 2;
    private final Long id;
    private final Players players;
    private final Dealer dealer;
    private final Deck deck;

    private int currentPlayerIndex = 0;

    public BlackJackGame(Players players, Dealer dealer, Deck deck) {
        this(null, players, dealer, deck);
    }

    public BlackJackGame(Long id, Players players, Dealer dealer, Deck deck) {
        this.id = id;
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

    public void playerHit() {
        Player player = getCurrentPlayer();
        player.recieveCard(deck.draw());
        advanceIfTurnEnded(player);
    }

    public void playerStand() {
        getCurrentPlayer();
        moveToNextPlayer();
    }

    public boolean dealerHitOnce() {
        if (!dealer.shouldDraw()) {
            return false;
        }
        dealer.recieveCard(deck.draw());
        return true;
    }

    public boolean isAllPlayersFinished() {
        return currentPlayerIndex >= players.getPlayers().size();
    }

    public Player getCurrentPlayer() {
        if (isAllPlayersFinished()) {
            throw new IllegalStateException("모든 플레이어의 턴이 끝났습니다.");
        }
        return players.getPlayers().get(currentPlayerIndex);
    }

    private void advanceIfTurnEnded(Player player) {
        if (player.isBust()) {
            moveToNextPlayer();
        }
    }

    private void moveToNextPlayer() {
        currentPlayerIndex++;
    }

    public FinalIncome judgeGameResult() {
        return new BlackJackJudge().judge(players, dealer);
    }

    public Long getId() { return id; }
    public Players getPlayers() { return players; }
    public Dealer getDealer() { return dealer; }
}
