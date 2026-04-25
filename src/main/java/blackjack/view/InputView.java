package blackjack.view;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class InputView {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String lineSeparator = System.lineSeparator();

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
