// OrderService.java
package com.mcgill.ecse321.GameShop.service;

import com.mcgill.ecse321.GameShop.exception.GameShopException;
import com.mcgill.ecse321.GameShop.model.*;
import com.mcgill.ecse321.GameShop.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private AccountService customerService;

    @Autowired
    private SpecificGameService specificGameService;

    @Autowired
    private GameService gameService;

    @Transactional
    public Order createOrder(Date orderDate, String note, int paymentCard, String customerEmail) {
        Customer customer = customerRepository.findByEmail(customerEmail);
        if (customer == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Customer not found");
        }

        Cart cart = customerService.getCustomerAccountByEmail(customerEmail).getCart();
        if (cart == null || cart.getGames().isEmpty()) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Cart is empty");
        }

        Order order = new Order(orderDate, note, paymentCard, customer);
        order = orderRepository.save(order);

        // Transfer games from cart to order by creating SpecificGame instances
        List<Game> gamesInCart = cart.getGames();
        Map<Integer, Integer> quantities = cartService.getQuantitiesForCart(cart.getCart_id());
        for (Game game : gamesInCart) {
            int gameId = game.getGame_id();
            int quantity = quantities.getOrDefault(gameId, 0);

            if (game.getStockQuantity() < quantity) {
                throw new GameShopException(HttpStatus.BAD_REQUEST,
                        String.format("Insufficient stock for game ID %d", gameId));
            }

            // Create SpecificGame instances and associate them with the order
            for (int i = 0; i < quantity; i++) {
                SpecificGame specificGame = specificGameService.createSpecificGame(game);
                specificGame.addOrder(order);
            }
            game.setStockQuantity(game.getStockQuantity() - quantity);
            gameService.updateGameStockQuantity(game.getGame_id(), game.getStockQuantity());
        }
        cartService.clearCart(cart.getCart_id());

        return order;
    }

    @Transactional
    public Order getOrderById(String trackingNumber) {
        Order order = orderRepository.findByTrackingNumber(trackingNumber);
        if (order == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Order not found");
        }
        return order;
    }

    @Transactional
    public List<Order> getAllOrders() {
        return (List<Order>) orderRepository.findAll();
    }

    @Transactional
    public Order updateOrder(String trackingNumber, Date orderDate, String note, int paymentCard) {
        Order order = orderRepository.findByTrackingNumber(trackingNumber);
        if (order == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Order not found");
        }
        order.setOrderDate(orderDate);
        order.setNote(note);
        order.setPaymentCard(paymentCard);
        return orderRepository.save(order);
    }

    @Transactional
    public void addGameToOrder(String trackingNumber, int gameId, int quantity) {
        Order order = getOrderById(trackingNumber);
        Game game = gameService.findGameById(gameId);
        if (game == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Game not found");
        }
        if (game.getStockQuantity() < quantity) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Insufficient stock");
        }
        for (int i = 0; i < quantity; i++) {
            SpecificGame specificGame = specificGameService.createSpecificGame(game);
            specificGame.addOrder(order);
        }
        game.setStockQuantity(game.getStockQuantity() - quantity);
        gameService.updateGameStockQuantity(game.getGame_id(), game.getStockQuantity());
        orderRepository.save(order);
    }

    @Transactional
    public List<SpecificGame> getSpecificGamesByOrder(String trackingNumber) {
        Order order = getOrderById(trackingNumber);
        if (order == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Order not found");
        }

        List<SpecificGame> specificGames = new ArrayList<>();
        for (SpecificGame specificGame : specificGameService.getAllSpecificGames()) {
            if (specificGame.getOrder().contains(order)) {
                specificGames.add(specificGame);
            }
        }
        return specificGames;
    }

    @Transactional
    public void returnGame(String trackingNumber, int specificGameId) {
        SpecificGame specificGame = specificGameService.findSpecificGameById(specificGameId);
        if (specificGame == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "SpecificGame not found");
        }
        if (!getSpecificGamesByOrder(trackingNumber).contains(specificGame)) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Game not in order");
        }
        specificGame.setItemStatus(SpecificGame.ItemStatus.Returned);
        specificGameService.updateSpecificGame(specificGame.getSpecificGame_id(), specificGame.getGames().getGame_id());

        Game game = specificGame.getGames();
        game.setStockQuantity(game.getStockQuantity() + 1);
        gameService.updateGameStockQuantity(game.getGame_id(), game.getStockQuantity());
    }
}