package com.mcgill.ecse321.GameShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.mcgill.ecse321.GameShop.dto.WishListDto.WishListResponseDto;
import com.mcgill.ecse321.GameShop.model.WishList;
import com.mcgill.ecse321.GameShop.service.WishListService;

public class WishListController {
    
    @Autowired
    private WishListService wishListService;
    
    @GetMapping("/wishlist/{wishlist_id}")
    public WishListResponseDto findWishlistById(@PathVariable int wishlist_id) {
        WishList wishList = wishListService.findWishlistById(wishlist_id);
        return WishListResponseDto.create(wishList);
    }
    //TODO: Implement POST mapping for adding a game to a wishlist
    //TODO: Implement DELETE mapping for removing a game from a wishlist
    //TODO: Implement Get all games in a wishlist
    
}
