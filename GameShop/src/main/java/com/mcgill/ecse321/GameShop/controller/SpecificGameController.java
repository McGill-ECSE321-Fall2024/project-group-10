package com.mcgill.ecse321.GameShop.controller;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
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

    @PutMapping("/specificGames/{id}")
    public void putMethod(@PathVariable int id, @PathVariable int game_id) {
        specificGameService.updateSpecficGame(id, game_id);
    }

    // Return Specific Game for return request taking as args (customer_id, specificGame_id)
}
