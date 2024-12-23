package com.example.MafiaGame_frontend.controllers;

import com.example.MafiaGame_frontend.clients.GameClient;
import com.example.MafiaGame_frontend.dtos.GameDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/game")
public class GameController {
    private final GameClient gameClient;
    private static final String SESSION_TOKEN = "sessionToken";

    @GetMapping("/lobby")
    public String lobby(@RequestParam(name = "gameId") Long gameId, Model model){
        model.addAttribute("gameId", gameId);
        return "Game/lobby";
    }

    @PostMapping("/create")
    public String createGame(HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute(SESSION_TOKEN);

        GameDTO gameDTO = gameClient.createGame(token);
        Long gameId = gameDTO.getId();

        return "redirect:/lobby?gameId=" + gameId;
    }

    @GetMapping("/start")
    public String startGame(@RequestParam(name = "gameId") Long gameId, Model model){
        model.addAttribute("gameId", gameId);
        return "Game/start";
    }

    @PostMapping("/start")
    public String startGame(@RequestParam Long gameId, @RequestParam int killerQuantity, @RequestParam int doctorQuantity, @RequestParam int policeQuantity,  HttpServletRequest request){
        String token = (String) request.getSession().getAttribute(SESSION_TOKEN);

        gameClient.startGame(gameId, killerQuantity, doctorQuantity, policeQuantity, token);
        return "redirect:/game-page";
    }
}
