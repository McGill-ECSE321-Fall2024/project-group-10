package com.mcgill.ecse321.GameShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.mcgill.ecse321.GameShop.dto.GameDto.GameListDto;
import com.mcgill.ecse321.GameShop.dto.GameDto.GameResponseDto;
import com.mcgill.ecse321.GameShop.dto.WishListDto.WishListResponseDto;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.WishList;
import com.mcgill.ecse321.GameShop.service.GameService;
import com.mcgill.ecse321.GameShop.service.WishListService;

public class WishListController {
    
    @Autowired
    private WishListService wishListService;
    @Autowired
    private GameService gameService;
    
    @GetMapping("/wishlist/{wishlist_id}")
    public WishListResponseDto findWishlistById(@PathVariable int wishlist_id) {
        WishList wishList = wishListService.findWishlistById(wishlist_id);
        return WishListResponseDto.create(wishList);
    }

    @PutMapping("/wishlist/{wishlist_id}/{game_id}")
    public WishListResponseDto addGameToWishlist(@PathVariable int wishlist_id, @PathVariable int game_id) {
        Game game = gameService.findGameById(game_id);
        WishList wishList = wishListService.findWishlistById(wishlist_id);
        wishList.addGame(game);
        return WishListResponseDto.create(wishList);
    }
    @DeleteMapping("/wishlist/{wishlist_id}/{game_id}")
    public WishListResponseDto removeGameFromWishlist(@PathVariable int wishlist_id, @PathVariable int game_id) {
        Game game = gameService.findGameById(game_id);
        WishList wishList = wishListService.findWishlistById(wishlist_id);
        wishList.removeGame(game);
        return WishListResponseDto.create(wishList);
    }
    @PutMapping("/wishlist/{wishlist_id}")
    public WishListResponseDto removeAllGamesFromWishlist(@PathVariable int wishlist_id) {
        WishList wishList = wishListService.findWishlistById(wishlist_id);
        wishList = wishListService.removeAllGamesFromWishlist(wishlist_id);
        return WishListResponseDto.create(wishList);
    }
    @GetMapping("/wishlist/{wishlist_id}/")
    public GameListDto getAllGamesInWishlist(@PathVariable int wishlist_id) {
        WishList wishList = wishListService.findWishlistById(wishlist_id);
        WishListResponseDto wishListResponseDto = new WishListResponseDto(wishList);
        return wishListResponseDto.getGames();
        
    }
    @GetMapping("/wishlist/{wishlist_id}/{game_id}")
    public GameResponseDto getGameInWishList(@PathVariable int wishlist_id, @PathVariable int game_id) {
        Game game = wishListService.getGameInWishList(wishlist_id,game_id);
        return new GameResponseDto(game);
    }
}
