package com.mcgill.ecse321.GameShop.controller;

import com.mcgill.ecse321.GameShop.dto.CartDto.*;
import com.mcgill.ecse321.GameShop.dto.GameDto.GameListDto;
import com.mcgill.ecse321.GameShop.dto.GameDto.GameResponseDto;
import com.mcgill.ecse321.GameShop.dto.GameDto.GameSummaryDto;
import com.mcgill.ecse321.GameShop.exception.GameShopException;
import com.mcgill.ecse321.GameShop.model.Cart;
import com.mcgill.ecse321.GameShop.model.Customer;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.service.CartService;
import com.mcgill.ecse321.GameShop.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private AccountService customerService;

    // Get the cart associated with a customer
    @GetMapping("/carts/customer/{customerEmail}")
    public CartResponseDto getCartByCustomerEmail(@PathVariable String customerEmail) {
        Cart cart = cartService.getCartByCustomerEmail(customerEmail);

        Map<Integer, Integer> quantities = cartService.getQuantitiesForCart(cart.getCart_id());
        return CartResponseDto.create(cart, quantities);
    }

    // Add a game to the cart with quantity
    @PostMapping("/carts/{cartId}/games")
    public CartResponseDto addGameToCart(
            @PathVariable int cartId,
            @Valid @RequestBody CartRequestDto requestDto) {
        int gameId = requestDto.getGameId();
        int quantity = requestDto.getQuantity();

        cartService.addGameToCart(cartId, gameId, quantity);

        Cart cart = cartService.getCartById(cartId);
        Map<Integer, Integer> quantities = cartService.getQuantitiesForCart(cartId);
        return CartResponseDto.create(cart, quantities);
    }

    // Remove a game from the cart with quantity
    @PostMapping("/carts/{cartId}/games/remove")
    public CartResponseDto removeGameFromCart(
            @PathVariable int cartId,
            @Valid @RequestBody CartRequestDto requestDto) {
        int gameId = requestDto.getGameId();
        int quantity = requestDto.getQuantity();

        cartService.removeGameFromCart(cartId, gameId, quantity);

        Cart cart = cartService.getCartById(cartId);
        Map<Integer, Integer> quantities = cartService.getQuantitiesForCart(cartId);
        return CartResponseDto.create(cart, quantities);
    }

    // Get a specific cart by ID
    @GetMapping("/carts/{cartId}")
    public CartResponseDto findCartById(@PathVariable int cartId) {
        Cart cart = cartService.getCartById(cartId);
        Map<Integer, Integer> quantities = cartService.getQuantitiesForCart(cartId);
        return CartResponseDto.create(cart, quantities);
    }

    // Get all carts
    @GetMapping("/carts")
    public CartListDto findAllCarts() {
        List<Cart> carts = (List<Cart>) cartService.getAllCarts();
        List<CartSummaryDto> cartSummaries = carts.stream()
                .map(cart -> {
                    int cartId = cart.getCart_id();
                    Map<Integer, Integer> quantities = cartService.getQuantitiesForCart(cartId);
                    int totalItems = quantities.values().stream().mapToInt(Integer::intValue).sum();
                    int totalPrice = cart.getGames().stream()
                            .mapToInt(game -> game.getPrice() * quantities.getOrDefault(game.getGame_id(), 1))
                            .sum();
                    return new CartSummaryDto(cart, totalItems, totalPrice);
                })
                .collect(Collectors.toList());
        return new CartListDto(cartSummaries);
    }

    // Update the quantity of a specific game in the cart
    @PutMapping("/carts/{cartId}/games/{gameId}/quantity")
    public CartResponseDto updateGameQuantityInCart(
            @PathVariable int cartId,
            @PathVariable int gameId,
            @Valid @RequestBody CartRequestDto requestDto) {
        int quantity = requestDto.getQuantity();

        cartService.updateGameQuantityInCart(cartId, gameId, quantity);

        Cart cart = cartService.getCartById(cartId);
        Map<Integer, Integer> quantities = cartService.getQuantitiesForCart(cartId);
        return CartResponseDto.create(cart, quantities);
    }

    // Clear all games from the cart
    @PostMapping("/carts/{cartId}/clear")
    public CartResponseDto clearCart(@PathVariable int cartId) {
        cartService.clearCart(cartId);
        Cart cart = cartService.getCartById(cartId);
        Map<Integer, Integer> quantities = cartService.getQuantitiesForCart(cartId);
        return CartResponseDto.create(cart, quantities);
    }

    // Get a specific game from the cart
    @GetMapping("/carts/{cartId}/games/{gameId}")
    public GameResponseDto getGameFromCart(
            @PathVariable int cartId,
            @PathVariable int gameId) {
        Game game = cartService.getGameFromCart(cartId, gameId);
        return GameResponseDto.create(game);
    }

    // Get all games from the cart
    @GetMapping("/carts/{cartId}/games")
    public GameListDto getGamesInCart(@PathVariable int cartId) {
        Map<Game, Integer> gamesWithQuantities = cartService.getAllGamesFromCartWithQuantities(cartId);
        List<GameSummaryDto> gameSummaries = gamesWithQuantities.entrySet().stream()
                .map(entry -> new GameSummaryDto(entry.getKey()))
                .collect(Collectors.toList());
        return new GameListDto(gameSummaries);
    }
}