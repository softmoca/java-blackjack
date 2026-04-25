package blackjack.repository;

import blackjack.domain.game.BlackJackGame;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class BlackJackGameRepository {

    private final Map<Long, BlackJackGame> store = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    public BlackJackGame save(BlackJackGame game) {
        store.put(game.getId(), game);
        return game;
    }

    public BlackJackGame findById(Long gameId) {
        BlackJackGame game = store.get(gameId);
        if (game == null) {
            throw new IllegalArgumentException("존재하지 않는 게임입니다. id=" + gameId);
        }
        return game;
    }

    public Long nextId() {
        return idGenerator.incrementAndGet();
    }
}
