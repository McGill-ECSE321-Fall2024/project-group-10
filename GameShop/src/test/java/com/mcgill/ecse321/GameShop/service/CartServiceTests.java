package com.mcgill.ecse321.GameShop.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.mcgill.ecse321.GameShop.exception.GameShopException;
import com.mcgill.ecse321.GameShop.model.Cart;
import com.mcgill.ecse321.GameShop.model.Customer;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.Game.GameStatus;
import com.mcgill.ecse321.GameShop.repository.AccountRepository;
import com.mcgill.ecse321.GameShop.repository.CartRepository;
import com.mcgill.ecse321.GameShop.repository.GameRepository;

import java.util.*;

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
public class CartServiceTests {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private CartService cartService;

    private static final int VALID_CART_ID = 1;
    private static final int INVALID_CART_ID = 999;
    private static final int VALID_GAME_ID = 100;
    private static final int INVALID_GAME_ID = 999;
    private static final Integer VALID_QUANTITY = 2;
    private static final Integer INVALID_QUANTITY = -1;
    private static final String VALID_CUSTOMER_EMAIL = "customer@gmail.ca";
    private static final String INVALID_CUSTOMER_EMAIL = "invalid@ca.ca";

    // --- Tests for getCartByCustomerEmail ---

    @Test
    public void testGetCartByCustomerEmailValid() {
        // Arrange
        Cart cart = new Cart();
        cart.setCart_id(VALID_CART_ID);

        Customer customer = new Customer(
                VALID_CUSTOMER_EMAIL,
                "username",
                "password",
                "1234567890",
                "123 Street",
                cart);

        when(accountRepository.findByEmail(VALID_CUSTOMER_EMAIL)).thenReturn(customer);

        // Act
        Cart retrievedCart = cartService.getCartByCustomerEmail(VALID_CUSTOMER_EMAIL);

        // Assert
        assertNotNull(retrievedCart);
        assertEquals(VALID_CART_ID, retrievedCart.getCart_id());

        verify(accountRepository, times(1)).findByEmail(VALID_CUSTOMER_EMAIL);
    }

    @Test
    public void testGetCartByCustomerEmailInvalidEmail() {
        // Arrange
        when(accountRepository.findByEmail(INVALID_CUSTOMER_EMAIL)).thenReturn(null);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            cartService.getCartByCustomerEmail(INVALID_CUSTOMER_EMAIL);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Customer not found", exception.getMessage());

        verify(accountRepository, times(1)).findByEmail(INVALID_CUSTOMER_EMAIL);
    }

    // --- Tests for getCartById ---

    @Test
    public void testGetCartByIdValid() {
        // Arrange
        Cart cart = new Cart();
        cart.setCart_id(11);

        when(cartRepository.findById(11)).thenReturn(cart);

        // Act
        Cart retrievedCart = cartService.getCartById(11);

        // Assert
        assertNotNull(retrievedCart);
        assertEquals(11, retrievedCart.getCart_id());

        verify(cartRepository, times(1)).findById(11);
    }

    @Test
    public void testGetCartByIdInvalid() {
        // Arrange
        when(cartRepository.findById(INVALID_CART_ID)).thenReturn(null);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            cartService.getCartById(INVALID_CART_ID);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(String.format("Cart with ID %d does not exist", INVALID_CART_ID), exception.getMessage());

        verify(cartRepository, times(1)).findById(INVALID_CART_ID);
    }

    // --- Tests for addGameToCart ---

    @Test
    public void testAddGameToCartValid() {
        // Arrange
        Cart cart = new Cart();
        cart.setCart_id(12);

        Game game = new Game("Title", "Description", 50, GameStatus.InStock, 10, "photoUrl");
        game.setGame_id(VALID_GAME_ID);

        when(cartRepository.findById(12)).thenReturn(cart);
        when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
        when(cartRepository.save(any(Cart.class)))
                .thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        // Act
        Cart updatedCart = cartService.addGameToCart(12, VALID_GAME_ID, VALID_QUANTITY);

        // Assert
        assertNotNull(updatedCart);
        assertTrue(updatedCart.getGames().contains(game));

        Map<Integer, Integer> quantities = cartService.getQuantitiesForCart(12);
        assertEquals(VALID_QUANTITY, quantities.get(VALID_GAME_ID));

        verify(cartRepository, times(1)).findById(12); // getCartById is called twice
        verify(gameRepository, times(1)).findById(VALID_GAME_ID);
        verify(cartRepository, times(1)).save(cart);
    }

    // --- Other tests follow the same pattern ---
    // Adjusted to use the correct Customer constructor and ensure proper
    // associations

    // --- Tests for removeGameFromCart ---

    @Test
    public void testRemoveGameFromCartValid() {
        // Arrange
        Cart cart = new Cart();
        cart.setCart_id(14);

        Game game = new Game("Title", "Description", 50, GameStatus.InStock, 10, "photoUrl");
        game.setGame_id(21);

        when(cartRepository.findById(14)).thenReturn(cart);
        when(gameRepository.findById(21)).thenReturn(game);
        when(cartRepository.save(any(Cart.class)))
                .thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        // Simulate adding the game to the cart first
        cartService.addGameToCart(14, 21, 5);

        // Act
        Cart updatedCart = cartService.removeGameFromCart(14, 21, 2);

        // Assert
        assertNotNull(updatedCart);
        Map<Integer, Integer> updatedQuantities = cartService.getQuantitiesForCart(14);
        assertEquals(3, updatedQuantities.get(21));

        verify(cartRepository, atLeast(1)).findById(14);
        verify(gameRepository, atLeast(1)).findById(21);
        verify(cartRepository, atLeast(1)).save(cart);
    }

    // --- Tests for updateGameQuantityInCart ---

    @Test
    public void testUpdateGameQuantityInCartValid() {
        // Arrange
        Cart cart = new Cart();
        cart.setCart_id(7301);

        Game game = new Game("Title", "Description", 50, GameStatus.InStock, 10, "photoUrl");
        game.setGame_id(7302);

        when(cartRepository.findById(7301)).thenReturn(cart);
        when(gameRepository.findById(7302)).thenReturn(game);
        when(cartRepository.save(any(Cart.class)))
                .thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        // Simulate adding the game to the cart first
        cartService.addGameToCart(7301, 7302, 5);

        // Act
        Cart updatedCart = cartService.updateGameQuantityInCart(7301, 7302, 7);

        // Assert
        assertNotNull(updatedCart);
        Map<Integer, Integer> updatedQuantities = cartService.getQuantitiesForCart(7301);
        assertEquals(7, updatedQuantities.get(7302));

        verify(cartRepository, atLeast(1)).findById(7301);
        verify(gameRepository, atLeast(1)).findById(7302);
        verify(cartRepository, atLeast(1)).save(cart);
    }

    // --- Tests for getAllGamesFromCartWithQuantities ---

    @Test
    public void testGetAllGamesFromCartWithQuantitiesValid() {
        // Arrange
        Cart cart = new Cart();
        cart.setCart_id(19);

        Game game1 = new Game("Title1", "Description1", 50, GameStatus.InStock, 10, "photoUrl1");
        game1.setGame_id(891);
        Game game2 = new Game("Title2", "Description2", 60, GameStatus.InStock, 15, "photoUrl2");
        game2.setGame_id(891 + 1);

        when(cartRepository.findById(19)).thenReturn(cart);
        when(gameRepository.findById(anyInt())).thenAnswer(invocation -> {
            int gameId = invocation.getArgument(0);
            if (gameId == 891) {
                return game1;
            } else if (gameId == 891 + 1) {
                return game2;
            }
            return null;
        });
        when(cartRepository.save(any(Cart.class)))
                .thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        // Simulate adding games to the cart
        cartService.addGameToCart(19, game1.getGame_id(), 3);
        cartService.addGameToCart(19, game2.getGame_id(), 5);

        // Act
        Map<Game, Integer> result = cartService.getAllGamesFromCartWithQuantities(19);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(3, result.get(game1));
        assertEquals(5, result.get(game2));

        verify(cartRepository, atLeast(1)).findById(19);
    }

    // --- Tests for getGameFromCart ---

    @Test
    public void testGetGameFromCartValid() {
        // Arrange
        Cart cart = new Cart();
        cart.setCart_id(29);

        Game game = new Game("Title", "Description", 50, GameStatus.InStock, 10, "photoUrl");
        game.setGame_id(283);

        when(cartRepository.findById(29)).thenReturn(cart);
        when(gameRepository.findById(283)).thenReturn(game);

        // Simulate adding the game to the cart
        cartService.addGameToCart(29, 283, 1);

        // Act
        Game resultGame = cartService.getGameFromCart(29, 283);

        // Assert
        assertNotNull(resultGame);
        assertEquals(283, resultGame.getGame_id());

        verify(cartRepository, atLeast(1)).findById(29);
    }

    // --- Tests for clearCart ---

    @Test
    public void testClearCartValid() {
        // Arrange
        Cart cart = new Cart();
        cart.setCart_id(279);

        Game game1 = new Game("Title1", "Description1", 50, GameStatus.InStock, 10, "photoUrl1");
        game1.setGame_id(341);
        Game game2 = new Game("Title2", "Description2", 60, GameStatus.InStock, 15, "photoUrl2");
        game2.setGame_id(342);

        when(cartRepository.findById(279)).thenReturn(cart);
        when(cartRepository.save(any(Cart.class)))
                .thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        when(gameRepository.findById(341)).thenReturn(game1);
        when(gameRepository.findById(342)).thenReturn(game2);

        // Simulate adding games to the cart
        cartService.addGameToCart(279, game1.getGame_id(), 3);
        cartService.addGameToCart(279, game2.getGame_id(), 5);

        // Act
        Cart clearedCart = cartService.clearCart(279);

        // Assert
        assertNotNull(clearedCart);
        assertTrue(clearedCart.getGames().isEmpty());
        Map<Integer, Integer> quantities = cartService.getQuantitiesForCart(279);
        assertTrue(quantities.isEmpty());

        verify(cartRepository, atLeast(1)).findById(279);
        verify(cartRepository, atLeast(1)).save(cart);
    }

    // --- Tests for getAllCarts ---

    @Test
    public void testGetAllCarts() {
        // Arrange
        Cart cart1 = new Cart();
        cart1.setCart_id(652);
        Cart cart2 = new Cart();
        cart2.setCart_id(653);

        List<Cart> carts = Arrays.asList(cart1, cart2);

        when(cartRepository.findAll()).thenReturn(carts);

        // Act
        Iterable<Cart> result = cartService.getAllCarts();

        // Assert
        assertNotNull(result);
        List<Cart> resultList = new ArrayList<>();
        result.forEach(resultList::add);

        assertEquals(2, resultList.size());
        assertTrue(resultList.contains(cart1));
        assertTrue(resultList.contains(cart2));

        verify(cartRepository, times(1)).findAll();
    }
}
