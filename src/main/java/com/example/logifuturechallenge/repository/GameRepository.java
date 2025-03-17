package com.example.logifuturechallenge.repository;

import com.example.logifuturechallenge.model.Game;
import com.example.logifuturechallenge.model.Wallet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends CrudRepository<Game, String> {

    Optional<Game> findById(@Param("id") String id);
    List<Game> findByUserId(@Param("userId") String userId);
}
