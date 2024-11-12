package com.mcgill.ecse321.GameShop.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.mcgill.ecse321.GameShop.exception.GameShopException;
import com.mcgill.ecse321.GameShop.model.Customer;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.Game.GameStatus;
import com.mcgill.ecse321.GameShop.model.Cart;

import com.mcgill.ecse321.GameShop.model.Order;
import com.mcgill.ecse321.GameShop.repository.CartRepository;
import com.mcgill.ecse321.GameShop.repository.CustomerRepository;
import com.mcgill.ecse321.GameShop.repository.GameRepository;
import com.mcgill.ecse321.GameShop.repository.OrderRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class OrderServiceTests {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private OrderService orderService;

    private static final String VALID_TRACKING_NUMBER = "TRACK123";
    private static final String INVALID_TRACKING_NUMBER = "INVALID_TRACK";
    private static final String VALID_CUSTOMER_EMAIL = "customer@gmail.ca";
    private static final String INVALID_CUSTOMER_EMAIL = "invalid@ca.ca";
    private static final String VALID_NOTE = "Please deliver between 5-6 PM";
    private static final String UPDATED_NOTE = "Please leave at the front door";
    private static final int VALID_PAYMENT_CARD = 1234567890;
    private static final int INVALID_PAYMENT_CARD = -1;

    // --- Tests for getOrderByTrackingNumber ---

    @Test
    public void testGetOrderByTrackingNumberValid() {
        Cart cart = new Cart();
        cart.setCart_id(701);
        Customer customer = new Customer(
                VALID_CUSTOMER_EMAIL,
                "username",
                "password",
                "1234567890",
                "123 Street",
                cart);
        Order order = new Order(new Date(), VALID_NOTE, VALID_PAYMENT_CARD,
                customer);
        order.setTrackingNumber(VALID_TRACKING_NUMBER);

        when(orderRepository.findByTrackingNumber(VALID_TRACKING_NUMBER)).thenReturn(
                order);

        // Act
        Order retrievedOrder = orderService.getOrderByTrackingNumber(VALID_TRACKING_NUMBER);

        // Assert
        assertNotNull(retrievedOrder);
        assertEquals(VALID_TRACKING_NUMBER, retrievedOrder.getTrackingNumber());

        verify(orderRepository,
                times(1)).findByTrackingNumber(VALID_TRACKING_NUMBER);
    }

    @Test
    public void testGetOrderByTrackingNumberInvalid() {
        when(orderRepository.findByTrackingNumber(INVALID_TRACKING_NUMBER)).thenReturn(null);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            orderService.getOrderByTrackingNumber(INVALID_TRACKING_NUMBER);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Order not found", exception.getMessage());

        verify(orderRepository,
                times(1)).findByTrackingNumber(INVALID_TRACKING_NUMBER);
    }

    @Test
    public void testGetOrderByTrackingNumberNullTrackingNumber() {
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            orderService.getOrderByTrackingNumber(null);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Order not found", exception.getMessage());

        verify(orderRepository, never()).findByTrackingNumber(anyString());
    }

    // --- Tests for createOrder ---

    @Test
    public void testCreateOrderValid() {
        // Arrange
        Game game = new Game("Title", "Description", 50, GameStatus.InStock, 10, "photoUrl");
        game.setGame_id(8703);

        Cart cart = new Cart();
        cart.setCart_id(8702);
        cart.addGame(game); // Ensure this method adds the game to the cart's game list
        cart.().put(game.getGame_id(), 1); // Set the quantity for the game

        Customer customer = new Customer(
                VALID_CUSTOMER_EMAIL + "ta",
                "username",
                "password",
                "1234567890",
                "123 Street",
                cart);

        when(customerRepository.findByEmail(VALID_CUSTOMER_EMAIL + "ta")).thenReturn(customer);

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order savedOrder = invocation.getArgument(0);
            savedOrder.setTrackingNumber(VALID_TRACKING_NUMBER);
            return savedOrder;
        });

        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LocalDate localDate = LocalDate.of(2022, 12, 12);
        Date VALID_ORDER_DATE = java.sql.Date.valueOf(localDate);

        // Act
        Order createdOrder = orderService.createOrder(
                VALID_ORDER_DATE,
                VALID_NOTE,
                VALID_PAYMENT_CARD,
                VALID_CUSTOMER_EMAIL + "ta");

        // Assert
        assertNotNull(createdOrder);
        assertEquals(VALID_TRACKING_NUMBER, createdOrder.getTrackingNumber());
        assertEquals(VALID_ORDER_DATE, createdOrder.getOrderDate());
        assertEquals(VALID_NOTE, createdOrder.getNote());
        assertEquals(VALID_PAYMENT_CARD, createdOrder.getPaymentCard());
        assertEquals(customer, createdOrder.getCustomer());

        // Verify interactions
        verify(customerRepository, times(1)).findByEmail(VALID_CUSTOMER_EMAIL + "ta");
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(cartRepository, times(1)).save(cart);
        verify(gameRepository, times(1)).save(game);
    }

}