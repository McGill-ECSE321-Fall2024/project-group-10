package com.mcgill.ecse321.GameShop.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;

import com.mcgill.ecse321.GameShop.exception.GameShopException;
import com.mcgill.ecse321.GameShop.model.Cart;
import com.mcgill.ecse321.GameShop.model.Customer;
import com.mcgill.ecse321.GameShop.model.Game;

import com.mcgill.ecse321.GameShop.model.WishList;
import com.mcgill.ecse321.GameShop.repository.WishListRepository;
import com.mcgill.ecse321.GameShop.repository.CustomerRepository;
import com.mcgill.ecse321.GameShop.repository.GameRepository;

import org.junit.jupiter.api.Test;

import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;

import org.mockito.quality.Strictness;


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
    public void testAddGameToWishlistWithInvalidGameIdNegative(){
        int invalidId2 = -2;
        Customer customer = new Customer(VALID_CUSTOMER_EMAIL + "tbn", "customerUser", "customerPass", "123-456-7890",
                "123 Fake StMontreal", new Cart());
        WishList wishList = new WishList(VALID_TITLE, customer);
        wishList.setWishList_id(55);
        // when(wishListRepository.findById(55)).thenReturn(wishList);
        // when(gameRepository.findById(invalidId2)).thenReturn(null);
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            wishListService.addGameToWishlist(55, invalidId2);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Game Id must be greater than 0", exception.getMessage());
        verify(wishListRepository, times(0)).findById(55);
        verify(gameRepository, times(0)).findById(invalidId2);
        verify(wishListRepository, never()).save(any(WishList.class));
    }
    @Test
    public void testAddGameToWishlistWithInvalidWishlistIdNegative(){
        int invalidId2 = -2;
        Customer customer = new Customer(VALID_CUSTOMER_EMAIL + "tbsn", "customerUser", "customerPass", "123-456-7890",
                "123 Fake StMontreal", new Cart());
        WishList wishList = new WishList(VALID_TITLE, customer);
        wishList.setWishList_id(invalidId2);
        
        // when(wishListRepository.findById(55)).thenReturn(wishList);
        // when(gameRepository.findById(invalidId2)).thenReturn(null);
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            wishListService.addGameToWishlist(invalidId2, 55);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Wishlist Id must be greater than 0", exception.getMessage());
        verify(wishListRepository, times(0)).findById(invalidId2);
        verify(gameRepository, times(0)).findById(55);
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

    @Test
    public void testRemoveGameFromWishlist() {
        Customer customer = new Customer(VALID_CUSTOMER_EMAIL + "u", "customerUser", "customerPass", "123-456-7890",
                "123 Fake StMontreal", new Cart());
        int wishlistId = 1;
        int gameId = 789;
    
        WishList wishlist = new WishList("Wishlist", customer);
        wishlist.setWishList_id(wishlistId);
        
        // Add the game to the wishlist and verify addition
        Game game = new Game("Game", "Description", 50, Game.GameStatus.InStock, 10, "photo.jpg");
        game.setGame_id(gameId);
        wishlist.addGame(game);

        assertTrue(wishlist.getGames().contains(game));
        
        // Mock repository responses
        when(wishListRepository.findById(wishlistId)).thenReturn(wishlist);
    
        
        // Act: Remove the game from the wishlist
        wishListService.removeGameFromWishlist(wishlistId, gameId);
    
        // Assert the game is removed
        verify(wishListRepository, times(1)).save(wishlist);
        assertFalse(wishlist.getGames().contains(game));
        assertEquals(0, wishlist.getGames().size());
        verify(wishListRepository, times(1)).findById(wishlistId);
    }




    @Test
    public void testRemoveGameFromWishlistInvalidWishlist() {
        // Arrange: Define the invalid wishlist and game IDs
        int wishlistId = 550;
        int gameId = 550;
    
        // Mock repository to return null when the wishlist ID is not found
        when(wishListRepository.findById(wishlistId)).thenReturn(null);
    
        // Act & Assert: Verify that the correct exception is thrown
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            wishListService.removeGameFromWishlist(wishlistId, gameId);
        });
    
        // Verify the exception's status and message
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(String.format("There is no WishList with Id %d.", wishlistId), exception.getMessage());
    
        // Verify that save is never called
        verify(wishListRepository, never()).save(any(WishList.class));
        verify(wishListRepository, times(1)).findById(wishlistId);
    }

@Test
public void testRemoveGameFromWishlistInvalidGame() {
    // Arrange
    int wishlistId = 89;
    int gameId = 89;
    Customer customer = new Customer(VALID_CUSTOMER_EMAIL + "os", "customerUser", "customerPass", "123-456-7890",
    "123 Fake StMontreal", new Cart());

    WishList wishlist = new WishList("Wishlist", customer);
    wishlist.setWishList_id(wishlistId);

    // Mock repository to return the wishlist but no game
    when(wishListRepository.findById(wishlistId)).thenReturn(wishlist);
    // when(gameRepository.findById(gameId)).thenReturn(null);

    // Act & Assert
    GameShopException exception = assertThrows(GameShopException.class, () -> {
        wishListService.removeGameFromWishlist(wishlistId, gameId);
    });

    // Verify the exception details
    assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    assertEquals(String.format("There is no Game with Id %d in the WishList with Id %d.", gameId, wishlistId), exception.getMessage());

    // Verify repository interactions
    verify(wishListRepository, times(1)).findById(wishlistId);
    // verify(gameRepository, times(1)).findById(gameId);
    verify(wishListRepository, never()).save(any(WishList.class));
}
@Test
public void testRemoveGameFromWishlistInvalidGameNegative() {
    // Arrange
    int wishlistId = 89;
    int gameId = -2;
    Customer customer = new Customer(VALID_CUSTOMER_EMAIL + "ossdfgs", "customerUser", "customerPass", "123-456-7890",
    "123 Fake StMontreal", new Cart());

    WishList wishlist = new WishList("Wishlist", customer);
    wishlist.setWishList_id(wishlistId);

    // Mock repository to return the wishlist but no game
    // when(wishListRepository.findById(wishlistId)).thenReturn(wishlist);
    // when(gameRepository.findById(gameId)).thenReturn(null);

    // Act & Assert
    GameShopException exception = assertThrows(GameShopException.class, () -> {
        wishListService.removeGameFromWishlist(wishlistId, gameId);
    });

    // Verify the exception details
    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    assertEquals("Game Id must be greater than 0", exception.getMessage());

    // Verify repository interactions
    verify(wishListRepository, times(0)).findById(wishlistId);
    // verify(gameRepository, times(1)).findById(gameId);
    verify(wishListRepository, never()).save(any(WishList.class));
}
@Test
public void testRemoveGameFromWishlistInvalidWishlistNegative() {
    // Arrange
    int wishlistId = -3;
    int gameId = 71;
    Customer customer = new Customer(VALID_CUSTOMER_EMAIL + "osstjyutyujidfgs", "customerUser", "customerPass", "123-456-7890",
    "123 Fake StMontreal", new Cart());

    WishList wishlist = new WishList("Wishlist", customer);
    wishlist.setWishList_id(wishlistId);

    // Mock repository to return the wishlist but no game
    // when(wishListRepository.findById(wishlistId)).thenReturn(wishlist);
    // when(gameRepository.findById(gameId)).thenReturn(null);

    // Act & Assert
    GameShopException exception = assertThrows(GameShopException.class, () -> {
        wishListService.removeGameFromWishlist(wishlistId, gameId);
    });

    // Verify the exception details
    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    assertEquals("Wishlist Id must be greater than 0", exception.getMessage());

    // Verify repository interactions
    verify(wishListRepository, times(0)).findById(wishlistId);
    // verify(gameRepository, times(1)).findById(gameId);
    verify(wishListRepository, never()).save(any(WishList.class));
}

@Test
public void testGetWishlistSize_Successful() {
    Customer customer = new Customer(VALID_CUSTOMER_EMAIL, "customerUser", "customerPass", "123-456-7890",
            "123 Fake StMontreal", new Cart());
    WishList wishlist = new WishList("Wishlist", customer);
    wishlist.setWishList_id(42);

    Game game1 = new Game("Game1", "Description1", 50, Game.GameStatus.InStock, 10, "photo1.jpg");
    Game game2 = new Game("Game2", "Description2", 60, Game.GameStatus.InStock, 5, "photo2.jpg");
    wishlist.addGame(game1);
    wishlist.addGame(game2);

    when(wishListRepository.findById(42)).thenReturn(wishlist);

    int size = wishListService.getWishlistSize(42);

    assertEquals(2, size, "Wishlist size does not match expected value");
    verify(wishListRepository, times(1)).findById(42);
}

@Test
public void testGetWishlistSize_InvalidWishlistId() {
    int invalidWishlistId = -1;

    GameShopException exception = assertThrows(GameShopException.class, () -> {
        wishListService.getWishlistSize(invalidWishlistId);
    });

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    assertEquals("Wishlist Id must be greater than 0", exception.getMessage());
    verify(wishListRepository, never()).findById(invalidWishlistId);
}
@Test
public void testGetGamesInWishList_Successful() {
    Customer customer = new Customer(VALID_CUSTOMER_EMAIL +"lk", "customerUser", "customerPass", "123-456-7890",
            "123 Fake StMontreal", new Cart());
    WishList wishlist = new WishList("Wishlist", customer);
    wishlist.setWishList_id(36);

    Game game1 = new Game("Game1", "Description1", 50, Game.GameStatus.InStock, 10, "photo1.jpg");
    Game game2 = new Game("Game2", "Description2", 60, Game.GameStatus.InStock, 5, "photo2.jpg");
    wishlist.addGame(game1);
    wishlist.addGame(game2);

    when(wishListRepository.findById(36)).thenReturn(wishlist);

    List<Game> games = wishListService.getGamesInWishList(36);

    assertNotNull(games);
    assertEquals(2, games.size());
    assertTrue(games.contains(game1));
    assertTrue(games.contains(game2));
    verify(wishListRepository, times(1)).findById(36);
}

@Test
public void testGetGamesInWishList_InvalidWishlistId() {
    int invalidWishlistId = -1;

    GameShopException exception = assertThrows(GameShopException.class, () -> {
        wishListService.getGamesInWishList(invalidWishlistId);
    });

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    assertEquals("Wishlist Id must be greater than 0", exception.getMessage());
    verify(wishListRepository, never()).findById(invalidWishlistId);
}
@Test
public void testGetGamesInWishList_InvalidWishlistIdNotFound() {
    int invalidWishlistId = 4513;
    
        when(wishListRepository.findById(invalidWishlistId)).thenReturn(null);

    GameShopException exception = assertThrows(GameShopException.class, () -> {
        wishListService.getGamesInWishList(invalidWishlistId);
    });

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    assertEquals("There is no WishList with Id 4513.", exception.getMessage());
    verify(wishListRepository, times(1)).findById(invalidWishlistId);
}
@Test
public void testRemoveAllGamesFromWishlist_Successful() {
    Customer customer = new Customer(VALID_CUSTOMER_EMAIL+"asaber", "customerUser", "customerPass", "123-456-7890",
            "123 Fake StMontreal", new Cart());
    WishList wishlist = new WishList("Wishlist", customer);
    wishlist.setWishList_id(14);

    Game game1 = new Game("Game1", "Description1", 50, Game.GameStatus.InStock, 10, "photo1.jpg");
    Game game2 = new Game("Game2", "Description2", 60, Game.GameStatus.InStock, 5, "photo2.jpg");
    wishlist.addGame(game1);
    wishlist.addGame(game2);

    when(wishListRepository.findById(14)).thenReturn(wishlist);
    when(wishListRepository.save(any(WishList.class)))
            .thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

    WishList updatedWishlist = wishListService.removeAllGamesFromWishlist(14);

    assertNotNull(updatedWishlist);
    assertEquals(0, updatedWishlist.getGames().size());
    verify(wishListRepository, times(1)).save(wishlist);
    verify(wishListRepository, times(1)).findById(14);
}

@Test
public void testRemoveAllGamesFromWishlist_InvalidWishlistId() {
    int invalidWishlistId = -1;

    GameShopException exception = assertThrows(GameShopException.class, () -> {
        wishListService.removeAllGamesFromWishlist(invalidWishlistId);
    });

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    assertEquals("Wishlist Id must be greater than 0", exception.getMessage());
    verify(wishListRepository, never()).save(any(WishList.class));
    verify(wishListRepository, never()).findById(invalidWishlistId);
}

@Test
public void testGetGameInWishList_Successful() {
    Customer customer = new Customer(VALID_CUSTOMER_EMAIL+"asaber6", "customerUser", "customerPass", "123-456-7890",
            "123 Fake StMontreal", new Cart());
    WishList wishlist = new WishList("Wishlist", customer);
    wishlist.setWishList_id(967);

    Game game = new Game("Game1", "Description1", 50, Game.GameStatus.InStock, 10, "photo1.jpg");
    game.setGame_id(967);
    wishlist.addGame(game);

    when(wishListRepository.findById(967)).thenReturn(wishlist);

    Game result = wishListService.getGameInWishList(967, 967);

    assertNotNull(result);
    assertEquals(967, result.getGame_id());
    assertEquals("Game1", result.getTitle());
    verify(wishListRepository, times(1)).findById(967);
}

@Test
public void testGetGameInWishList_GameNotFound() {
    Customer customer = new Customer(VALID_CUSTOMER_EMAIL+"asayy", "customerUser", "customerPass", "123-456-7890",
            "123 Fake StMontreal", new Cart());
    WishList wishlist = new WishList("Wishlist", customer);
    wishlist.setWishList_id(966);

    when(wishListRepository.findById(966)).thenReturn(wishlist);

    GameShopException exception = assertThrows(GameShopException.class, () -> {
        wishListService.getGameInWishList(966, 966);
    });

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    assertEquals(String.format("There is no Game with Id %d in the WishList with Id %d.", 966, 966), exception.getMessage());
    verify(wishListRepository, times(1)).findById(966);
}

@Test
public void testGetGameInWishList_InvalidWishlistId() {
    int invalidWishlistId = -1;

    GameShopException exception = assertThrows(GameShopException.class, () -> {
        wishListService.getGameInWishList(invalidWishlistId, 966);
    });

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    assertEquals("Wishlist Id must be greater than 0", exception.getMessage());
    verify(wishListRepository, never()).findById(invalidWishlistId);
}









}
