package com.example.logifuturechallenge.repository;

import com.example.logifuturechallenge.model.User;
import com.example.logifuturechallenge.model.Wallet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends CrudRepository<Wallet, String> {

    Optional<Wallet> findByUserId(@Param("userId") String userId);
}
