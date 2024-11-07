package com.mcgill.ecse321.GameShop.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import com.mcgill.ecse321.GameShop.exception.GameShopException;
import com.mcgill.ecse321.GameShop.model.Cart;
import com.mcgill.ecse321.GameShop.model.Customer;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.Platform;
import com.mcgill.ecse321.GameShop.model.WishList;
import com.mcgill.ecse321.GameShop.repository.WishListRepository;
import com.mcgill.ecse321.GameShop.repository.CustomerRepository;
import com.mcgill.ecse321.GameShop.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
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
public class WishListServiceTests {

    @Mock
    private WishListRepository wishListRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private WishListService wishListService;

    private static final int VALID_WISHLIST_ID = 2;
    private static final String VALID_CUSTOMER_EMAIL = "customer@example.com";
    private static final String VALID_TITLE = "My Wishlist";
    private static final String VALID_GAME_NAME = "Game Name";

    @Test
    public void testCreateWishlist_Successful() {
        String customerEmail = "test@example.com";
        String title = "My Wishlist";
        Cart cart = new Cart();

        Customer customer = new Customer(customerEmail, "customerUser", "customerPass", "123-456-7890",
                "123 Fake StMontreal", cart);

        when(customerRepository.findByEmail(customerEmail)).thenReturn(customer);
        when(wishListRepository.save(any(WishList.class))).thenAnswer((InvocationOnMock invocation) -> {
            WishList wishlist = invocation.getArgument(0);
            wishlist.setWishList_id(VALID_WISHLIST_ID);
            return wishlist;
        });

        WishList result = wishListService.createWishlist(customerEmail, title);

        assertNotNull(result);
        assertEquals(title, result.getTitle());
        assertEquals(customer, result.getCustomer());
        assertEquals(VALID_WISHLIST_ID, result.getWishList_id());

        verify(customerRepository, times(1)).findByEmail(customerEmail);
        verify(wishListRepository, times(1)).save(any(WishList.class));
    }

    @Test
    public void testCreateWishlistWithNullWishlistTitle() {
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            wishListService.createWishlist(VALID_CUSTOMER_EMAIL, null);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Wishlist title cannot be empty or null", exception.getMessage());
        verify(wishListRepository, never()).save(any(WishList.class));

    }

    @Test
    public void testCreateWishlistWithEmptyWishlistTitle() {
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            wishListService.createWishlist(VALID_CUSTOMER_EMAIL, "   ");
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Wishlist title cannot be empty or null", exception.getMessage());
        verify(wishListRepository, never()).save(any(WishList.class));
    }

    @Test
    public void testCreateWishlistWithNullCustomerEmail() {
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            wishListService.createWishlist(null, VALID_TITLE);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Customer email cannot be empty or null", exception.getMessage());
        verify(wishListRepository, never()).save(any(WishList.class));
    }

    @Test
    public void testCreateWishlistWithEmptyCustomerEmail() {
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            wishListService.createWishlist("   ", VALID_TITLE);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Customer email cannot be empty or null", exception.getMessage());
        verify(wishListRepository, never()).save(any(WishList.class));
    }

    @Test
    public void testCreateWishlistWithNonExistentCustomer() {
        when(customerRepository.findByEmail(VALID_CUSTOMER_EMAIL)).thenReturn(null);
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            wishListService.createWishlist(VALID_CUSTOMER_EMAIL, VALID_TITLE);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("There is no customer with email: " + VALID_CUSTOMER_EMAIL, exception.getMessage());
        verify(wishListRepository, never()).save(any(WishList.class));
        verify(customerRepository, times(1)).findByEmail(VALID_CUSTOMER_EMAIL);
    }

    @Test
    public void testFindWishlistByValidId() {
        Customer customer = new Customer(VALID_CUSTOMER_EMAIL + "s", "customerUser", "customerPass", "123-456-7890",
                "123 Fake StMontreal", new Cart());
        WishList wishList = new WishList(VALID_TITLE, customer);
        wishList.setWishList_id(94);
        when(wishListRepository.findById(94)).thenReturn(wishList);
        WishList result = wishListService.findWishlistById(94);
        assertNotNull(result);
        assertEquals(94, result.getWishList_id(), "Wishlist ID does not match");
        assertEquals(VALID_TITLE, result.getTitle());
        assertEquals(customer, result.getCustomer());
        verify(wishListRepository, times(1)).findById(94);
    }

    @Test
    public void testFindWishlistByInvalidId() {
        when(wishListRepository.findById(799)).thenReturn(null);
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            wishListService.findWishlistById(799);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("There is no WishList with Id 799.", exception.getMessage());
        verify(wishListRepository, times(1)).findById(799);
    }

    @Test
    public void testFindWishlistbyInvalidIdValue() {
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            wishListService.findWishlistById(-1);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Wishlist Id must be greater than 0", exception.getMessage());
        verify(wishListRepository, never()).findById(-1);
    }

    @Test
    public void testUpdateWishlistTitle_Successful() {
        Customer customer = new Customer(VALID_CUSTOMER_EMAIL + "q", "customerUser", "customerPass", "123-456-7890",
                "123 Fake StMontreal", new Cart());
        WishList wishList = new WishList(VALID_TITLE, customer);
        wishList.setWishList_id(13);
        when(wishListRepository.findById(13)).thenReturn(wishList);
        when(wishListRepository.save(any(WishList.class)))
                .thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));
        String newTitle = "New Title";
        WishList result = wishListService.updateWishlListTitle(13, newTitle);
        assertNotNull(result);
        assertEquals(13, result.getWishList_id());
        assertEquals(newTitle, result.getTitle());
        assertEquals(customer, result.getCustomer());
        verify(wishListRepository, times(1)).findById(13);
        verify(wishListRepository, times(1)).save(any(WishList.class));
    }
    @Test
    public void testUpdateWishListWithInvalidId(){
        int invalidId =9877;
        when(wishListRepository.findById(invalidId)).thenReturn(null);
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            wishListService.updateWishlListTitle(invalidId, "New Title");
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("There is no WishList with Id 9877.", exception.getMessage());
        verify(wishListRepository, times(1)).findById(invalidId);
        verify(wishListRepository, never()).save(any(WishList.class));
    }
    @Test
    public void testUpdateWishListWithInvalidIdValue(){
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            wishListService.updateWishlListTitle(-1, "New Title");
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Wishlist Id must be greater than 0", exception.getMessage());
        verify(wishListRepository, never()).findById(-1);
        verify(wishListRepository, never()).save(any(WishList.class));
    }
    @Test
    public void testUpdateWishListWithNullTitle(){
        Customer customer = new Customer(VALID_CUSTOMER_EMAIL + "w", "customerUser", "customerPass", "123-456-7890",
                "123 Fake StMontreal", new Cart());
        WishList wishList = new WishList(VALID_TITLE, customer);
        wishList.setWishList_id(30);
        // when(wishListRepository.findById(30)).thenReturn(wishList);
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            wishListService.updateWishlListTitle(30, null);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Wishlist title cannot be empty or null", exception.getMessage());
        // verify(wishListRepository, times(0)).findById(30);
        verify(wishListRepository, never()).save(any(WishList.class));
    }
    @Test
    public void testUpdateWishListWithEmptyTitle(){
        Customer customer = new Customer(VALID_CUSTOMER_EMAIL + "e", "customerUser", "customerPass", "123-456-7890",
                "123 Fake StMontreal", new Cart());
        WishList wishList = new WishList(VALID_TITLE, customer);
        wishList.setWishList_id(307);
        // when(wishListRepository.findById(307)).thenReturn(wishList);
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            wishListService.updateWishlListTitle(307, "   ");
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Wishlist title cannot be empty or null", exception.getMessage());
        // verify(wishListRepository, times(0)).findById(307);
        verify(wishListRepository, never()).save(any(WishList.class));
    }
    @Test
    public void testAddGameToWishlist_Successful(){
        Customer customer = new Customer(VALID_CUSTOMER_EMAIL + "r", "customerUser", "customerPass", "123-456-7890",
                "123 Fake StMontreal", new Cart());
        WishList wishList = new WishList(VALID_TITLE, customer);
        wishList.setWishList_id(40);
        Game game = new Game(VALID_GAME_NAME, "Game Description", 50, Game.GameStatus.InStock, 10, "photo.jpg");
        game.setGame_id(100);
        when(wishListRepository.findById(40)).thenReturn(wishList);
        when(gameRepository.findById(100)).thenReturn(game);
        when(wishListRepository.save(any(WishList.class)))
                .thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));
        WishList result = wishListService.addGameToWishlist(40, 100);
        assertNotNull(result);
        assertEquals(40, result.getWishList_id());
        assertEquals(1, result.getGames().size());
        assertTrue(result.getGames().contains(game));
        verify(wishListRepository, times(1)).findById(40);
        verify(gameRepository, times(1)).findById(100);
        verify(wishListRepository, times(1)).save(any(WishList.class));
    }
    @Test
    public void testAddGameToWishlistWithInvalidWishlistId(){
        int invalidId = 987;
        Game game = new Game(VALID_GAME_NAME, "Game Description", 50, Game.GameStatus.InStock, 10, "photo.jpg");
        game.setGame_id(101);
        // when(gameRepository.findById(101)).thenReturn(game);
        when(wishListRepository.findById(invalidId)).thenReturn(null);
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            wishListService.addGameToWishlist(invalidId, 100);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("There is no WishList with Id "+invalidId+".", exception.getMessage());
        verify(wishListRepository, times(1)).findById(invalidId);
        verify(gameRepository, never()).findById(100);
        verify(wishListRepository, never()).save(any(WishList.class));
    }
    @Test
    public void testAddGameToWishlistWithInvalidGameId(){
        int invalidId = 986;
        Customer customer = new Customer(VALID_CUSTOMER_EMAIL + "t", "customerUser", "customerPass", "123-456-7890",
                "123 Fake StMontreal", new Cart());
        WishList wishList = new WishList(VALID_TITLE, customer);
        wishList.setWishList_id(50);
        when(wishListRepository.findById(50)).thenReturn(wishList);
        when(gameRepository.findById(invalidId)).thenReturn(null);
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            wishListService.addGameToWishlist(50, invalidId);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("There is no Game with Id 986.", exception.getMessage());
        verify(wishListRepository, times(1)).findById(50);
        verify(gameRepository, times(1)).findById(invalidId);
        verify(wishListRepository, never()).save(any(WishList.class));
    }
    @Test
    public void testAddGameToWishlist_GameAlreadyInWishlist() {
        Customer customer = new Customer(VALID_CUSTOMER_EMAIL + "y", "customerUser", "customerPass", "123-456-7890",
                "123 Fake StMontreal", new Cart());
        WishList wishList = new WishList(VALID_TITLE, customer);
        wishList.setWishList_id(60);
        Game game = new Game(VALID_GAME_NAME, "Game Description", 50, Game.GameStatus.InStock, 10, "photo.jpg");
        game.setGame_id(102);
        wishList.addGame(game);
        when(wishListRepository.findById(60)).thenReturn(wishList);
        // when(gameRepository.findById(102)).thenReturn(game);
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            wishListService.addGameToWishlist(60, 102);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Game is already in the wishlist", exception.getMessage());
        verify(wishListRepository, times(1)).findById(60);
        verify(gameRepository, never()).findById(102);
        verify(wishListRepository, never()).save(any(WishList.class));
    }


}
