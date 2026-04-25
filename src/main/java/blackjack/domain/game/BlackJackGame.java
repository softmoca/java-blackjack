package blackjack.domain.game;

import blackjack.domain.deck.Deck;
import blackjack.domain.participant.Dealer;
import blackjack.domain.participant.Player;
import blackjack.domain.participant.Players;
import java.util.function.Consumer;

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

    // 새 생성자 (id 포함) — Repository 저장용
    public BlackJackGame(Long id, Players players, Dealer dealer, Deck deck) {
        this.id = id;
        this.players = players;
        this.dealer = dealer;
        this.deck = deck;
    }

    public Long getId() {
        return id;
    }

    public Players getPlayers() {
        return players;
    }

    public Dealer getDealer() {
        return dealer;
    }


    public void initDraw() {
        for (int i = 0; i < INIT_DRAW_CARD_COUNT; i++) {
            players.recieveCard(deck);
            dealer.recieveCard(deck.draw());
        }
    }
    // === 새로 추가된 1회성 행동 메서드들 ===

    public void playerHit() {
        Player player = getCurrentPlayer();
        if (!player.shouldDraw()) {
            throw new IllegalStateException("더 이상 카드를 받을 수 없습니다.");
        }
        player.recieveCard(deck.draw());
        if (player.isBust()) {
            moveToNextPlayer();
        }
    }

    public void playerStand() {
        getCurrentPlayer();  // 현재 플레이어가 유효한지 확인
        moveToNextPlayer();
    }

    public boolean dealerHitOnce() {
        if (!dealer.shouldDraw()) {
            return false;
        }
        dealer.recieveCard(deck.draw());
        return true;
    }

    // === 상태 조회 ===

    public boolean isAllPlayersFinished() {
        return currentPlayerIndex >= players.getPlayers().size();
    }

    public Player getCurrentPlayer() {
        if (isAllPlayersFinished()) {
            throw new IllegalStateException("모든 플레이어의 턴이 끝났습니다.");
        }
        return players.getPlayers().get(currentPlayerIndex);
    }

    private void moveToNextPlayer() {
        currentPlayerIndex++;
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
