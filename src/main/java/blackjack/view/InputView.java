package blackjack.view;

import blackjack.domain.game.PlayerAction;
import blackjack.domain.participant.BetAmount;
import blackjack.domain.participant.Name;
import blackjack.domain.participant.Player;
import blackjack.domain.participant.Players;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class InputView {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String lineSeparator = System.lineSeparator();

    public Players readPlayers() {
        System.out.println("게임에 참여할 사람의 이름을 입력하세요.(쉼표 기준으로 분리)");
        String input = scanner.nextLine();
        List<String> names = Arrays.asList(input.split(","));

        List<Player> players = names.stream()
                .map(String::trim)
                .map(name -> new Player(new Name(name), readBetAmount(name)))
                .collect(Collectors.toList());
        return new Players(players);
    }

    private BetAmount readBetAmount(String name) {
        System.out.println(lineSeparator + name + "의 배팅 금액은?");
        return new BetAmount(Integer.parseInt(scanner.nextLine().trim()));
    }

    public PlayerAction readPlayerAction(String name) {
        System.out.printf(lineSeparator + "%s는 한장의 카드를 더 받겠습니까?(예는 y, 아니오는 n, 서렌더는 s)" + lineSeparator, name);
        String input = scanner.nextLine();
        return PlayerAction.from(input);
    }
}
