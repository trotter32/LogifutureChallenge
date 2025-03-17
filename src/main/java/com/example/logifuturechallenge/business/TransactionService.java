package com.example.logifuturechallenge.business;

import com.example.logifuturechallenge.model.Game;
import com.example.logifuturechallenge.model.TransactionType;
import com.example.logifuturechallenge.model.Wallet;
import com.example.logifuturechallenge.repository.GameRepository;
import com.example.logifuturechallenge.repository.WalletRepository;
import com.example.logifuturechallenge.utils.AsyncService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.Callable;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class TransactionService {

    UserService userService;
    AsyncService asyncService;
    WalletRepository walletRepository;
    GameRepository gameRepository;
    TransactionValidator transactionValidator;


    public final Game processTransaction(String gameId, String username, TransactionType transactionType, BigDecimal amount) throws Throwable {
        var user = userService.generateUser(username);

        var game = asyncService.submitTask(user.getId(),
                processTransactionInternal(gameId, user.getId(), transactionType, amount));

        return game;
    }

    private Callable<Game> processTransactionInternal(String gameId, String userId, TransactionType transactionType, BigDecimal amount) {
        return () -> {
            var wallet = walletRepository.findByUserId(userId).orElseGet(() -> {
                var newWallet = new Wallet();
                newWallet.setUserId(userId);
                newWallet.setBalance(BigDecimal.valueOf(1000));
                return newWallet;
            });

            Game game;
            if (gameId == null) {
                game = generateGame(userId);
            } else {
                game = gameRepository.findById(gameId).orElseGet(() -> generateGame(userId));
            }

            var round = game.getRound();

            switch (transactionType) {
                case DEBIT -> {
                    transactionValidator.validateDebit(amount, wallet, round);
                    wallet.setBalance(wallet.getBalance().subtract(amount));
                    round.setDebitDone(true);
                }
                case CREDIT -> {
                    transactionValidator.validateCredit(round);
                    wallet.setBalance(wallet.getBalance().add(amount));
                    var newRound = new Game.Round();
                    newRound.setRoundNumber(round.getRoundNumber() + 1);
                    game.setRound(newRound);
                }
            }

            gameRepository.save(game);
            walletRepository.save(wallet);

            return game;
        };
    }

    private Game generateGame(String userId) {
        var newGame = new Game();
        newGame.setUserId(userId);
        return newGame;
    }

    public String getUserDetails(String username) {
        var user = userService.getUser(username);
        var userId = user.getId();
        var wallet = walletRepository.findByUserId(userId).orElseThrow(RuntimeException::new);
        var games = gameRepository.findByUserId(userId);

        var builder = new StringBuilder();
        builder.append("User with username ")
                .append(username)
                .append(", currently has balance of ")
                .append(wallet.getBalance())
                .append(", and is playing following games: ");


        games.forEach(game -> {
            var round = game.getRound();
            builder.append("\n Game id: ").append(game.getId()).append(", round: ").append(round.getRoundNumber());

            if(round.isDebitDone()) {
                builder.append(", with debit done for the round.");
            } else {
                builder.append(", and no plays done this round.");
            }

        });

        return builder.toString();
    }
}
