package com.example.logifuturechallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableMongoRepositories
@EnableScheduling
public class LogifutureChallengeApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogifutureChallengeApplication.class, args);
    }
}
