package com.example.logifuturechallenge.business;

import com.example.logifuturechallenge.model.Game;
import com.example.logifuturechallenge.model.Wallet;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.StandardException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static lombok.AccessLevel.PRIVATE;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class TransactionValidator {

    public void validateDebit(BigDecimal amount, Wallet wallet, Game.Round round) {
        if (amount.compareTo(wallet.getBalance()) > 0) {
            throw new ValidationException("Balance not high enough!");
        }
        if (round.isDebitDone()) {
            throw new ValidationException("You can only play one Debit Transaction per round!");
        }
    }

    public void validateCredit(Game.Round round) {
        if (!round.isDebitDone()) {
            throw new ValidationException("You need to execute a Debit Transaction this round!");
        }
    }

    @StandardException
    public static class ValidationException extends RuntimeException {

    }
}
