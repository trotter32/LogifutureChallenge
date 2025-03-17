package com.example.logifuturechallenge.business;


import com.example.logifuturechallenge.model.User;
import com.example.logifuturechallenge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;


    public User generateUser(String username) {
        return userRepository.findByUsername(username)
                .orElseGet(() -> {
                    var newUser = new User();
                    newUser.setUsername(username);
                    userRepository.save(newUser);
                    return newUser;
                });
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new TransactionValidator.ValidationException("User doesn't exist!"));
    }
}
