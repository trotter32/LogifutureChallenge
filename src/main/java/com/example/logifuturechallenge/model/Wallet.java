package com.example.logifuturechallenge.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Data
public class Wallet {
    @Id
    String id;

    String userId;

    BigDecimal balance;
}
