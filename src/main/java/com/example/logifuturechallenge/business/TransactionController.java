package com.example.logifuturechallenge.business;


import com.example.logifuturechallenge.model.Game;
import com.example.logifuturechallenge.model.TransactionType;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static lombok.AccessLevel.PRIVATE;

@RestController
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TransactionController {

    TransactionService transactionService;


    @PostMapping("/transaction/execute")
    public ResponseEntity<Game> executeTransaction(@RequestBody TransactionDto dto) throws Throwable {
        return ResponseEntity.ok(
                transactionService.processTransaction(dto.gameId, dto.user, dto.transactionType, dto.amount));
    }

    @GetMapping("/transaction/user/details")
    public ResponseEntity<String> getUserDetails(@RequestParam String username) {
        return ResponseEntity.ok(transactionService.getUserDetails(username));
    }


    public record TransactionDto(String gameId, String user, TransactionType transactionType, BigDecimal amount) {

    }
}
