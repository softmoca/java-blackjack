package blackjack.domain.game;

import static org.assertj.core.api.Assertions.assertThat;

import blackjack.domain.card.Card;
import blackjack.domain.card.CardPattern;
import blackjack.domain.card.CardPoint;
import blackjack.domain.deck.Deck;
import blackjack.domain.deck.FixedOrderShuffleStrategy;
import blackjack.domain.participant.BetAmount;
import blackjack.domain.participant.Dealer;
import blackjack.domain.participant.Name;
import blackjack.domain.participant.Player;
import blackjack.domain.participant.Players;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class BlackJackGameTest {

    private Player createPlayer(String name, int amount) {
        return new Player(new Name(name), new BetAmount(amount));
    }

    // --- initDeal 테스트 ---

    @Test
    void 게임_시작시_플레이어와_딜러는_각각_두장의_카드를_받는다() {
        Player pobi = createPlayer("pobi", 1000);
        Player jason = createPlayer("jason", 1000);
        Players players = new Players(List.of(pobi, jason));
        Dealer dealer = new Dealer();

        // pobi 2장 → jason 2장 → dealer 2장 순서
        Deck deck = new Deck(new FixedOrderShuffleStrategy(List.of(
                new Card(CardPoint.NINE, CardPattern.CLUB),    // dealer 2번째
                new Card(CardPoint.TEN, CardPattern.CLUB),     // dealer 1번째
                new Card(CardPoint.SEVEN, CardPattern.HEART),  // jason 2번째
                new Card(CardPoint.SIX, CardPattern.HEART),    // jason 1번째
                new Card(CardPoint.THREE, CardPattern.SPADE),  // pobi 2번째
                new Card(CardPoint.TWO, CardPattern.SPADE)     // pobi 1번째
        )));

        BlackJackGame game = new BlackJackGame(players, dealer, deck);
        game.initDeal();

        assertThat(pobi.getCardCount()).isEqualTo(2);
        assertThat(jason.getCardCount()).isEqualTo(2);
        assertThat(dealer.getCardCount()).isEqualTo(2);
    }

    @Test
    void 시작_카드가_A와_K이면_플레이어는_블랙잭이다() {
        Player pobi = createPlayer("pobi", 1000);
        Players players = new Players(List.of(pobi));
        Dealer dealer = new Dealer();

        // initDeal: pobi가 먼저 2장, 그 다음 dealer가 2장
        // draw()는 removeLast()이므로 마지막이 먼저 뽑힘
        Deck deck = new Deck(new FixedOrderShuffleStrategy(List.of(
                new Card(CardPoint.FIVE, CardPattern.CLUB),    // dealer 2번째
                new Card(CardPoint.THREE, CardPattern.DIAMOND),// dealer 1번째
                new Card(CardPoint.KING, CardPattern.SPADE),   // pobi 2번째
                new Card(CardPoint.ACE, CardPattern.HEART)     // pobi 1번째 (먼저 뽑힘)
        )));

        BlackJackGame game = new BlackJackGame(players, dealer, deck);
        game.initDeal();

        assertThat(pobi.isBlackjack()).isTrue();
        assertThat(pobi.isFinished()).isTrue();
    }

    // --- proceedAllPlayersTurn 테스트 ---

    @Test
    void 플레이어가_y를_선택하면_카드를_한장_더_받는다() {
        Player pobi = createPlayer("pobi", 1000);
        Players players = new Players(List.of(pobi));
        Dealer dealer = new Dealer();

        // pobi: 2+3=5, 추가로 4 받아도 9 → 버스트 없음
        Deck deck = new Deck(new FixedOrderShuffleStrategy(List.of(
                new Card(CardPoint.FOUR, CardPattern.CLUB),    // pobi 추가 카드
                new Card(CardPoint.TEN, CardPattern.CLUB),     // dealer 2번째
                new Card(CardPoint.NINE, CardPattern.CLUB),    // dealer 1번째
                new Card(CardPoint.THREE, CardPattern.HEART),  // pobi 2번째
                new Card(CardPoint.TWO, CardPattern.SPADE)     // pobi 1번째
        )));

        BlackJackGame game = new BlackJackGame(players, dealer, deck);
        game.initDeal();

        PlayerHitStrategy strategy = new FixedPlayerHitStrategy(true, false);

        game.proceedAllPlayersTurn(strategy, p -> {
        });

        assertThat(pobi.getCardCount()).isEqualTo(3);
        assertThat(pobi.isFinished()).isTrue();
    }

    @Test
    void 플레이어가_n을_선택하면_카드를_받지_않고_Stay된다() {
        Player pobi = createPlayer("pobi", 1000);
        Players players = new Players(List.of(pobi));
        Dealer dealer = new Dealer();

        // pobi: 5+6=11, dealer: 10+9=19
        Deck deck = new Deck(new FixedOrderShuffleStrategy(List.of(
                new Card(CardPoint.NINE, CardPattern.CLUB),    // dealer 2번째
                new Card(CardPoint.TEN, CardPattern.CLUB),     // dealer 1번째
                new Card(CardPoint.SIX, CardPattern.HEART),    // pobi 2번째
                new Card(CardPoint.FIVE, CardPattern.SPADE)    // pobi 1번째
        )));

        BlackJackGame game = new BlackJackGame(players, dealer, deck);
        game.initDeal();

        PlayerHitStrategy strategy = new FixedPlayerHitStrategy(false);

        game.proceedAllPlayersTurn(strategy, p -> {
        });

        assertThat(pobi.getCardCount()).isEqualTo(2);
        assertThat(pobi.isFinished()).isTrue();
    }

    @Test
    void 플레이어가_버스트되면_더_이상_카드를_받지_않는다() {
        Player pobi = createPlayer("pobi", 1000);
        Players players = new Players(List.of(pobi));
        Dealer dealer = new Dealer();

        Deck deck = new Deck(new FixedOrderShuffleStrategy(List.of(
                new Card(CardPoint.JACK, CardPattern.CLUB),    // pobi 추가 카드
                new Card(CardPoint.THREE, CardPattern.CLUB),   // dealer 2번째
                new Card(CardPoint.FOUR, CardPattern.CLUB),    // dealer 1번째
                new Card(CardPoint.KING, CardPattern.CLUB),    // pobi 2번째
                new Card(CardPoint.QUEEN, CardPattern.CLUB)    // pobi 1번째
        )));

        BlackJackGame game = new BlackJackGame(players, dealer, deck);
        game.initDeal(); // pobi: Q + K = 20

        PlayerHitStrategy strategy = new FixedPlayerHitStrategy(true, true, true);

        game.proceedAllPlayersTurn(strategy, p -> {
        });

        assertThat(pobi.isBust()).isTrue();
        assertThat(pobi.getCardCount()).isEqualTo(3); // 초기 2장 + 버스트 1장
    }

    @Test
    void 플레이어가_카드를_받을때마다_콜백이_호출된다() {
        Player pobi = createPlayer("pobi", 1000);
        Players players = new Players(List.of(pobi));
        Dealer dealer = new Dealer();

        // pobi: 2+3=5, 추가로 2,2 받아도 9 → 버스트 없음
        Deck deck = new Deck(new FixedOrderShuffleStrategy(List.of(
                new Card(CardPoint.TWO, CardPattern.DIAMOND),  // pobi 추가 2번째
                new Card(CardPoint.TWO, CardPattern.CLUB),     // pobi 추가 1번째
                new Card(CardPoint.TEN, CardPattern.CLUB),     // dealer 2번째
                new Card(CardPoint.NINE, CardPattern.CLUB),    // dealer 1번째
                new Card(CardPoint.THREE, CardPattern.HEART),  // pobi 2번째
                new Card(CardPoint.TWO, CardPattern.SPADE)     // pobi 1번째
        )));

        BlackJackGame game = new BlackJackGame(players, dealer, deck);
        game.initDeal();

        List<Player> receivedPlayers = new ArrayList<>();
        PlayerHitStrategy strategy = new FixedPlayerHitStrategy(true, true, false);

        game.proceedAllPlayersTurn(strategy, receivedPlayers::add);

        assertThat(receivedPlayers).hasSize(2);
        assertThat(receivedPlayers).containsOnly(pobi);
    }

    // --- proceedDealerTurn 테스트 ---

    @Test
    void 딜러가_16이하이면_카드를_더_받는다() {
        Player pobi = createPlayer("pobi", 1000);
        Players players = new Players(List.of(pobi));
        Dealer dealer = new Dealer();

        // dealer: 3 + 5 = 8 → shouldDraw true → 추가 카드 받음
        Deck deck = new Deck(new FixedOrderShuffleStrategy(List.of(
                new Card(CardPoint.TEN, CardPattern.HEART),    // dealer 추가 카드 (8+10=18, stop)
                new Card(CardPoint.FIVE, CardPattern.CLUB),    // dealer 2번째
                new Card(CardPoint.THREE, CardPattern.DIAMOND),// dealer 1번째
                new Card(CardPoint.NINE, CardPattern.SPADE),   // pobi 2번째
                new Card(CardPoint.EIGHT, CardPattern.HEART)   // pobi 1번째
        )));

        BlackJackGame game = new BlackJackGame(players, dealer, deck);
        game.initDeal();

        List<String> callbackLog = new ArrayList<>();
        game.proceedDealerTurn(() -> callbackLog.add("카드받음"));

        assertThat(dealer.getCardCount()).isEqualTo(3);
        assertThat(callbackLog).hasSize(1);
        assertThat(dealer.isFinished()).isTrue();
    }

    @Test
    void 딜러가_17이상이면_카드를_받지_않는다() {
        Player pobi = createPlayer("pobi", 1000);
        Players players = new Players(List.of(pobi));
        Dealer dealer = new Dealer();

        // dealer: 10 + 7 = 17 → shouldDraw false
        Deck deck = new Deck(new FixedOrderShuffleStrategy(List.of(
                new Card(CardPoint.SEVEN, CardPattern.CLUB),   // dealer 2번째
                new Card(CardPoint.TEN, CardPattern.DIAMOND),  // dealer 1번째
                new Card(CardPoint.NINE, CardPattern.SPADE),   // pobi 2번째
                new Card(CardPoint.EIGHT, CardPattern.HEART)   // pobi 1번째
        )));

        BlackJackGame game = new BlackJackGame(players, dealer, deck);
        game.initDeal();

        List<String> callbackLog = new ArrayList<>();
        game.proceedDealerTurn(() -> callbackLog.add("카드받음"));

        assertThat(dealer.getCardCount()).isEqualTo(2);
        assertThat(callbackLog).isEmpty();
        assertThat(dealer.isFinished()).isTrue(); // stay 처리됨
    }

    // --- judgeGameResult 테스트 ---

    @Test
    void 플레이어_승리시_베팅금액만큼_수익을_받는다() {
        Player pobi = createPlayer("pobi", 10000);
        Players players = new Players(List.of(pobi));
        Dealer dealer = new Dealer();

        // pobi: 10 + 9 = 19, dealer: 10 + 7 = 17
        Deck deck = new Deck(new FixedOrderShuffleStrategy(List.of(
                new Card(CardPoint.SEVEN, CardPattern.CLUB),   // dealer 2번째
                new Card(CardPoint.TEN, CardPattern.DIAMOND),  // dealer 1번째
                new Card(CardPoint.NINE, CardPattern.SPADE),   // pobi 2번째
                new Card(CardPoint.TEN, CardPattern.HEART)     // pobi 1번째
        )));

        BlackJackGame game = new BlackJackGame(players, dealer, deck);
        game.initDeal();

        // 플레이어 stay, 딜러도 stay (17이상)
        game.proceedAllPlayersTurn(new FixedPlayerHitStrategy(false), p -> {
        });
        game.proceedDealerTurn(() -> {
        });

        FinalIncome result = game.judgeGameResult();

        assertThat(result.getIncomeOf(pobi)).isEqualTo(10000);
        assertThat(result.getDealerIncome()).isEqualTo(-10000);
    }

    @Test
    void 블랙잭_승리시_1점5배_수익을_받는다() {
        Player pobi = createPlayer("pobi", 10000);
        Players players = new Players(List.of(pobi));
        Dealer dealer = new Dealer();

        // pobi: A + K = 블랙잭, dealer: 10 + 9 = 19
        Deck deck = new Deck(new FixedOrderShuffleStrategy(List.of(
                new Card(CardPoint.NINE, CardPattern.CLUB),    // dealer 2번째
                new Card(CardPoint.TEN, CardPattern.DIAMOND),  // dealer 1번째
                new Card(CardPoint.KING, CardPattern.SPADE),   // pobi 2번째
                new Card(CardPoint.ACE, CardPattern.HEART)     // pobi 1번째
        )));

        BlackJackGame game = new BlackJackGame(players, dealer, deck);
        game.initDeal();

        // 블랙잭이라 이미 finished
        game.proceedAllPlayersTurn(new FixedPlayerHitStrategy(false), p -> {
        });
        game.proceedDealerTurn(() -> {
        });

        FinalIncome result = game.judgeGameResult();

        assertThat(result.getIncomeOf(pobi)).isEqualTo(15000);
        assertThat(result.getDealerIncome()).isEqualTo(-15000);
    }
}
