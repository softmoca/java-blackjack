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

        Deck deck = new Deck(new FixedOrderShuffleStrategy(List.of(
                new Card(CardPoint.NINE, CardPattern.CLUB),
                new Card(CardPoint.TEN, CardPattern.CLUB),
                new Card(CardPoint.SEVEN, CardPattern.HEART),
                new Card(CardPoint.SIX, CardPattern.HEART),
                new Card(CardPoint.THREE, CardPattern.SPADE),
                new Card(CardPoint.TWO, CardPattern.SPADE)
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

        Deck deck = new Deck(new FixedOrderShuffleStrategy(List.of(
                new Card(CardPoint.FIVE, CardPattern.CLUB),
                new Card(CardPoint.THREE, CardPattern.DIAMOND),
                new Card(CardPoint.KING, CardPattern.SPADE),
                new Card(CardPoint.ACE, CardPattern.HEART)
        )));

        BlackJackGame game = new BlackJackGame(players, dealer, deck);
        game.initDeal();

        assertThat(pobi.isBlackjack()).isTrue();
        assertThat(pobi.isFinished()).isTrue();
    }

    // --- proceedAllPlayersTurn 테스트 ---

    @Test
    void 플레이어가_HIT을_선택하면_카드를_한장_더_받는다() {
        Player pobi = createPlayer("pobi", 1000);
        Players players = new Players(List.of(pobi));
        Dealer dealer = new Dealer();

        Deck deck = new Deck(new FixedOrderShuffleStrategy(List.of(
                new Card(CardPoint.FOUR, CardPattern.CLUB),
                new Card(CardPoint.TEN, CardPattern.CLUB),
                new Card(CardPoint.NINE, CardPattern.CLUB),
                new Card(CardPoint.THREE, CardPattern.HEART),
                new Card(CardPoint.TWO, CardPattern.SPADE)
        )));

        BlackJackGame game = new BlackJackGame(players, dealer, deck);
        game.initDeal();

        PlayerHitStrategy strategy = new FixedPlayerHitStrategy(
                PlayerAction.HIT, PlayerAction.STAY);

        game.proceedAllPlayersTurn(strategy, p -> {
        });

        assertThat(pobi.getCardCount()).isEqualTo(3);
        assertThat(pobi.isFinished()).isTrue();
    }

    @Test
    void 플레이어가_STAY를_선택하면_카드를_받지_않는다() {
        Player pobi = createPlayer("pobi", 1000);
        Players players = new Players(List.of(pobi));
        Dealer dealer = new Dealer();

        Deck deck = new Deck(new FixedOrderShuffleStrategy(List.of(
                new Card(CardPoint.NINE, CardPattern.CLUB),
                new Card(CardPoint.TEN, CardPattern.CLUB),
                new Card(CardPoint.SIX, CardPattern.HEART),
                new Card(CardPoint.FIVE, CardPattern.SPADE)
        )));

        BlackJackGame game = new BlackJackGame(players, dealer, deck);
        game.initDeal();

        PlayerHitStrategy strategy = new FixedPlayerHitStrategy(PlayerAction.STAY);

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
                new Card(CardPoint.JACK, CardPattern.CLUB),
                new Card(CardPoint.THREE, CardPattern.CLUB),
                new Card(CardPoint.FOUR, CardPattern.CLUB),
                new Card(CardPoint.KING, CardPattern.CLUB),
                new Card(CardPoint.QUEEN, CardPattern.CLUB)
        )));

        BlackJackGame game = new BlackJackGame(players, dealer, deck);
        game.initDeal();

        PlayerHitStrategy strategy = new FixedPlayerHitStrategy(
                PlayerAction.HIT, PlayerAction.HIT, PlayerAction.HIT);

        game.proceedAllPlayersTurn(strategy, p -> {
        });

        assertThat(pobi.isBust()).isTrue();
        assertThat(pobi.getCardCount()).isEqualTo(3);
    }

    @Test
    void 플레이어가_카드를_받을때마다_콜백이_호출된다() {
        Player pobi = createPlayer("pobi", 1000);
        Players players = new Players(List.of(pobi));
        Dealer dealer = new Dealer();

        Deck deck = new Deck(new FixedOrderShuffleStrategy(List.of(
                new Card(CardPoint.TWO, CardPattern.DIAMOND),
                new Card(CardPoint.TWO, CardPattern.CLUB),
                new Card(CardPoint.TEN, CardPattern.CLUB),
                new Card(CardPoint.NINE, CardPattern.CLUB),
                new Card(CardPoint.THREE, CardPattern.HEART),
                new Card(CardPoint.TWO, CardPattern.SPADE)
        )));

        BlackJackGame game = new BlackJackGame(players, dealer, deck);
        game.initDeal();

        List<Player> receivedPlayers = new ArrayList<>();
        PlayerHitStrategy strategy = new FixedPlayerHitStrategy(
                PlayerAction.HIT, PlayerAction.HIT, PlayerAction.STAY);

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

        Deck deck = new Deck(new FixedOrderShuffleStrategy(List.of(
                new Card(CardPoint.TEN, CardPattern.HEART),
                new Card(CardPoint.FIVE, CardPattern.CLUB),
                new Card(CardPoint.THREE, CardPattern.DIAMOND),
                new Card(CardPoint.NINE, CardPattern.SPADE),
                new Card(CardPoint.EIGHT, CardPattern.HEART)
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

        Deck deck = new Deck(new FixedOrderShuffleStrategy(List.of(
                new Card(CardPoint.SEVEN, CardPattern.CLUB),
                new Card(CardPoint.TEN, CardPattern.DIAMOND),
                new Card(CardPoint.NINE, CardPattern.SPADE),
                new Card(CardPoint.EIGHT, CardPattern.HEART)
        )));

        BlackJackGame game = new BlackJackGame(players, dealer, deck);
        game.initDeal();

        List<String> callbackLog = new ArrayList<>();
        game.proceedDealerTurn(() -> callbackLog.add("카드받음"));

        assertThat(dealer.getCardCount()).isEqualTo(2);
        assertThat(callbackLog).isEmpty();
        assertThat(dealer.isFinished()).isTrue();
    }

    // --- judgeGameResult 테스트 ---

    @Test
    void 플레이어_승리시_베팅금액만큼_수익을_받는다() {
        Player pobi = createPlayer("pobi", 10000);
        Players players = new Players(List.of(pobi));
        Dealer dealer = new Dealer();

        Deck deck = new Deck(new FixedOrderShuffleStrategy(List.of(
                new Card(CardPoint.SEVEN, CardPattern.CLUB),
                new Card(CardPoint.TEN, CardPattern.DIAMOND),
                new Card(CardPoint.NINE, CardPattern.SPADE),
                new Card(CardPoint.TEN, CardPattern.HEART)
        )));

        BlackJackGame game = new BlackJackGame(players, dealer, deck);
        game.initDeal();

        game.proceedAllPlayersTurn(
                new FixedPlayerHitStrategy(PlayerAction.STAY), p -> {
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

        Deck deck = new Deck(new FixedOrderShuffleStrategy(List.of(
                new Card(CardPoint.NINE, CardPattern.CLUB),
                new Card(CardPoint.TEN, CardPattern.DIAMOND),
                new Card(CardPoint.KING, CardPattern.SPADE),
                new Card(CardPoint.ACE, CardPattern.HEART)
        )));

        BlackJackGame game = new BlackJackGame(players, dealer, deck);
        game.initDeal();

        game.proceedAllPlayersTurn(
                new FixedPlayerHitStrategy(PlayerAction.STAY), p -> {
                });
        game.proceedDealerTurn(() -> {
        });

        FinalIncome result = game.judgeGameResult();

        assertThat(result.getIncomeOf(pobi)).isEqualTo(15000);
        assertThat(result.getDealerIncome()).isEqualTo(-15000);
    }

    // --- 서렌더 테스트 ---

    @Test
    void 플레이어가_서렌더하면_베팅금액의_절반을_잃는다() {
        Player pobi = createPlayer("pobi", 10000);
        Players players = new Players(List.of(pobi));
        Dealer dealer = new Dealer();

        Deck deck = new Deck(new FixedOrderShuffleStrategy(List.of(
                new Card(CardPoint.SEVEN, CardPattern.CLUB),
                new Card(CardPoint.TEN, CardPattern.DIAMOND),
                new Card(CardPoint.SIX, CardPattern.HEART),
                new Card(CardPoint.TEN, CardPattern.SPADE)
        )));

        BlackJackGame game = new BlackJackGame(players, dealer, deck);
        game.initDeal();

        game.proceedAllPlayersTurn(
                new FixedPlayerHitStrategy(PlayerAction.SURRENDER), p -> {
                });
        game.proceedDealerTurn(() -> {
        });

        FinalIncome result = game.judgeGameResult();

        assertThat(pobi.isSurrender()).isTrue();
        assertThat(result.getIncomeOf(pobi)).isEqualTo(-5000);
        assertThat(result.getDealerIncome()).isEqualTo(5000);
    }

    @Test
    void 한명은_서렌더_한명은_승리할_수_있다() {
        Player pobi = createPlayer("pobi", 10000);
        Player jason = createPlayer("jason", 20000);
        Players players = new Players(List.of(pobi, jason));
        Dealer dealer = new Dealer();

        Deck deck = new Deck(new FixedOrderShuffleStrategy(List.of(
                new Card(CardPoint.SEVEN, CardPattern.CLUB),
                new Card(CardPoint.TEN, CardPattern.DIAMOND),
                new Card(CardPoint.NINE, CardPattern.HEART),
                new Card(CardPoint.TEN, CardPattern.HEART),
                new Card(CardPoint.SIX, CardPattern.SPADE),
                new Card(CardPoint.TEN, CardPattern.SPADE)
        )));

        BlackJackGame game = new BlackJackGame(players, dealer, deck);
        game.initDeal();

        game.proceedAllPlayersTurn(
                new FixedPlayerHitStrategy(
                        PlayerAction.SURRENDER, PlayerAction.STAY),
                p -> {
                });
        game.proceedDealerTurn(() -> {
        });

        FinalIncome result = game.judgeGameResult();

        assertThat(result.getIncomeOf(pobi)).isEqualTo(-5000);
        assertThat(result.getIncomeOf(jason)).isEqualTo(20000);
        assertThat(result.getDealerIncome()).isEqualTo(-15000);
    }
}
