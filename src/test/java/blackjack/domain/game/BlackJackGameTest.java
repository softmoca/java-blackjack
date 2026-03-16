package blackjack.domain.game;

import static org.assertj.core.api.Assertions.assertThat;

import blackjack.domain.card.Card;
import blackjack.domain.card.CardPattern;
import blackjack.domain.card.CardPoint;
import blackjack.domain.deck.Deck;
import blackjack.domain.deck.FixedOrderShuffleStrategy;
import blackjack.domain.deck.NoShuffleStrategy;
import blackjack.domain.participant.BetAmount;
import blackjack.domain.participant.Dealer;
import blackjack.domain.participant.Name;
import blackjack.domain.participant.Player;
import blackjack.domain.participant.Players;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class BlackJackGameTest {

    private static final BetAmount DEFAULT_BET_AMOUNT = new BetAmount(1000);

    private Player createPlayer(String name, int amount) {
        return new Player(new Name(name), new BetAmount(amount));
    }

    @Test
    void 게임_시작시_플레이어와_딜러는_각각_두장의_카드를_받는다() {
        Players players = new Players(List.of(
                new Player(new Name("pobi"), DEFAULT_BET_AMOUNT),
                new Player(new Name("jason"), DEFAULT_BET_AMOUNT)
        ));
        Dealer dealer = new Dealer();
        Deck deck = new Deck();
        BlackJackGame game = new BlackJackGame(players, dealer, deck);

        game.initDraw();

        assertThat(players.getPlayers().get(0).getCardCount()).isEqualTo(2);
        assertThat(players.getPlayers().get(1).getCardCount()).isEqualTo(2);
        assertThat(dealer.getCardCount()).isEqualTo(2);
    }


    @Test
    void 시작_카드가_A와_K이면_플레이어는_블랙잭이다() {
        Player player = new Player(new Name("pobi"), DEFAULT_BET_AMOUNT);
        Players players = new Players(List.of(player));
        Dealer dealer = new Dealer();

        Deck deck = new Deck(new FixedOrderShuffleStrategy(List.of(
                new Card(CardPoint.TWO, CardPattern.CLUB),
                new Card(CardPoint.THREE, CardPattern.DIAMOND),
                new Card(CardPoint.NINE, CardPattern.HEART),   // dealer 2라운드 - 4번째 뽑힘
                new Card(CardPoint.KING, CardPattern.SPADE),   // pobi 2라운드 - 3번째 뽑힘
                new Card(CardPoint.FIVE, CardPattern.CLUB),    // dealer 1라운드  2번째 뽑힘
                new Card(CardPoint.ACE, CardPattern.HEART)     // pobi 1라운드  - 1번째 뽑힘
        )));

        BlackJackGame blackJackGame = new BlackJackGame(players, dealer, deck);
        blackJackGame.initDraw();

        assertThat(player.isBlackJack()).isTrue();
    }


    @Test
    void NoShuffleStrategy를_주입하면_카드_순서가_유지된다() {
        Deck deck = new Deck(new NoShuffleStrategy());

        // draw()는 removeLast() 이므로 마지막 카드(CLUB × ACE)가 첫 번째로 나옴
        Card firstDrawn = deck.draw();
        assertThat(firstDrawn.getName()).isEqualTo("A클로버");
    }

// --- proceedAllPlayersTurn 테스트 ---

    @Test
    void 플레이어가_y를_선택하면_카드를_한장_더_받는다() {
        // given
        Player pobi = createPlayer("pobi", 1000);
        Players players = new Players(List.of(pobi));
        Dealer dealer = new Dealer();

        // 덱 구성
        Deck deck = new Deck();

        BlackJackGame game = new BlackJackGame(players, dealer, deck);
        game.initDraw(); // pobi: 2장, dealer: 2장

        // true 한 번 → 카드 한 장 더 받고
        // false     → 종료
        PlayerHitStrategy strategy = new FixedPlayerHitStrategy(true, false);

        // when
        game.proceedAllPlayersTurn(strategy, p -> {
        });

        // then
        assertThat(pobi.getCardCount()).isEqualTo(3); // 초기 2장 + 추가 1장
    }

    @Test
    void 플레이어가_n을_선택하면_카드를_받지_않는다() {
        // given
        Player pobi = createPlayer("pobi", 1000);
        Players players = new Players(List.of(pobi));
        Dealer dealer = new Dealer();
        Deck deck = new Deck();

        BlackJackGame game = new BlackJackGame(players, dealer, deck);
        game.initDraw(); // pobi: 2장

        // false → 바로 종료
        PlayerHitStrategy strategy = new FixedPlayerHitStrategy(false);

        // when
        game.proceedAllPlayersTurn(strategy, p -> {
        });

        // then
        assertThat(pobi.getCardCount()).isEqualTo(2); // 추가 없음
    }

    @Test
    void 플레이어가_버스트되면_전략과_관계없이_카드를_더_받지_않는다() {
        // given
        Player pobi = createPlayer("pobi", 1000);
        Players players = new Players(List.of(pobi));
        Dealer dealer = new Dealer();

        // pobi가 버스트 되도록 카드 구성
        // 초기: Q(10) + K(10) = 20
        // 추가: J(10) → 30 버스트
        Deck deck = new Deck(new FixedOrderShuffleStrategy(List.of(
                new Card(CardPoint.JACK, CardPattern.CLUB),   // pobi 추가 카드
                new Card(CardPoint.THREE, CardPattern.CLUB),  // dealer 2번째
                new Card(CardPoint.KING, CardPattern.CLUB),   // pobi 2번째
                new Card(CardPoint.FOUR, CardPattern.CLUB),   // dealer 1번째
                new Card(CardPoint.QUEEN, CardPattern.CLUB)   // pobi 1번째 - 첫 뽑힘
        )));

        BlackJackGame game = new BlackJackGame(players, dealer, deck);
        game.initDraw(); // pobi: Q + K = 20

        // 전략은 계속 true지만 버스트 후엔 멈춰야 함
        PlayerHitStrategy strategy = new FixedPlayerHitStrategy(true, true, true);

        // when
        game.proceedAllPlayersTurn(strategy, p -> {
        });// pobi: Q + K +J= 30

        // then - 버스트 후 추가 카드 없음
        assertThat(pobi.isBust()).isTrue();
        assertThat(pobi.getCardCount()).isEqualTo(3); // 초기 2장 + 버스트 1장
    }

    // --- 콜백 테스트 ---
    @Test
    void 플레이어가_카드를_받을때마다_콜백이_호출된다() {
        // given
        Player pobi = createPlayer("pobi", 1000);
        Players players = new Players(List.of(pobi));
        Dealer dealer = new Dealer();
        Deck deck = new Deck();

        BlackJackGame game = new BlackJackGame(players, dealer, deck);
        game.initDraw();

        // 콜백 호출 횟수를 추적
        List<Player> receivedPlayers = new ArrayList<>();

        // true 두 번 → 카드 2장 추가
        PlayerHitStrategy strategy = new FixedPlayerHitStrategy(true, true, false);

        // when
        game.proceedAllPlayersTurn(strategy, receivedPlayers::add);

        // then - 카드 받을 때마다 콜백 호출됐는지 확인
        assertThat(receivedPlayers).hasSize(2);
        assertThat(receivedPlayers).containsOnly(pobi);
    }

    @Test
    void 플레이어가_카드를_받지_않으면_콜백이_호출되지_않는다() {
        // given
        Player pobi = createPlayer("pobi", 1000);
        Players players = new Players(List.of(pobi));
        Dealer dealer = new Dealer();
        Deck deck = new Deck();

        BlackJackGame game = new BlackJackGame(players, dealer, deck);
        game.initDraw();

        List<Player> receivedPlayers = new ArrayList<>();
        PlayerHitStrategy strategy = new FixedPlayerHitStrategy(false);

        // when
        game.proceedAllPlayersTurn(strategy, receivedPlayers::add);

        // then
        assertThat(receivedPlayers).isEmpty();
    }

    // --- proceedDealerTurn 테스트 ---

    @Test
    void 딜러가_전략에_따라_카드를_한장_받는다() {
        // given
        Players players = new Players(List.of(createPlayer("pobi", 1000)));
        Dealer dealer = new Dealer();
        Deck deck = new Deck();

        BlackJackGame game = new BlackJackGame(players, dealer, deck);
        game.initDraw(); // dealer: 2장

        // true 한 번 → 카드 1장 추가
        // false     → 종료
        DealerHitStrategy strategy = new FixedDealerHitStrategy(true) {
            private int count = 0;

            @Override
            public boolean shouldHit(Dealer dealer) {
                return count++ < 1;                 // 한 번만 true, 이후 false
            }
        };

        // when
        game.proceedDealerTurn(strategy, () -> {
        });

        // then
        assertThat(dealer.getCardCount()).isEqualTo(3); // 초기 2장 + 추가 1장
    }

    @Test
    void 딜러_전략이_false이면_카드를_받지_않는다() {
        // given
        Players players = new Players(List.of(createPlayer("pobi", 1000)));
        Dealer dealer = new Dealer();
        Deck deck = new Deck();

        BlackJackGame game = new BlackJackGame(players, dealer, deck);
        game.initDraw(); // dealer: 2장

        DealerHitStrategy strategy = new FixedDealerHitStrategy(false);

        // when
        game.proceedDealerTurn(strategy, () -> {
        });

        // then
        assertThat(dealer.getCardCount()).isEqualTo(2); // 추가 없음
    }

    @Test
    void 딜러가_카드를_받을때마다_콜백이_호출된다() {
        // given
        Players players = new Players(List.of(createPlayer("pobi", 1000)));
        Dealer dealer = new Dealer();
        Deck deck = new Deck();

        BlackJackGame game = new BlackJackGame(players, dealer, deck);
        game.initDraw();

        // 콜백 호출 횟수 추적
        List<String> callbackLog = new ArrayList<>();

        DealerHitStrategy strategy = new FixedDealerHitStrategy(true) {
            private int count = 0;

            @Override
            public boolean shouldHit(Dealer dealer) {
                return count++ < 2; // 두 번만 true
            }
        };

        // when
        game.proceedDealerTurn(strategy, () -> callbackLog.add("카드받음"));

        // then
        assertThat(callbackLog).hasSize(2);
    }

}
