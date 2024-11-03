package com.mcgill.ecse321.GameShop.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mcgill.ecse321.GameShop.exception.GameShopException;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.WishList;
import com.mcgill.ecse321.GameShop.repository.WishListRepository;

import jakarta.transaction.Transactional;

@Service
public class WishlistService {
    @Autowired
    private WishListRepository wishListRepository;

    public WishList findWishlistById(int id) {
        WishList wishList = wishListRepository.findById(id);
        if (wishList == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND,
                    String.format("There is no WishList with Id %d.",id));
        }
        return wishList;
    }
    @Transactional
    public WishList addGameToWishlist(int wishlistId, int gameId) {
        WishList wishList = findWishlistById(wishlistId);
        if (wishList == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND,
                    String.format("There is no WishList with Id %d.",wishlistId));
        }
        List<Game> games = wishList.getGames();
        for (Game game : games) {
            if (game.getGame_id() == gameId) {
                //Game already in wishlist
                return wishList;
            }
        }
        Game game = Game.getWithGame_id(gameId);
        wishList.addGame(game);
        return wishListRepository.save(wishList);
    }
    @Transactional
    public WishList removeGameFromWishlist(int wishlistId, int gameId) {
        WishList wishList = findWishlistById(wishlistId);
        if (wishList == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND,
                    String.format("There is no WishList with Id %d.",wishlistId));
        }
        List<Game> games = wishList.getGames();
        for (Game game : games) {
            if (game.getGame_id() == gameId) {
                wishList.removeGame(game);
                return wishListRepository.save(wishList);
            }
        }
        throw new GameShopException(HttpStatus.NOT_FOUND,
                String.format("There is no Game with Id %d in the WishList with Id %d.",gameId,wishlistId));
    }
    @Transactional
    public int getWishlistSize(int wishlistId) {
        WishList wishList = findWishlistById(wishlistId);
        if (wishList == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND,
                    String.format("There is no WishList with Id %d.",wishlistId));
        }
        return wishList.getGames().size();
    }
    @Transactional
    public Iterable<Game> getGamesInWishList(int wishlistId){
        WishList wishList = findWishlistById(wishlistId);
        if (wishList == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND,
                    String.format("There is no WishList with Id %d.",wishlistId));
        }
        return wishList.getGames();
    }
}