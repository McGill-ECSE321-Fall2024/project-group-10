package com.mcgill.ecse321.GameShop.repository;



import org.springframework.data.repository.CrudRepository;


import com.mcgill.ecse321.GameShop.model.Game;

public interface GameRepository extends CrudRepository<Game, Integer> {
   public Game findById(int game_id);
}