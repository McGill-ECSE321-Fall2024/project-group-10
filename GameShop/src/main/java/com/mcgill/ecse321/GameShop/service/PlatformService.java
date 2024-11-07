package com.mcgill.ecse321.GameShop.service;


import java.util.List;

import org.antlr.v4.runtime.atn.ParseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mcgill.ecse321.GameShop.exception.GameShopException;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.Manager;
import com.mcgill.ecse321.GameShop.model.Platform;
import com.mcgill.ecse321.GameShop.repository.GameRepository;
import com.mcgill.ecse321.GameShop.repository.ManagerRepository;
import com.mcgill.ecse321.GameShop.repository.PlatformRepository;

import jakarta.transaction.Transactional;

@Service
public class PlatformService {
    @Autowired
    private PlatformRepository platformRepository;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private GameRepository gameRepository;

    @Transactional
    public Platform createPlatform(String platformName, String managerEmail) {
        if (platformName == null || platformName.trim().isEmpty()) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Platform name cannot be empty or null");
        }
        if (managerEmail == null || managerEmail.trim().isEmpty()) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Manager email cannot be empty or null");
        }

        Manager manager = managerRepository.findByEmail(managerEmail);
        if (manager == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND,
                    String.format("There is no manager with email: %s", managerEmail));
        }
        Platform platform = new Platform(platformName, manager);
        return platformRepository.save(platform);

    }

    @Transactional
    public Platform getPlatform(int platformId) {
        if (platformId <= 0) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Platform ID must be greater than 0");
        }
        Platform platform = platformRepository.findById(platformId);
        if (platform == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Platform does not exist");
        }
        return platform;
    }

    @Transactional
    public Iterable<Platform> getAllPlatforms() {
        return platformRepository.findAll();
    }

    @Transactional
    public Platform updatePlatform(int platformId, String platformName) {
        if (platformId <= 0) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Invalid platform ID");
        }
        if (platformName == null || platformName.trim().isEmpty()) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Platform name cannot be empty or null");
        }

        Platform platform = getPlatform(platformId);


        platform.setPlatformName(platformName);
        return platformRepository.save(platform);
    }

    @Transactional
    public void deletePlatform(int platformId) {
        if(platformId <= 0){
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Invalid platform ID");
        }
        Platform platform = getPlatform(platformId);
        List<Game> gamesInPlatform = gameRepository.findAllByPlatformsContains(platform);
        for(Game game : gamesInPlatform){
            game.getPlatforms().remove(platform);
            gameRepository.save(game);
        }
        
        platformRepository.delete(platform);
    }

    @Transactional
    public List<Game> getAllGamesInPlatform(int platformId) {
        if (platformId <= 0) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Invalid platform ID");
        }
        Platform platform = getPlatform(platformId);
        List<Game> games = gameRepository.findAllByPlatformsContains(platform);
        return games;
    }



}
