package com.example.logifuturechallenge.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Game {

    @Id
    String id;

    String userId;

    Round round = new Round();


    @Data
    public static class Round {

        Integer roundNumber = 1;

        boolean debitDone = false;
    }
}
