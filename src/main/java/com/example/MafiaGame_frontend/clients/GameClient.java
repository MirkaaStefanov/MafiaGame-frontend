package com.example.MafiaGame_frontend.clients;

import com.example.MafiaGame_frontend.dtos.GameDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "mafia-game-game", url = "${backend.base-url}/game")
public interface GameClient {

    @PostMapping("/create")
    GameDTO createGame(@RequestHeader("Authorization") String auth);

    @PostMapping("/enter")
    GameDTO enterGame(@RequestParam Long id, @RequestHeader("Authorization") String auth);

    @PostMapping("/start")
    void startGame(@RequestParam Long gameId, @RequestParam int killerQuantity, @RequestParam int doctorQuantity, @RequestParam int policeQuantity, @RequestHeader("Authorization") String auth);
}
