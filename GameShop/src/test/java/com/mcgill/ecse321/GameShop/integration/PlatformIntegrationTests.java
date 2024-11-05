package com.mcgill.ecse321.GameShop.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import com.mcgill.ecse321.GameShop.dto.PlatformDto.PlatformListDto;
import com.mcgill.ecse321.GameShop.dto.PlatformDto.PlatformRequestDto;
import com.mcgill.ecse321.GameShop.dto.PlatformDto.PlatformResponseDto;
import com.mcgill.ecse321.GameShop.dto.PlatformDto.PlatformSummaryDto;
import com.mcgill.ecse321.GameShop.repository.PlatformRepository;
import com.mcgill.ecse321.GameShop.repository.ManagerRepository;
import com.mcgill.ecse321.GameShop.repository.AccountRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.*;
import org.springframework.boot.test.web.client.TestRestTemplate;

import com.mcgill.ecse321.GameShop.dto.AccountDtos.AccountRequestDto;
import com.mcgill.ecse321.GameShop.dto.AccountDtos.AccountResponseDto;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class PlatformIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private PlatformRepository platformRepo;

    @Autowired
    private ManagerRepository managerRepo;

    @Autowired
    private AccountRepository accountRepo;

    private int platformId;
    private static final String MANAGER_EMAIL = "manager2@example.com";
    private static final String MANAGER_PASSWORD = "managerPass";
    private static final String MANAGER_USERNAME = "managerUser";
    private static final String MANAGER_PHONE = "123-456-7890";
    private static final String MANAGER_ADDRESS = "123 Manager Street";
    private static final String PLATFORM_NAME = "PlayStation 5";
    private static final String UPDATED_PLATFORM_NAME = "PlayStation 5 Pro";
    private static final String SECOND_PLATFORM_NAME = "Xbox Series X";

    @AfterAll
    public void clearDatabase() {
        platformRepo.deleteAll();
        managerRepo.deleteAll();
        accountRepo.deleteAll();
    }

    @SuppressWarnings("null")
    @Test
    @Order(1)
    public void testCreateValidPlatform() {
        // Ensure manager exists
        AccountRequestDto manager = new AccountRequestDto(MANAGER_EMAIL, MANAGER_USERNAME, MANAGER_PASSWORD, MANAGER_PHONE, MANAGER_ADDRESS);
        ResponseEntity<AccountResponseDto> managerResponse = client.postForEntity("/account/manager", manager, AccountResponseDto.class);

        // Assert CREATED MANAGER
        assertNotNull(managerResponse);
        assertEquals(HttpStatus.OK, managerResponse.getStatusCode());
        AccountResponseDto managerDto = managerResponse.getBody();
        assertNotNull(managerDto);
        assertEquals(MANAGER_EMAIL, managerResponse.getBody().getEmail());

        // Arrange
        PlatformRequestDto request = new PlatformRequestDto(PLATFORM_NAME, MANAGER_EMAIL);

        // Act
        ResponseEntity<PlatformResponseDto> response = client.postForEntity("/platforms", request, PlatformResponseDto.class);

        // Assert CREATED PLATFORM
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        PlatformResponseDto platform = response.getBody();
        assertNotNull(platform);
        assertTrue(platform.getPlatformId() > 0);
        this.platformId = platform.getPlatformId();
        assertEquals(PLATFORM_NAME, platform.getPlatformName());
        assertEquals(MANAGER_EMAIL, platform.getManagerEmail());
    }

    @Test
    @Order(2)
    public void testGetPlatformById() {
        // Arrange
        String url = String.format("/platforms/%d", this.platformId);
        System.out.println(String.format("URL: %s", url));

        // Act
        ResponseEntity<PlatformResponseDto> response = client.getForEntity(url, PlatformResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        PlatformResponseDto platform = response.getBody();
        assertNotNull(platform);
        assertEquals(this.platformId, platform.getPlatformId());
        assertEquals(PLATFORM_NAME, platform.getPlatformName());
        assertEquals(MANAGER_EMAIL, platform.getManagerEmail());
    }

    @Test
    @Order(3)
    public void testUpdatePlatform() {
        // Arrange
        String url = String.format("/platforms/%d", this.platformId);
        System.out.println(String.format("URL: %s", url));
        PlatformRequestDto updateRequest = new PlatformRequestDto();
        updateRequest.setPlatformName(UPDATED_PLATFORM_NAME);
        HttpEntity<PlatformRequestDto> requestEntity = new HttpEntity<>(updateRequest);

        // Act
        ResponseEntity<PlatformResponseDto> updateResponse = client.exchange(url, HttpMethod.PUT, requestEntity, PlatformResponseDto.class);

        // Assert
        assertNotNull(updateResponse);
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        PlatformResponseDto updatedPlatform = updateResponse.getBody();
        assertNotNull(updatedPlatform);
        assertEquals(this.platformId, updatedPlatform.getPlatformId());
        assertEquals(UPDATED_PLATFORM_NAME, updatedPlatform.getPlatformName());
        assertEquals(MANAGER_EMAIL, updatedPlatform.getManagerEmail());
    }

    @Test
    @Order(4)
    public void testGetAllPlatforms() {
        // Arrange
        // Create another platform
        PlatformRequestDto request = new PlatformRequestDto(SECOND_PLATFORM_NAME, MANAGER_EMAIL);
        ResponseEntity<PlatformResponseDto> createResponse = client.postForEntity("/platforms", request, PlatformResponseDto.class);
        assertNotNull(createResponse);
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());

        // Act
        ResponseEntity<PlatformListDto> response = client.getForEntity("/platforms", PlatformListDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        PlatformListDto platforms = response.getBody();
        assertNotNull(platforms);
        List<PlatformSummaryDto> platformList = platforms.getPlatforms();
        assertNotNull(platformList);
        assertTrue(platformList.size() >= 2);
    }

    @Test
    @Order(5)
    public void testDeletePlatform() {
        // Arrange
        String url = String.format("/platforms/%d", this.platformId);

        // Act
        ResponseEntity<Void> response = client.exchange(url, HttpMethod.DELETE, null, Void.class);

        // Log for debugging
        System.out.println("Response status code for DELETE: " + response.getStatusCode());

        // Assert
        assertNotNull(response);
        // assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Verify Deletion by trying to fetch the platform again
        ResponseEntity<PlatformResponseDto> getResponse = client.getForEntity(url, PlatformResponseDto.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }
}
