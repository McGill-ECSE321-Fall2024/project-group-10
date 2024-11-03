package com.mcgill.ecse321.GameShop.controller;
import java.util.ArrayList;
import java.util.List;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.mcgill.ecse321.GameShop.dto.GameDto.GameListDto;
import com.mcgill.ecse321.GameShop.dto.GameDto.GameRequestDto;
import com.mcgill.ecse321.GameShop.dto.GameDto.GameResponseDto;
import com.mcgill.ecse321.GameShop.dto.GameDto.GameSummaryDto;
import com.mcgill.ecse321.GameShop.dto.SpecificGameDto.SpecificGameListDto;
import com.mcgill.ecse321.GameShop.dto.SpecificGameDto.SpecificGameRequestDto;
import com.mcgill.ecse321.GameShop.dto.SpecificGameDto.SpecificGameResponseDto;
import com.mcgill.ecse321.GameShop.dto.SpecificGameDto.SpecificGameSummaryDto;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.SpecificGame;
import com.mcgill.ecse321.GameShop.service.GameService;
import com.mcgill.ecse321.GameShop.service.SpecificGameService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class SpecificGameController {
    
    @Autowired
    private SpecificGameService specificGameService;

    @Autowired
    private GameService GameService;

    @GetMapping("/specificGames/{specificGame_id}")
    public SpecificGameResponseDto findSpecificGameById(@PathVariable int specificGame_id) {
        SpecificGame specificGame = specificGameService.findSpecificGameById(specificGame_id);
        return SpecificGameResponseDto.create(specificGame);
    }

    @GetMapping("/specificGames")
    public SpecificGameListDto findAllSpecificGames() {
        List<SpecificGameSummaryDto> dtos = new ArrayList<SpecificGameSummaryDto>();

        for (SpecificGame specificGame : specificGameService.getAllSpecificGames()) {
            dtos.add(new SpecificGameSummaryDto(specificGame));
        }

        return new SpecificGameListDto(dtos);
    }

    @PostMapping("/specificGames")
    public SpecificGameResponseDto createSpecificGame(@Valid @RequestBody SpecificGameRequestDto request) {
        SpecificGame createdSpecificGame = specificGameService.createSpecificGame(GameService.findGameById(request.getGame_id()));
        return SpecificGameResponseDto.create(createdSpecificGame);
    }

    // TODO still have to implement PUT mapping for update
}
