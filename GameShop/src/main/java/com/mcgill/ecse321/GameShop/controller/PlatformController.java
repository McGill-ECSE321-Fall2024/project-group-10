package com.mcgill.ecse321.GameShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.mcgill.ecse321.GameShop.dto.GameDto.GameListDto;
import com.mcgill.ecse321.GameShop.dto.GameDto.GameSummaryDto;
import com.mcgill.ecse321.GameShop.dto.PlatformDto.PlatformListDto;
import com.mcgill.ecse321.GameShop.dto.PlatformDto.PlatformRequestDto;
import com.mcgill.ecse321.GameShop.dto.PlatformDto.PlatformResponseDto;
import com.mcgill.ecse321.GameShop.dto.PlatformDto.PlatformSummaryDto;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.Platform;
import com.mcgill.ecse321.GameShop.service.PlatformService;

import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;


@RestController
public class PlatformController {

    @Autowired
    private PlatformService platformService;

    /** Create a new platform */
    @PostMapping("/platforms")
    public PlatformResponseDto createPlatform(@Valid @RequestBody PlatformRequestDto request) {
        Platform platform = platformService.createPlatform(request.getPlatformName(), request.getManagerEmail());
        return PlatformResponseDto.create(platform);
    }

    /** Get a platform by ID */
    @GetMapping("/platforms/{pid}")
    public PlatformResponseDto getPlatformById(@PathVariable int pid) {
        Platform platform = platformService.getPlatform(pid);
        return PlatformResponseDto.create(platform);
    }

    /** Get all platforms */
    @GetMapping("/platforms")
    public PlatformListDto getAllPlatforms() {
        List<PlatformSummaryDto> dtos = new ArrayList<PlatformSummaryDto>();
        for (Platform platform : platformService.getAllPlatforms()) {
            dtos.add(new PlatformSummaryDto(platform));
        }
        return new PlatformListDto(dtos);
    }
    public GameListDto getAllGamesInPlatform(@PathVariable int pid) {
        List<GameSummaryDto> dtos = new ArrayList<GameSummaryDto>();
        for (Game game : platformService.getAllGamesInPlatform(pid)) {
            dtos.add(new GameSummaryDto(game));
        }
        return new GameListDto(dtos);
    }

    /** Update a platform's name */
    @PutMapping("/platforms/{id}")
    public PlatformResponseDto updatePlatform(@PathVariable int id, @RequestBody PlatformRequestDto request) {
        Platform platform = platformService.updatePlatform(id, request.getPlatformName());
        return PlatformResponseDto.create(platform);
    }

    /** Delete a platform */
    @DeleteMapping("/platforms/{id}")
    public void deletePlatform(@PathVariable int id) {
        platformService.deletePlatform(id);
    }


}
