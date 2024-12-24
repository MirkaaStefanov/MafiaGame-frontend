package com.example.MafiaGame_frontend.controllers;

import com.example.MafiaGame_frontend.clients.GameClient;
import com.example.MafiaGame_frontend.clients.MafiaPlayerClient;
import com.example.MafiaGame_frontend.clients.UserClient;
import com.example.MafiaGame_frontend.dtos.GameDTO;
import com.example.MafiaGame_frontend.dtos.MafiaPlayerDTO;
import com.example.MafiaGame_frontend.dtos.UserDTO;
import com.example.MafiaGame_frontend.enums.PlayerRole;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/game")
public class GameController {

    private final GameClient gameClient;
    private static final String SESSION_TOKEN = "sessionToken";
    private final MafiaPlayerClient mafiaPlayerClient;

    @GetMapping("/lobby")
    public String lobby(@RequestParam(name = "gameId") Long gameId, Model model,HttpServletRequest request){
        String token = (String) request.getSession().getAttribute(SESSION_TOKEN);
        MafiaPlayerDTO mafiaPlayerDTO = mafiaPlayerClient.findMyPlayer(gameId,token);
        List<MafiaPlayerDTO> allMafiaPlayers = gameClient.allMafiaPlayersInGame(gameId,token);
        model.addAttribute("gameId", gameId);
        model.addAttribute("userId",mafiaPlayerDTO.getUser());
        model.addAttribute("allMafiaPlayers",allMafiaPlayers);
        model.addAttribute("token",token);
        if(mafiaPlayerDTO.getRole() == PlayerRole.NARRATOR){
            return "Game/lobby-narrator";
        }else{
            return "Game/lobby-player";
        }
    }

    @PostMapping("/create")
    public String createGame(HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute(SESSION_TOKEN);

        GameDTO gameDTO = gameClient.createGame(token);
        Long gameId = gameDTO.getId();

        return "redirect:/game/lobby?gameId=" + gameId;
    }

    @PostMapping("/start")
    public String startGame(@RequestParam Long gameId, @RequestParam int killerQuantity, @RequestParam int doctorQuantity, @RequestParam int policeQuantity,  HttpServletRequest request){
        String token = (String) request.getSession().getAttribute(SESSION_TOKEN);

        gameClient.startGame(gameId, killerQuantity, doctorQuantity, policeQuantity, token);
        return "redirect:/game-page";
    }
    @PostMapping("/enter")
    public  String enterGame(@RequestParam(name="gameId") Long id,HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute(SESSION_TOKEN);
        GameDTO gameDTO = gameClient.enterGame(id,token);
        Long gameId = gameDTO.getId();
        return "redirect:/game/lobby?gameId=" + gameId;
    }
}
