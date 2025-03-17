package com.example.logifuturechallenge.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
public class User {
    @Id
    String id;

    @Indexed(unique = true)
    String username;
}
