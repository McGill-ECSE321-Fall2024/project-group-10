package com.mcgill.ecse321.GameShop.repository;

import org.springframework.data.repository.CrudRepository;

import com.mcgill.ecse321.GameShop.model.WishList;

public interface WishListRepository extends CrudRepository<WishList, Integer> {
   public WishList findByID(int wishList_id);
}