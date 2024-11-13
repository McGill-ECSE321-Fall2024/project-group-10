package com.mcgill.ecse321.GameShop.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import com.mcgill.ecse321.GameShop.dto.OrderDto.OrderAddGameRequestDto;
import com.mcgill.ecse321.GameShop.dto.OrderDto.OrderListDto;
import com.mcgill.ecse321.GameShop.dto.OrderDto.OrderRequestDto;
import com.mcgill.ecse321.GameShop.dto.OrderDto.OrderResponseDto;
import com.mcgill.ecse321.GameShop.dto.AccountDtos.AccountRequestDto;
import com.mcgill.ecse321.GameShop.dto.AccountDtos.AccountResponseDto;
import com.mcgill.ecse321.GameShop.dto.CartDto.CartRequestDto;
import com.mcgill.ecse321.GameShop.dto.CartDto.CartResponseDto;
import com.mcgill.ecse321.GameShop.dto.GameDto.GameListDto;
import com.mcgill.ecse321.GameShop.dto.GameDto.GameRequestDto;
import com.mcgill.ecse321.GameShop.dto.GameDto.GameResponseDto;
import com.mcgill.ecse321.GameShop.dto.SpecificGameDto.SpecificGameRequestDto;
import com.mcgill.ecse321.GameShop.dto.SpecificGameDto.SpecificGameResponseDto;
import com.mcgill.ecse321.GameShop.model.Game.GameStatus;
import com.mcgill.ecse321.GameShop.repository.AccountRepository;
import com.mcgill.ecse321.GameShop.repository.CartRepository;
import com.mcgill.ecse321.GameShop.repository.CustomerRepository;
import com.mcgill.ecse321.GameShop.repository.GameRepository;
import com.mcgill.ecse321.GameShop.repository.OrderRepository;
import com.mcgill.ecse321.GameShop.repository.SpecificGameRepository;
import com.mcgill.ecse321.GameShop.repository.WishListRepository;
import com.mcgill.ecse321.GameShop.model.SpecificGame;
import com.mcgill.ecse321.GameShop.model.SpecificGame.ItemStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class OrderIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private SpecificGameRepository specificGameRepository;

    @Autowired
    private CartRepository cartRepository;

    private String trackingNumber = "HELLOHELLOTRACKINGNUMBERHELLOHELLO";
    private String customerEmail = "customer@hellohello.com";
    private int gameId;
    private int specificGameId;
    private ObjectMapper objectMapper = new ObjectMapper();
    private int paymentCard = 1234567890;

    @BeforeAll
    @AfterAll
    public void clearDatabase() {
        specificGameRepository.deleteAll();
        wishListRepository.deleteAll();
        orderRepository.deleteAll();
        accountRepository.deleteAll();
        cartRepository.deleteAll();
        gameRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testCreateValidOrder() {
        // Step 1: Create Customer Account
        AccountRequestDto customerRequest = new AccountRequestDto(
                customerEmail,
                "customerUser",
                "customerPass",
                "123-456-7890",
                "123 Customer Street");
        ResponseEntity<AccountResponseDto> customerResponse = client.postForEntity(
                "/account/customer",
                customerRequest,
                AccountResponseDto.class);
        assertNotNull(customerResponse);
        assertEquals(HttpStatus.OK, customerResponse.getStatusCode());
        AccountResponseDto customer = customerResponse.getBody();
        assertNotNull(customer);
        assertEquals(customerEmail, customer.getEmail());

        // Step 2: Create a Game
        GameRequestDto gameRequest = new GameRequestDto(
                "Test Game",
                "Test Description",
                50,
                GameStatus.InStock,
                10,
                "http://example.com/game.jpg");
        ResponseEntity<GameResponseDto> gameResponse = client.postForEntity(
                "/games",
                gameRequest,
                GameResponseDto.class);
        assertNotNull(gameResponse);
        assertEquals(HttpStatus.OK, gameResponse.getStatusCode());
        GameResponseDto game = gameResponse.getBody();
        assertNotNull(game);
        gameId = game.getaGame_id();

        // Step 3: Retrieve or Create Customer's Cart
        ResponseEntity<CartResponseDto> cartResponse = client.getForEntity(
                "/carts/customer/" + customerEmail,
                CartResponseDto.class);
        assertEquals(HttpStatus.OK, cartResponse.getStatusCode());
        CartResponseDto cart = cartResponse.getBody();
        assertNotNull(cart);

        int cartId = cart.getCartId();

        // Step 4: Add Game to Customer's Cart
        CartRequestDto cartRequestDto = new CartRequestDto(gameId, 1);
        ResponseEntity<CartResponseDto> updatedCartResponse = client.postForEntity(
                "/carts/" + cartId + "/games",
                cartRequestDto,
                CartResponseDto.class);
        assertNotNull(updatedCartResponse);
        assertEquals(HttpStatus.OK, updatedCartResponse.getStatusCode());

        // Step 5: Create Specific Game Instance for Order
        SpecificGameRequestDto specificGameRequest = new SpecificGameRequestDto(
                SpecificGame.ItemStatus.Confirmed,
                new ArrayList<>(),
                gameId);
        ResponseEntity<SpecificGameResponseDto> specificGameResponse = client.postForEntity(
                "/specificGames",
                specificGameRequest,
                SpecificGameResponseDto.class);
        assertNotNull(specificGameResponse);
        assertEquals(HttpStatus.OK, specificGameResponse.getStatusCode());
        SpecificGameResponseDto specificGame = specificGameResponse.getBody();
        assertNotNull(specificGame);
        specificGameId = specificGame.getSpecificGame_id();

        List<Integer> specificGamesIds = new ArrayList<>();
        specificGamesIds.add(specificGameId); // Use SpecificGame ID here

        // Step 6: Create Order with Customer’s Cart
        OrderRequestDto orderRequest = new OrderRequestDto(
                new Date(),
                "Please deliver between 9 AM - 5 PM",
                paymentCard,
                customerEmail,
                specificGamesIds);

        ResponseEntity<OrderResponseDto> orderResponse = client.postForEntity(
                "/orders",
                orderRequest,
                OrderResponseDto.class);

        assertNotNull(orderResponse);
        assertEquals(HttpStatus.OK, orderResponse.getStatusCode());
        OrderResponseDto order = orderResponse.getBody();
        assertNotNull(order);
        trackingNumber = order.getTrackingNumber();

        // Final Assertions
        assertNotNull(trackingNumber);
        assertEquals(customerEmail, order.getCustomerEmail());
    }

    @Test
    @Order(2)
    public void testGetOrdersByCustomerEmail() {
        // Adding logging to see the URL and expected customer email
        System.out.println("Retrieving orders for email: " + customerEmail);

        // Retrieve all orders by customer email
        ResponseEntity<OrderListDto> response = client.getForEntity(
                "/orders/customer/" + customerEmail,
                OrderListDto.class);

        // Print status for debugging
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody());

        // Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        OrderListDto orders = response.getBody();
        assertNotNull(orders);
        assertTrue(orders.getOrders().size() > 0); // Ensure at least one order is returned
    }
}