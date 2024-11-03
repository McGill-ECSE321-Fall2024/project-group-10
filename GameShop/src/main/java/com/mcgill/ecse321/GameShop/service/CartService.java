package com.mcgill.ecse321.GameShop.service;

import com.mcgill.ecse321.GameShop.exception.GameShopException;
import com.mcgill.ecse321.GameShop.model.Cart;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.repository.CartRepository;
import com.mcgill.ecse321.GameShop.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private GameRepository gameRepository;

    // In-memory map to track quantities per cart - Key: cartId, Value: Map of
    // gameId to quantity
    private Map<Integer, Map<Integer, Integer>> cartQuantities = new ConcurrentHashMap<>();

    @Transactional
    public Cart getCartById(int cartId) {
        Cart cart = cartRepository.findById(cartId);
        if (cart == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND,
                    String.format("Cart with ID %d does not exist", cartId));
        }
        // Initialize quantities map for this cart if not present
        cartQuantities.computeIfAbsent(cartId, k -> new ConcurrentHashMap<>());
        return cart;
    }

    @Transactional
    public Cart createCart() {
        Cart cart = new Cart();
        cartRepository.save(cart);
        cartQuantities.put(cart.getCart_id(), new ConcurrentHashMap<>());
        return cart;
    }

    @Transactional
    public Cart addGameToCart(int cartId, int gameId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            quantity = 1;
        }
        Cart cart = getCartById(cartId);
        Game game = gameRepository.findById(gameId);
        if (game == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND,
                    String.format("Game with ID %d does not exist", gameId));
        }

        if (!game.getGameStatus().equals(Game.GameStatus.InStock)) {
            throw new GameShopException(HttpStatus.BAD_REQUEST,
                    String.format("Game with ID %d is not available for purchase", gameId));
        }
        Map<Integer, Integer> quantities = cartQuantities.computeIfAbsent(cart.getCart_id(),
                k -> new ConcurrentHashMap<>());
        int currentQuantity = quantities.getOrDefault(gameId, 0);
        int newQuantity = currentQuantity + quantity;
        if (newQuantity > game.getStockQuantity()) {
            throw new GameShopException(HttpStatus.BAD_REQUEST,
                    String.format("Only %d units of Game ID %d are available", game.getStockQuantity(), gameId));
        }
        if (currentQuantity == 0) {
            cart.addGame(game);
        }
        quantities.put(gameId, newQuantity);
        cartRepository.save(cart);

        return cart;
    }

    @Transactional
    public Cart removeGameFromCart(int cartId, int gameId, int quantity) {
        if (quantity <= 0) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Quantity must be at least 1.");
        }

        Cart cart = getCartById(cartId);

        Game game = gameRepository.findById(gameId);
        if (game == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND,
                    String.format("Game with ID %d does not exist", gameId));
        }

        Map<Integer, Integer> quantities = cartQuantities.get(cart.getCart_id());
        if (quantities == null || !quantities.containsKey(gameId)) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Game is not in the cart.");
        }

        int newQuantity = quantities.get(gameId) - quantity;

        if (newQuantity > 0) {
            quantities.put(gameId, newQuantity);
        } else if (newQuantity == 0) {
            quantities.remove(gameId);
            cart.removeGame(game);
            cartRepository.save(cart);
        } else {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Cannot remove more than the existing quantity.");
        }

        return cart;
    }

    @Transactional
    public Cart updateGameQuantityInCart(int cartId, int gameId, int quantity) {
        if (quantity < 0) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Quantity must be 0 or greater.");
        }
        Cart cart = getCartById(cartId);
        Game game = gameRepository.findById(gameId);

        if (game == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND,
                    String.format("Game with ID %d does not exist", gameId));
        }

        if (!game.getGameStatus().equals(Game.GameStatus.InStock)) {
            throw new GameShopException(HttpStatus.BAD_REQUEST,
                    String.format("Game with ID %d is not available for purchase", gameId));
        }

        Map<Integer, Integer> quantities = cartQuantities.get(cart.getCart_id());
        if (quantities == null || !quantities.containsKey(gameId)) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Game is not in the cart.");
        }

        if (quantity == 0) {
            cart.removeGame(game);
            quantities.remove(gameId);
            cartRepository.save(cart);
        } else {
            if (quantity > game.getStockQuantity()) {
                throw new GameShopException(HttpStatus.BAD_REQUEST,
                        String.format("Only %d units of Game ID %d are available", game.getStockQuantity(), gameId));
            }
            quantities.put(gameId, quantity);
        }

        return cart;
    }

    @Transactional
    public Map<Integer, Integer> getQuantitiesForCart(int cartId) {
        Map<Integer, Integer> quantities = cartQuantities.get(cartId);
        if (quantities == null) {
            return Collections.emptyMap();
        }
        return new HashMap<>(quantities);
    }

    @Transactional
    public Iterable<Cart> getAllCarts() {
        return cartRepository.findAll();
    }
}
