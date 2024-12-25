package com.example.MafiaGame_frontend.controllers;

import com.example.MafiaGame_frontend.clients.GameClient;
import com.example.MafiaGame_frontend.clients.MafiaPlayerClient;
import com.example.MafiaGame_frontend.dtos.GameDTO;
import com.example.MafiaGame_frontend.dtos.MafiaPlayerDTO;
import com.example.MafiaGame_frontend.dtos.ResultDTO;
import com.example.MafiaGame_frontend.dtos.VoteResultDTO;
import com.example.MafiaGame_frontend.enums.PlayerRole;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private boolean killerFlag;
    private boolean healerFlag;

    @GetMapping("/lobby")
    public String lobby(@RequestParam(name = "gameId") Long gameId, Model model, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute(SESSION_TOKEN);
        MafiaPlayerDTO mafiaPlayerDTO = mafiaPlayerClient.findMyPlayer(gameId, token);
        List<MafiaPlayerDTO> allMafiaPlayers = gameClient.allMafiaPlayersInGame(gameId, token);
        model.addAttribute("gameId", gameId);
        model.addAttribute("userId", mafiaPlayerDTO.getUser());
        model.addAttribute("allMafiaPlayers", allMafiaPlayers);
        model.addAttribute("token", token);
        if (mafiaPlayerDTO.getRole() == PlayerRole.NARRATOR) {
            return "Game/lobby-narrator";
        } else {
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
    public String startGame(@RequestParam Long gameId, @RequestParam int killerQuantity, @RequestParam int doctorQuantity, @RequestParam int policeQuantity, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute(SESSION_TOKEN);

        gameClient.startGame(gameId, killerQuantity, doctorQuantity, policeQuantity, token);
        return "redirect:/game-page";
    }

    @PostMapping("/enter")
    public String enterGame(@RequestParam(name = "gameId") Long id, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute(SESSION_TOKEN);
        GameDTO gameDTO = gameClient.enterGame(id, token);
        Long gameId = gameDTO.getId();
        return "redirect:/game/lobby?gameId=" + gameId;
    }

    @PostMapping("/exit")
    public String exitGame(HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute(SESSION_TOKEN);
        gameClient.exitGame(token);
        return "redirect:/";
    }

    @GetMapping("/night")
    public String night(HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute(SESSION_TOKEN);
        List<MafiaPlayerDTO> allDoctors = mafiaPlayerClient.allDoctors(token);

        if (allDoctors.isEmpty()) {
            healerFlag = true;
        }

        if (!killerFlag) {
            killerFlag = true;
            return "redirect:/killer";
        }
        if (!healerFlag) {
            healerFlag = true;
            return "redirect:/healer";
        }

        killerFlag = false;
        healerFlag = false;
        return "redirect:/morning";
    }


    @GetMapping("/killer")
    public String killer(Model model, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute(SESSION_TOKEN);
        List<MafiaPlayerDTO> mafiaPlayers = mafiaPlayerClient.allMafiaPlayersThatPlay(token);
        List<MafiaPlayerDTO> killers = mafiaPlayerClient.allKillers(token);

        mafiaPlayers.remove(killers);

        model.addAttribute("mafiaPlayers", mafiaPlayers);
        return "Game/killer";
    }

    @PostMapping("/killer")
    public String postKiller(@RequestParam("playerId") Long playerId, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute(SESSION_TOKEN);
        gameClient.kill(playerId, token);
        return "redirect:/game/night";
    }

    @GetMapping("/heal")
    public String heal(Model model, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute(SESSION_TOKEN);
        List<MafiaPlayerDTO> mafiaPlayers = mafiaPlayerClient.allMafiaPlayersThatPlay(token);

        model.addAttribute("mafiaPlayers", mafiaPlayers);
        return "Game/killer";
    }

    @PostMapping("/heal")
    public String heal(@RequestParam("playerId") Long playerId, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute(SESSION_TOKEN);
        gameClient.heal(playerId, token);
        return "redirect:/game/night";
    }

    @PostMapping("/result")
    public String result(HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute(SESSION_TOKEN);
        ResultDTO resultDTO = gameClient.resultInTheMorning(token);

        // Add the ResultDTO to the redirect attributes
        request.getSession().setAttribute("resultDTO", resultDTO);

        // Redirect to the GET mapping
        return "redirect:/game/result";
    }

    @GetMapping("/result")
    public String result(Model model, HttpServletRequest request) {
        ResultDTO resultDTO = (ResultDTO) request.getSession().getAttribute("resultDTO");
        if (resultDTO.isGameEnd()) {
            if (resultDTO.isWinners()) {
                //TODO to get the killers of the game
                return "Game/killers-win";
            } else {
                //TODO to get the players except killers of the game
                return "Game/villagers-win";
            }
        }
        model.addAttribute("killed", resultDTO.getKilled());
        return "Game/result-page"; // Return the name of the result view
    }
}
