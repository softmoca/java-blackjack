package blackjack.view;

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

    // === 새로 추가된 원시 값 반환 API ===

    public List<String> readPlayerNames() {
        System.out.println("게임에 참여할 사람의 이름을 입력하세요.(쉼표 기준으로 분리)");
        return Arrays.stream(scanner.nextLine().split(","))
                .map(String::trim)
                .toList();
    }

    public int readBetAmount(String name) {
        System.out.println(lineSeparator + name + "의 배팅 금액은?");
        return Integer.parseInt(scanner.nextLine().trim());
    }

    // === 기존 API (당분간 유지) ===

    public Players readPlayers() {
        System.out.println("게임에 참여할 사람의 이름을 입력하세요.(쉼표 기준으로 분리)");
        String input = scanner.nextLine();
        List<String> names = Arrays.asList(input.split(","));

        List<Player> players = names.stream()
                .map(String::trim)
                .map(name -> new Player(new Name(name), readBetAmountLegacy(name)))
                .collect(Collectors.toList());
        return new Players(players);
    }

    private BetAmount readBetAmountLegacy(String name) {
        System.out.println(lineSeparator + name + "의 배팅 금액은?");
        return new BetAmount(Integer.parseInt(scanner.nextLine().trim()));
    }

    // === 히트 여부 입력 (변경 없음) ===

    public boolean readHitAnswer(String name) {
        System.out.printf(lineSeparator + "%s는 한장의 카드를 더 받겠습니까?(예는 y, 아니오는 n)" + lineSeparator, name);
        String input = scanner.nextLine();
        validateHitAnswer(input);

        return input.equals("y");
    }

    private void validateHitAnswer(String input) {
        if (!input.equals("y") && !input.equals("n")) {
            throw new IllegalArgumentException("[ERROR] y 또는 n로 입력해주세요.");
        }
    }

}
