package com.mcgill.ecse321.GameShop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mcgill.ecse321.GameShop.exception.GameShopException;
import com.mcgill.ecse321.GameShop.model.WishList;
import com.mcgill.ecse321.GameShop.repository.WishListRepository;

import jakarta.transaction.Transactional;

@Service
public class WishlistService {
    @Autowired
    private WishListRepository wishListRepository;

    @Transactional
    public WishList findWishlistById(int id) {
        WishList wishList = wishListRepository.findById(id);
        if (wishList == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND,
                    String.format("There is no evend with ID %d.",id));
        }
        return wishList;
    }
}
