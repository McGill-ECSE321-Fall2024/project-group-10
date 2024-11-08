package com.mcgill.ecse321.GameShop.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.mcgill.ecse321.GameShop.exception.GameShopException;
import com.mcgill.ecse321.GameShop.model.Cart;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.Game.GameStatus;
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

    @InjectMocks
    private CartService cartService;

    private static final int VALID_CART_ID = 1;
    private static final int INVALID_CART_ID = 999;
    private static final int VALID_GAME_ID = 100;
    private static final int INVALID_GAME_ID = 999;
    private static final Integer VALID_QUANTITY = 2;
    private static final Integer INVALID_QUANTITY = -1;

    // createCart

    @Test
    public void testCreateCart() {
        when(cartRepository.save(any(Cart.class))).thenAnswer((InvocationOnMock invocation) -> {
            Cart savedCart = invocation.getArgument(0);
            savedCart.setCart_id(VALID_CART_ID);
            return savedCart;
        });
        Cart createdCart = cartService.createCart();
        assertNotNull(createdCart);
        assertEquals(VALID_CART_ID, createdCart.getCart_id());

        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    // getCartById

    @Test
    public void testGetCartByIdValid() {
        Cart cart = new Cart();
        cart.setCart_id(VALID_CART_ID);
        when(cartRepository.findById(VALID_CART_ID)).thenReturn(cart);
        Cart retrievedCart = cartService.getCartById(VALID_CART_ID);

        assertNotNull(retrievedCart);
        assertEquals(VALID_CART_ID, retrievedCart.getCart_id());
        verify(cartRepository, times(1)).findById(VALID_CART_ID);
    }

    @Test
    public void testGetCartByIdInvalid() {
        when(cartRepository.findById(INVALID_CART_ID)).thenReturn(null);

        GameShopException exception = assertThrows(GameShopException.class, () -> {
            cartService.getCartById(INVALID_CART_ID);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(String.format("Cart with ID %d does not exist", INVALID_CART_ID), exception.getMessage());

        verify(cartRepository, times(1)).findById(INVALID_CART_ID);
    }

    // addGameToCart

    @Test
    public void testAddGameToCartValid() {
        Cart cart = new Cart();
        cart.setCart_id(VALID_CART_ID);

        Game game = new Game("Title", "Description", 50, GameStatus.InStock, 10, "photoUrl");
        game.setGame_id(VALID_GAME_ID);

        when(cartRepository.findById(VALID_CART_ID)).thenReturn(cart);
        when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
        when(cartRepository.save(any(Cart.class)))
                .thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        Cart updatedCart = cartService.addGameToCart(VALID_CART_ID, VALID_GAME_ID, VALID_QUANTITY);
        assertNotNull(updatedCart);
        assertTrue(updatedCart.getGames().contains(game));

        Map<Integer, Integer> quantities = cartService.getQuantitiesForCart(VALID_CART_ID);
        assertEquals(VALID_QUANTITY, quantities.get(VALID_GAME_ID));

        verify(cartRepository, times(1)).findById(VALID_CART_ID);
        verify(gameRepository, times(1)).findById(VALID_GAME_ID);
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    public void testAddGameToCartWithInvalidCartId() {
        when(cartRepository.findById(INVALID_CART_ID)).thenReturn(null);

        GameShopException exception = assertThrows(GameShopException.class, () -> {
            cartService.addGameToCart(INVALID_CART_ID, VALID_GAME_ID, VALID_QUANTITY);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(String.format("Cart with ID %d does not exist", INVALID_CART_ID), exception.getMessage());

        verify(cartRepository, times(1)).findById(INVALID_CART_ID);
        verify(gameRepository, never()).findById(anyInt());
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    public void testAddGameToCartWithInvalidGameId() {
        Cart cart = new Cart();
        cart.setCart_id(VALID_CART_ID);

        when(cartRepository.findById(VALID_CART_ID)).thenReturn(cart);
        when(gameRepository.findById(INVALID_GAME_ID)).thenReturn(null);
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            cartService.addGameToCart(VALID_CART_ID, INVALID_GAME_ID, VALID_QUANTITY);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(String.format("Game with ID %d does not exist", INVALID_GAME_ID), exception.getMessage());

        verify(cartRepository, times(1)).findById(VALID_CART_ID);
        verify(gameRepository, times(1)).findById(INVALID_GAME_ID);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    public void testAddGameToCartWithInvalidQuantity() {
        Cart cart = new Cart();
        cart.setCart_id(VALID_CART_ID);

        Game game = new Game("Title", "Description", 50, GameStatus.InStock, 10, "photoUrl");
        game.setGame_id(VALID_GAME_ID);

        when(cartRepository.findById(VALID_CART_ID)).thenReturn(cart);
        when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
        when(cartRepository.save(any(Cart.class)))
                .thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        Cart updatedCart = cartService.addGameToCart(VALID_CART_ID, VALID_GAME_ID, INVALID_QUANTITY);

        assertNotNull(updatedCart);
        assertTrue(updatedCart.getGames().contains(game));

        Map<Integer, Integer> quantities = cartService.getQuantitiesForCart(VALID_CART_ID);
        assertEquals(1, quantities.get(VALID_GAME_ID));

        verify(cartRepository, times(1)).findById(VALID_CART_ID);
        verify(gameRepository, times(1)).findById(VALID_GAME_ID);
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    public void testAddGameToCartGameOutOfStock() {
        Cart cart = new Cart();
        cart.setCart_id(VALID_CART_ID);

        Game game = new Game("Title", "Description", 50, GameStatus.OutOfStock, 0, "photoUrl");
        game.setGame_id(VALID_GAME_ID);

        when(cartRepository.findById(VALID_CART_ID)).thenReturn(cart);
        when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);

        GameShopException exception = assertThrows(GameShopException.class, () -> {
            cartService.addGameToCart(VALID_CART_ID, VALID_GAME_ID, VALID_QUANTITY);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(String.format("Game with ID %d is not available for purchase", VALID_GAME_ID),
                exception.getMessage());

        verify(cartRepository, times(1)).findById(VALID_CART_ID);
        verify(gameRepository, times(1)).findById(VALID_GAME_ID);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    public void testAddGameToCartQuantityExceedsStock() {
        Cart cart = new Cart();
        cart.setCart_id(VALID_CART_ID);

        Game game = new Game("Title", "Description", 50, GameStatus.InStock, 5, "photoUrl");
        game.setGame_id(VALID_GAME_ID);

        when(cartRepository.findById(VALID_CART_ID)).thenReturn(cart);
        when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);

        GameShopException exception = assertThrows(GameShopException.class, () -> {
            cartService.addGameToCart(VALID_CART_ID, VALID_GAME_ID, 10);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(String.format("Only %d units of Game ID %d are available", game.getStockQuantity(), VALID_GAME_ID),
                exception.getMessage());

        verify(cartRepository, times(1)).findById(VALID_CART_ID);
        verify(gameRepository, times(1)).findById(VALID_GAME_ID);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    // removeGameFromCart

    @Test
    public void testRemoveGameFromCartValid() {
        Cart cart = new Cart();
        cart.setCart_id(VALID_CART_ID);

        Game game = new Game("Title", "Description", 50, GameStatus.InStock, 10, "photoUrl");
        game.setGame_id(VALID_GAME_ID);

        // Simulate adding the game to the cart first
        when(cartRepository.findById(VALID_CART_ID)).thenReturn(cart);
        when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
        when(cartRepository.save(any(Cart.class)))
                .thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        cartService.addGameToCart(VALID_CART_ID, VALID_GAME_ID, 5);

        Cart updatedCart = cartService.removeGameFromCart(VALID_CART_ID, VALID_GAME_ID, 2);

        assertNotNull(updatedCart);
        assertTrue(updatedCart.getGames().contains(game));
        Map<Integer, Integer> updatedQuantities = cartService.getQuantitiesForCart(VALID_CART_ID);
        assertEquals(3, updatedQuantities.get(VALID_GAME_ID));

        verify(cartRepository, atLeast(1)).findById(VALID_CART_ID);
        verify(gameRepository, atLeast(1)).findById(VALID_GAME_ID);
        verify(cartRepository, atLeast(1)).save(cart);
    }

    @Test
    public void testRemoveGameFromCartInvalidQuantity() {
        Cart cart = new Cart();
        cart.setCart_id(VALID_CART_ID);

        when(cartRepository.findById(VALID_CART_ID)).thenReturn(cart);

        GameShopException exception = assertThrows(GameShopException.class, () -> {
            cartService.removeGameFromCart(VALID_CART_ID, VALID_GAME_ID, INVALID_QUANTITY);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Quantity must be at least 1.", exception.getMessage());

        verify(cartRepository, times(1)).findById(VALID_CART_ID);
        verify(gameRepository, never()).findById(anyInt());
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    public void testRemoveGameFromCartGameNotInCart() {
        Cart cart = new Cart();
        cart.setCart_id(VALID_CART_ID);

        Game game = new Game("Title", "Description", 50, GameStatus.InStock, 10, "photoUrl");
        game.setGame_id(VALID_GAME_ID);

        when(cartRepository.findById(VALID_CART_ID)).thenReturn(cart);
        when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);

        GameShopException exception = assertThrows(GameShopException.class, () -> {
            cartService.removeGameFromCart(VALID_CART_ID, VALID_GAME_ID, 1);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Game is not in the cart.", exception.getMessage());

        verify(cartRepository, times(1)).findById(VALID_CART_ID);
        verify(gameRepository, times(1)).findById(VALID_GAME_ID);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    public void testRemoveGameFromCartRemoveMoreThanExists() {
        Cart cart = new Cart();
        cart.setCart_id(VALID_CART_ID);

        Game game = new Game("Title", "Description", 50, GameStatus.InStock, 10, "photoUrl");
        game.setGame_id(VALID_GAME_ID);

        // Simulate adding the game to the cart first
        when(cartRepository.findById(VALID_CART_ID)).thenReturn(cart);
        when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);

        cartService.addGameToCart(VALID_CART_ID, VALID_GAME_ID, 2);

        GameShopException exception = assertThrows(GameShopException.class, () -> {
            cartService.removeGameFromCart(VALID_CART_ID, VALID_GAME_ID, 3);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Cannot remove more than the existing quantity.", exception.getMessage());

        verify(cartRepository, atLeast(1)).findById(VALID_CART_ID);
        verify(gameRepository, atLeast(1)).findById(VALID_GAME_ID);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    // updateGameQuantityInCart

    @Test
    public void testUpdateGameQuantityInCartValid() {
        Cart cart = new Cart();
        cart.setCart_id(VALID_CART_ID);

        Game game = new Game("Title", "Description", 50, GameStatus.InStock, 10, "photoUrl");
        game.setGame_id(VALID_GAME_ID);

        // Simulate adding the game to the cart first
        when(cartRepository.findById(VALID_CART_ID)).thenReturn(cart);
        when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
        when(cartRepository.save(any(Cart.class)))
                .thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        cartService.addGameToCart(VALID_CART_ID, VALID_GAME_ID, 5);

        Cart updatedCart = cartService.updateGameQuantityInCart(VALID_CART_ID, VALID_GAME_ID, 7);

        assertNotNull(updatedCart);
        Map<Integer, Integer> updatedQuantities = cartService.getQuantitiesForCart(VALID_CART_ID);
        assertEquals(7, updatedQuantities.get(VALID_GAME_ID));

        verify(cartRepository, atLeast(1)).findById(VALID_CART_ID);
        verify(gameRepository, atLeast(1)).findById(VALID_GAME_ID);
        verify(cartRepository, atLeast(1)).save(cart);
    }

    @Test
    public void testUpdateGameQuantityInCartInvalidQuantity() {
        Cart cart = new Cart();
        cart.setCart_id(VALID_CART_ID);

        when(cartRepository.findById(VALID_CART_ID)).thenReturn(cart);

        GameShopException exception = assertThrows(GameShopException.class, () -> {
            cartService.updateGameQuantityInCart(VALID_CART_ID, VALID_GAME_ID, INVALID_QUANTITY);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Quantity must be 0 or greater.", exception.getMessage());

        verify(cartRepository, times(1)).findById(VALID_CART_ID);
        verify(gameRepository, never()).findById(anyInt());
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    public void testUpdateGameQuantityInCartGameNotInCart() {
        Cart cart = new Cart();
        cart.setCart_id(VALID_CART_ID);

        Game game = new Game("Title", "Description", 50, GameStatus.InStock, 10, "photoUrl");
        game.setGame_id(VALID_GAME_ID);

        when(cartRepository.findById(VALID_CART_ID)).thenReturn(cart);
        when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);

        GameShopException exception = assertThrows(GameShopException.class, () -> {
            cartService.updateGameQuantityInCart(VALID_CART_ID, VALID_GAME_ID, 5);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Game is not in the cart.", exception.getMessage());

        verify(cartRepository, times(1)).findById(VALID_CART_ID);
        verify(gameRepository, times(1)).findById(VALID_GAME_ID);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    public void testUpdateGameQuantityInCartExceedsStock() {
        Cart cart = new Cart();
        cart.setCart_id(VALID_CART_ID);

        Game game = new Game("Title", "Description", 50, GameStatus.InStock, 5, "photoUrl");
        game.setGame_id(VALID_GAME_ID);

        // Simulate adding the game to the cart first
        when(cartRepository.findById(VALID_CART_ID)).thenReturn(cart);
        when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);

        cartService.addGameToCart(VALID_CART_ID, VALID_GAME_ID, 2);

        GameShopException exception = assertThrows(GameShopException.class, () -> {
            cartService.updateGameQuantityInCart(VALID_CART_ID, VALID_GAME_ID, 10);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(String.format("Only %d units of Game ID %d are available", game.getStockQuantity(), VALID_GAME_ID),
                exception.getMessage());

        verify(cartRepository, atLeast(1)).findById(VALID_CART_ID);
        verify(gameRepository, atLeast(1)).findById(VALID_GAME_ID);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    // getAllGamesFromCartWithQuantities

    @Test
    public void testGetAllGamesFromCartWithQuantitiesValid() {
        Cart cart = new Cart();
        cart.setCart_id(VALID_CART_ID);

        Game game1 = new Game("Title1", "Description1", 50, GameStatus.InStock, 10, "photoUrl1");
        game1.setGame_id(VALID_GAME_ID);
        Game game2 = new Game("Title2", "Description2", 60, GameStatus.InStock, 15, "photoUrl2");
        game2.setGame_id(VALID_GAME_ID + 1);

        when(cartRepository.findById(VALID_CART_ID)).thenReturn(cart);
        when(gameRepository.findById(game1.getGame_id())).thenReturn(game1);
        when(gameRepository.findById(game2.getGame_id())).thenReturn(game2);
        when(cartRepository.save(any(Cart.class)))
                .thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        // Simulate adding games to the cart
        cartService.addGameToCart(VALID_CART_ID, game1.getGame_id(), 3);
        cartService.addGameToCart(VALID_CART_ID, game2.getGame_id(), 5);

        Map<Game, Integer> result = cartService.getAllGamesFromCartWithQuantities(VALID_CART_ID);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsKey(game1));
        assertTrue(result.containsKey(game2));
        assertEquals(3, result.get(game1));
        assertEquals(5, result.get(game2));

        verify(cartRepository, atLeast(1)).findById(VALID_CART_ID);
    }

    @Test
    public void testGetAllGamesFromCartWithQuantitiesEmptyCart() {
        Cart cart = new Cart();
        cart.setCart_id(VALID_CART_ID);

        when(cartRepository.findById(VALID_CART_ID)).thenReturn(cart);

        Map<Game, Integer> result = cartService.getAllGamesFromCartWithQuantities(VALID_CART_ID);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(cartRepository, times(1)).findById(VALID_CART_ID);
    }

    @Test
    public void testGetAllGamesFromCartWithQuantitiesInvalidCartId() {
        when(cartRepository.findById(INVALID_CART_ID)).thenReturn(null);

        GameShopException exception = assertThrows(GameShopException.class, () -> {
            cartService.getAllGamesFromCartWithQuantities(INVALID_CART_ID);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(String.format("Cart with ID %d does not exist", INVALID_CART_ID), exception.getMessage());

        verify(cartRepository, times(1)).findById(INVALID_CART_ID);
    }

    // clearCart

    @Test
    public void testClearCartValid() {
        Cart cart = new Cart();
        cart.setCart_id(VALID_CART_ID);

        Game game1 = new Game("Title1", "Description1", 50, GameStatus.InStock, 10, "photoUrl1");
        game1.setGame_id(VALID_GAME_ID);
        Game game2 = new Game("Title2", "Description2", 60, GameStatus.InStock, 15, "photoUrl2");
        game2.setGame_id(VALID_GAME_ID + 1);

        when(cartRepository.findById(VALID_CART_ID)).thenReturn(cart);
        when(cartRepository.save(any(Cart.class)))
                .thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        cartService.addGameToCart(VALID_CART_ID, game1.getGame_id(), 3);
        cartService.addGameToCart(VALID_CART_ID, game2.getGame_id(), 5);

        Cart clearedCart = cartService.clearCart(VALID_CART_ID);

        assertNotNull(clearedCart);
        assertTrue(clearedCart.getGames().isEmpty());
        Map<Integer, Integer> quantities = cartService.getQuantitiesForCart(VALID_CART_ID);
        assertTrue(quantities.isEmpty());

        verify(cartRepository, atLeast(1)).findById(VALID_CART_ID);
        verify(cartRepository, atLeast(1)).save(cart);
    }

    @Test
    public void testClearCartInvalidCartId() {
        when(cartRepository.findById(INVALID_CART_ID)).thenReturn(null);

        GameShopException exception = assertThrows(GameShopException.class, () -> {
            cartService.clearCart(INVALID_CART_ID);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(String.format("Cart with ID %d does not exist", INVALID_CART_ID), exception.getMessage());

        verify(cartRepository, times(1)).findById(INVALID_CART_ID);
    }

    // getAllCarts

    @Test
    public void testGetAllCarts() {
        Cart cart1 = new Cart();
        cart1.setCart_id(VALID_CART_ID);
        Cart cart2 = new Cart();
        cart2.setCart_id(VALID_CART_ID + 1);

        List<Cart> carts = Arrays.asList(cart1, cart2);

        when(cartRepository.findAll()).thenReturn(carts);

        Iterable<Cart> result = cartService.getAllCarts();

        assertNotNull(result);
        List<Cart> resultList = new ArrayList<>();
        result.forEach(resultList::add);

        assertEquals(2, resultList.size());
        assertTrue(resultList.contains(cart1));
        assertTrue(resultList.contains(cart2));

        verify(cartRepository, times(1)).findAll();
    }

    // getGameFromCart

    @Test
    public void testGetGameFromCartValid() {
        Cart cart = new Cart();
        cart.setCart_id(VALID_CART_ID);

        Game game = new Game("Title", "Description", 50, GameStatus.InStock, 10, "photoUrl");
        game.setGame_id(VALID_GAME_ID);

        when(cartRepository.findById(VALID_CART_ID)).thenReturn(cart);

        cartService.addGameToCart(VALID_CART_ID, VALID_GAME_ID, 1);

        Game resultGame = cartService.getGameFromCart(VALID_CART_ID, VALID_GAME_ID);

        assertNotNull(resultGame);
        assertEquals(VALID_GAME_ID, resultGame.getGame_id());

        verify(cartRepository, atLeast(1)).findById(VALID_CART_ID);
    }

    @Test
    public void testGetGameFromCartGameNotInCart() {
        Cart cart = new Cart();
        cart.setCart_id(VALID_CART_ID);

        when(cartRepository.findById(VALID_CART_ID)).thenReturn(cart);

        GameShopException exception = assertThrows(GameShopException.class, () -> {
            cartService.getGameFromCart(VALID_CART_ID, VALID_GAME_ID);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(String.format("Game with ID %d does not exist in the cart", VALID_GAME_ID),
                exception.getMessage());

        verify(cartRepository, times(1)).findById(VALID_CART_ID);
    }
}
