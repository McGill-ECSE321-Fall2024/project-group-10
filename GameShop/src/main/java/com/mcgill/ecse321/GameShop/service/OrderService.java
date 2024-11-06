// OrderService.java
package com.mcgill.ecse321.GameShop.service;

import com.mcgill.ecse321.GameShop.model.Order;
import com.mcgill.ecse321.GameShop.model.SpecificGame;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.Customer;
import com.mcgill.ecse321.GameShop.model.Cart;
import com.mcgill.ecse321.GameShop.repository.*;
import com.mcgill.ecse321.GameShop.exception.GameShopException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
        for (Game game : gamesInCart) {
            int gameId = game.getGame_id();
            Map<Integer, Integer> quantities = cartService.getQuantitiesForCart(cart.getCart_id());
            int quantity = quantities.getOrDefault(gameId, 0);

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
    public Order getOrderById(String trackingNumber) {
        Order order = orderRepository.findByTrackingNumber(trackingNumber);
        if (order == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Order not found");
        }
        return order;
    }

    @Transactional
    public Iterable<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public List<Order> getOrdersByCustomerEmail(String customerEmail) {
        Customer customer = customerRepository.findByEmail(customerEmail);
        if (customer == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Customer not found");
        }
        List<Order> ordersList = StreamSupport.stream(getAllOrders().spliterator(), false)
                .collect(Collectors.toList());
        List<Order> customerOrders = ordersList.stream()
                .filter(order -> order.getCustomer().getEmail().equals(customerEmail))
                .collect(Collectors.toList());
        return customerOrders;
    }

}