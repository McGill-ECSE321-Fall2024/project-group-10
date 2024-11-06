package com.mcgill.ecse321.GameShop.controller;

import com.mcgill.ecse321.GameShop.dto.CartDto.*;
import com.mcgill.ecse321.GameShop.model.Cart;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.*;

// createcart, addgametocart, getallgamesformcart, getcustomerid, getgamefromcart, set specific game(s)
public class CartController {

    @Autowired
    private CartService cartService;

    // Create a new cart
    @PostMapping("/carts")
    public CartResponseDto createCart() {
        Cart cart = cartService.createCart();
        return buildCartResponseDto(cart);
    }

    @PostMapping("/carts/{cartId}/games")
    public CartResponseDto addGamesToCart(
            @PathVariable int cartId,
            @Valid @RequestBody CartRequestDto requestDto) {
        int gameId = requestDto.getGameId();
        int quantity = requestDto.getQuantity();

        cartService.addGameToCart(cartId, gameId, quantity);

        Cart cart = cartService.getCartById(cartId);
        return buildCartResponseDto(cart);
    }

    @PostMapping("/carts/{cartId}/games/remove")
    public CartResponseDto removeGamesFromCart(
            @PathVariable int cartId,
            @Valid @RequestBody CartRequestDto requestDto) {
        int gameId = requestDto.getGameId();
        int quantity = requestDto.getQuantity();

        cartService.removeGameFromCart(cartId, gameId, quantity);

        Cart cart = cartService.getCartById(cartId);
        return buildCartResponseDto(cart);
    }

    @GetMapping("/carts/{cartId}")
    public CartResponseDto findCartById(@PathVariable int cartId) {
        Cart cart = cartService.getCartById(cartId);
        return buildCartResponseDto(cart);
    }

    @GetMapping("/carts")
    public CartListDto findAllCarts() {
        List<CartSummaryDto> cartSummaries = new ArrayList<>();
        for (Cart cart : cartService.getAllCarts()) {
            Map<Integer, Integer> quantities = cartService.getQuantitiesForCart(cart.getCart_id());
            int totalItems = quantities.values().stream().mapToInt(Integer::intValue).sum();

            // Calculate total price for the cart
            int totalPrice = 0;
            for (Game game : cart.getGames()) {
                int gameId = game.getGame_id();
                int quantity = quantities.getOrDefault(gameId, 1);
                totalPrice += game.getPrice() * quantity;
            }

            cartSummaries.add(new CartSummaryDto(cart.getCart_id(), totalItems, totalPrice));
        }
        return new CartListDto(cartSummaries);
    }

    // Helper method to build CartResponseDto
    private CartResponseDto buildCartResponseDto(Cart cart) {
        Map<Integer, Integer> quantities = cartService.getQuantitiesForCart(cart.getCart_id());
        int totalItems = quantities.values().stream().mapToInt(Integer::intValue).sum();

        // Build a list of maps containing game details and quantities
        List<Map<String, Object>> games = new ArrayList<>();
        int totalPrice = 0;

        for (Game game : cart.getGames()) {
            int gameId = game.getGame_id();
            int quantity = quantities.getOrDefault(gameId, 1);

            Map<String, Object> gameInfo = new HashMap<>();
            gameInfo.put("gameId", game.getGame_id());
            gameInfo.put("title", game.getTitle());
            gameInfo.put("description", game.getDescription());
            gameInfo.put("price", game.getPrice());
            gameInfo.put("photoUrl", game.getPhotoUrl());
            gameInfo.put("gameStatus", game.getGameStatus().toString());
            gameInfo.put("stockQuantity", game.getStockQuantity());
            gameInfo.put("quantity", quantity);

            totalPrice += game.getPrice() * quantity;

            games.add(gameInfo);
        }

        return new CartResponseDto(cart.getCart_id(), games, totalItems, totalPrice);
    }

    @PostMapping("/carts/{cartId}/clear")
    public CartResponseDto clearCart(@PathVariable int cartId) {
        Cart cart = cartService.clearCart(cartId);
        return buildCartResponseDto(cart);
    }

    @PostMapping("/carts/{cartId}/games/updateQuantity")
    public CartResponseDto updateGameQuantityInCart(
            @PathVariable int cartId,
            @Valid @RequestBody CartRequestDto requestDto) {
        int gameId = requestDto.getGameId();
        int quantity = requestDto.getQuantity();

        Cart cart = cartService.updateGameQuantityInCart(cartId, gameId, quantity);
        return buildCartResponseDto(cart);
    }

}