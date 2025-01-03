package com.example.MafiaGame_frontend.clients;


import com.example.MafiaGame_frontend.dtos.MafiaPlayerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "mafia-game-mafiaPlayer", url = "${backend.base-url}/mafiaPlayer")
public interface MafiaPlayerClient {

    @GetMapping("/findMyPlayer")
    MafiaPlayerDTO findMyPlayer(@RequestParam Long id, @RequestHeader("Authorization") String auth);

    @GetMapping("/findAllPlayers")
    List<MafiaPlayerDTO> allMafiaPlayersThatPlay(@RequestHeader("Authorization") String auth);

    @GetMapping("/findAllKillers")
    List<MafiaPlayerDTO> allKillers(@RequestHeader("Authorization") String auth);

    @PostMapping("/findAllDoctors")
    List<MafiaPlayerDTO> allDoctors(@RequestHeader("Authorization") String auth);
}
