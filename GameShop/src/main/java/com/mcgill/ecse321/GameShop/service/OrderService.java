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
    private GameRepository gameRepository;

    @Autowired
    private SpecificGameService specificGameService;

    @Autowired
    private GameService gameService;

    @Transactional
    public Order createOrder(Date orderDate, String note, int paymentCard, String customerEmail) {
        if (customerEmail == null || customerEmail.trim().isEmpty()) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Customer email cannot be empty or null");
        }

        Customer customer = customerRepository.findByEmail(customerEmail);
        if (customer == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Customer not found");
        }

        Cart cart = customer.getCart();
        if (cart == null || cart.getGames().isEmpty()) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Cart is empty");
        }

        Order order = new Order(orderDate, note, paymentCard, customer);
        List<Game> gamesInCart = cart.getGames();
        Map<Integer, Integer> quantities = cartService.getQuantitiesForCart(cart.getCart_id());

        for (Game game : gamesInCart) {
            int gameId = game.getGame_id();
            int quantity = quantities.getOrDefault(gameId, 0);

            if (game.getStockQuantity() < quantity) {
                throw new GameShopException(HttpStatus.BAD_REQUEST,
                        String.format("Insufficient stock for game ID %d", gameId));
            }

            // Retrieve the SpecificGame instances
            Iterable<SpecificGame> specificGames = specificGameService.getSpecificGamesByGameId(game.getGame_id());
            List<SpecificGame> specificGameList = new ArrayList<>();
            specificGames.forEach(specificGameList::add);

            if (specificGameList.size() < quantity) {
                throw new GameShopException(HttpStatus.BAD_REQUEST,
                        "Not enough SpecificGame instances available for game ID " + game.getGame_id());
            }

            // Add each SpecificGame instance to the order up to the quantity required
            for (int i = 0; i < quantity; i++) {
                SpecificGame specificGame = specificGameList.get(i);
                specificGame.addOrder(order);
            }

            // Update the game stock after adding all required SpecificGames
            game.setStockQuantity(game.getStockQuantity() - quantity);
            gameService.updateGameStockQuantity(game.getGame_id(), game.getStockQuantity());
        }

        // Clear the cart and save the order
        cartService.clearCart(cart.getCart_id());
        return orderRepository.save(order);
    }

    @Transactional
    public Order getOrderByTrackingNumber(String trackingNumber) {
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
        // Retrieve the order by tracking number and validate
        Order order = getOrderByTrackingNumber(trackingNumber);
        if (order == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Order not found");
        }

        // Retrieve the game by ID and validate
        Game game = gameRepository.findById(gameId);
        if (game == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Game not found");
        }

        // Check if the game's stock is sufficient for the quantity requested
        if (game.getStockQuantity() < quantity) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Insufficient stock");
        }

        // Retrieve all SpecificGame instances for the game and ensure sufficient
        // quantity
        Iterable<SpecificGame> specificGames = specificGameService.getSpecificGamesByGameId(game.getGame_id());
        List<SpecificGame> specificGameList = new ArrayList<>();
        specificGames.forEach(specificGameList::add);

        if (specificGameList.size() < quantity) {
            throw new GameShopException(HttpStatus.BAD_REQUEST,
                    "Not enough SpecificGame instances available for game ID " + game.getGame_id());
        }

        // Now add each SpecificGame instance to the order
        for (int i = 0; i < quantity; i++) {
            SpecificGame specificGame = specificGameList.get(i);
            specificGame.addOrder(order);
        }

        // Update stock quantity for the game and save
        game.setStockQuantity(game.getStockQuantity() - quantity);
        gameService.updateGameStockQuantity(game.getGame_id(), game.getStockQuantity());
        orderRepository.save(order);
    }

    @Transactional
    public List<SpecificGame> getSpecificGamesByOrder(String trackingNumber) {
        Order order = getOrderByTrackingNumber(trackingNumber);
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
        // Find and validate the specific game
        Order order = getOrderByTrackingNumber(trackingNumber);
        if (order == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Order not found");
        }
        SpecificGame specificGame = specificGameService.findSpecificGameById(specificGameId);
        if (specificGame == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "SpecificGame not found");
        }

        // Verify that the game is part of the given order
        if (!getSpecificGamesByOrder(trackingNumber).contains(specificGame)) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Game not in order");
        }
        if (SpecificGame.ItemStatus.Returned == specificGame.getItemStatus()) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Game already returned");
        }

        // Update the specific game's item status to Returned
        specificGame.setItemStatus(SpecificGame.ItemStatus.Returned);
        specificGameService.updateSpecificGameItemStatus(specificGame.getSpecificGame_id(),
                SpecificGame.ItemStatus.Returned);

        // Get the associated game and increment the stock quantity
        Game game = specificGame.getGames();
        game.setStockQuantity(game.getStockQuantity() + 1);

        // Persist the stock quantity update
        gameService.updateGameStockQuantity(game.getGame_id(), game.getStockQuantity());
    }

    @Transactional
    public List<Order> getOrdersWithCustomerEmail(String customerEmail) {
        if (customerEmail == null || customerEmail.trim().isEmpty()) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Customer email cannot be empty or null");
        }

        Customer customer = customerRepository.findByEmail(customerEmail);
        if (customer == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Customer not found");
        }

        List<Order> orders = new ArrayList<>();
        for (Order order : getAllOrders()) {
            if (order.getCustomer().equals(customer)) {
                orders.add(order);
            }
        }

        if (orders.isEmpty()) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "No orders found for customer email: " + customerEmail);
        }

        return orders;
    }
}