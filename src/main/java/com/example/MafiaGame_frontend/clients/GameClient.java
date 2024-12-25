package com.example.MafiaGame_frontend.clients;

import com.example.MafiaGame_frontend.dtos.GameDTO;
import com.example.MafiaGame_frontend.dtos.MafiaPlayerDTO;
import com.example.MafiaGame_frontend.dtos.ResultDTO;
import com.example.MafiaGame_frontend.dtos.VoteResultDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "mafia-game-game", url = "${backend.base-url}/game")
public interface GameClient {

    @PostMapping("/create")
    GameDTO createGame(@RequestHeader("Authorization") String auth);

    @PostMapping("/enter")
    GameDTO enterGame(@RequestParam Long id, @RequestHeader("Authorization") String auth);

    @PostMapping("/start")
    void startGame(@RequestParam Long gameId, @RequestParam int killerQuantity, @RequestParam int doctorQuantity, @RequestParam int policeQuantity, @RequestHeader("Authorization") String auth);

    @GetMapping("/players")
    List<MafiaPlayerDTO> allMafiaPlayersInGame(@RequestParam(name = "gameId") Long gameId, @RequestHeader("Authorization") String auth);

    @PostMapping("/exit")
    void exitGame(@RequestHeader("Authorization") String auth);

    @PostMapping("/kill")
    void kill(@RequestParam Long playerId, @RequestHeader("Authorization") String auth);

    @PostMapping("/heal")
    void heal(@RequestParam Long playerId ,@RequestHeader("Authorization") String auth);

    @GetMapping("/morningResult")
    ResultDTO resultInTheMorning(@RequestHeader("Authorization") String auth);
}
