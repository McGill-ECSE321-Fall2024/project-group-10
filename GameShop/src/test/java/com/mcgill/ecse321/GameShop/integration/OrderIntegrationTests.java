package com.mcgill.ecse321.GameShop.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
        private int gameId = 74289562;
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

                // Call the endpoint to retrieve orders for the specified customer
                ResponseEntity<OrderListDto> response = client.getForEntity(
                                "/orders/customer/" + customerEmail,
                                OrderListDto.class);

                // Assert that response is non-null and status is OK
                assertNotNull(response, "Response was null; expected a valid response entity.");
                assertEquals(HttpStatus.OK, response.getStatusCode(),
                                "Expected status OK but got " + response.getStatusCode());

                // Extract order list from response and verify it contains data
                OrderListDto orders = response.getBody();
                assertNotNull(orders, "Expected non-null OrderListDto, but got null");

                // Ensure there's at least one order and that the order belongs to the correct
                // customer
                assertFalse(orders.getOrders().isEmpty(), "Expected orders, but found none");
                orders.getOrders().forEach(order -> {
                        assertEquals(customerEmail, order.getCustomerEmail(),
                                        "Order customer email does not match the expected email");
                });
        }

        @Test
        @Order(3)
        public void testGetOrderByTrackingNumber() {
                // Retrieve order by tracking number
                ResponseEntity<OrderResponseDto> response = client.getForEntity(
                                "/orders/" + trackingNumber,
                                OrderResponseDto.class);

                // Assertions
                assertNotNull(response, "Response was null; expected a valid response entity.");
                assertEquals(HttpStatus.OK, response.getStatusCode(),
                                "Expected status OK but got " + response.getStatusCode());

                OrderResponseDto order = response.getBody();
                assertNotNull(order, "Expected non-null OrderResponseDto, but got null");

                // Check if tracking number and customer email match
                assertEquals(trackingNumber, order.getTrackingNumber(), "Tracking number mismatch");
                assertEquals(customerEmail, order.getCustomerEmail(), "Customer email mismatch");

                // Ensure specific games are returned in the order
                assertFalse(order.getSpecificGames().isEmpty(), "Expected specific games in the order, but found none");
        }

        @Test
        @Order(4)
        public void testUpdateOrder() {
                // Updated details
                String updatedNote = "Updated delivery instructions";
                Date updatedOrderDate = new Date(); // Current date for simplicity
                int updatedPaymentCard = 987654321;

                OrderRequestDto updateRequest = new OrderRequestDto(
                                updatedOrderDate,
                                updatedNote,
                                updatedPaymentCard,
                                customerEmail,
                                null // No specific games update
                );

                // Send update request
                ResponseEntity<OrderResponseDto> response = client.exchange(
                                "/orders/" + trackingNumber,
                                HttpMethod.PUT,
                                new HttpEntity<>(updateRequest),
                                OrderResponseDto.class);

                // Assertions
                assertNotNull(response, "Response was null; expected a valid response entity.");
                assertEquals(HttpStatus.OK, response.getStatusCode(),
                                "Expected status OK but got " + response.getStatusCode());

                OrderResponseDto updatedOrder = response.getBody();
                assertNotNull(updatedOrder, "Expected non-null OrderResponseDto, but got null");

                // Verify that the updated fields match the expected values
                assertEquals(updatedNote, updatedOrder.getNote(), "Order note mismatch");
        }

        @Test
        @Order(5)
        public void testAddGameToOrder() {
                // Ensure a fresh game and specific game instance setup if needed
                GameRequestDto newGameRequest = new GameRequestDto(
                                "Additional Test Game",
                                "Description for additional game",
                                50,
                                GameStatus.InStock,
                                10,
                                "http://example.com/additional_game.jpg");

                ResponseEntity<GameResponseDto> gameResponse = client.postForEntity(
                                "/games",
                                newGameRequest,
                                GameResponseDto.class);
                assertNotNull(gameResponse);
                assertEquals(HttpStatus.OK, gameResponse.getStatusCode());

                GameResponseDto newGame = gameResponse.getBody();
                int additionalGameId = newGame.getaGame_id();

                // Create specific game instance for the newly created game
                SpecificGameRequestDto specificGameRequest = new SpecificGameRequestDto(
                                SpecificGame.ItemStatus.Confirmed,
                                new ArrayList<>(),
                                additionalGameId);

                ResponseEntity<SpecificGameResponseDto> specificGameResponse = client.postForEntity(
                                "/specificGames",
                                specificGameRequest,
                                SpecificGameResponseDto.class);
                assertNotNull(specificGameResponse);
                assertEquals(HttpStatus.OK, specificGameResponse.getStatusCode());

                // Add the game to the existing order
                OrderAddGameRequestDto addGameRequest = new OrderAddGameRequestDto(additionalGameId, 1);
                ResponseEntity<OrderResponseDto> response = client.postForEntity(
                                "/orders/" + trackingNumber + "/games",
                                addGameRequest,
                                OrderResponseDto.class);

                // Assertions
                assertNotNull(response, "Response was null; expected a valid response entity.");
                assertEquals(HttpStatus.OK, response.getStatusCode(),
                                "Expected status OK but got " + response.getStatusCode());

                OrderResponseDto order = response.getBody();
                assertNotNull(order, "Expected non-null OrderResponseDto, but got null");

                // Confirm that the order contains at least two specific games after addition
                assertTrue(order.getSpecificGames().size() >= 2,
                                "Expected at least 2 specific games in the order after addition, but found: "
                                                + order.getSpecificGames().size());
        }

        @Test
        @Order(6)
        public void testAddAnotherSpecificGameToOrder() {
                System.out.println("Starting testAddAnotherSpecificGameToOrder");

                // Step 1: Create a second SpecificGame instance for the same Game
                SpecificGameRequestDto specificGameRequest2 = new SpecificGameRequestDto(
                                SpecificGame.ItemStatus.Confirmed,
                                new ArrayList<>(),
                                gameId);
                ResponseEntity<SpecificGameResponseDto> specificGameResponse2 = client.postForEntity(
                                "/specificGames",
                                specificGameRequest2,
                                SpecificGameResponseDto.class);
                assertNotNull(specificGameResponse2);
                assertEquals(HttpStatus.OK, specificGameResponse2.getStatusCode());

                SpecificGameResponseDto specificGame2 = specificGameResponse2.getBody();
                assertNotNull(specificGame2);
                int specificGameId2 = specificGame2.getSpecificGame_id();

                System.out.println("SpecificGame 2 created with ID: " + specificGameId2);

                // Step 2: Add the second SpecificGame instance to the order
                OrderAddGameRequestDto addGameRequest2 = new OrderAddGameRequestDto(gameId, 1);
                ResponseEntity<OrderResponseDto> response = client.postForEntity(
                                "/orders/" + trackingNumber + "/games",
                                addGameRequest2,
                                OrderResponseDto.class);

                System.out.println("Adding second specific game to order response: " + response.getStatusCode());

                // Step 3: Retrieve the updated order
                ResponseEntity<OrderResponseDto> updatedOrderResponse = client.getForEntity(
                                "/orders/" + trackingNumber,
                                OrderResponseDto.class);

                assertNotNull(updatedOrderResponse);
                assertEquals(HttpStatus.OK, updatedOrderResponse.getStatusCode());

                OrderResponseDto updatedOrder = updatedOrderResponse.getBody();
                assertNotNull(updatedOrder);

                // Log the specific game IDs in the order for verification
                List<Integer> specificGameIds = updatedOrder.getSpecificGames().stream()
                                .map(SpecificGameResponseDto::getSpecificGame_id)
                                .collect(Collectors.toList());
                System.out.println("Specific game IDs in order: " + specificGameIds);

                // Ensure that both specific games are present
                assertTrue(specificGameIds.contains(specificGameId), "Order should contain the first SpecificGame");
                assertTrue(specificGameIds.contains(specificGameId2), "Order should contain the second SpecificGame");

                // Step 4: Confirm the count of specific games with the same gameId
                long count = updatedOrder.getSpecificGames().stream()
                                .filter(sg -> sg.getGame_id() == gameId)
                                .count();
                assertTrue(count >= 2,
                                "Expected at least 2 specific games with gameId " + gameId + " but found: " + count);
        }

}