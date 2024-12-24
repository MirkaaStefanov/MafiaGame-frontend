package com.example.MafiaGame_frontend.controllers;

import com.example.MafiaGame_frontend.clients.MafiaPlayerClient;
import com.example.MafiaGame_frontend.dtos.MafiaPlayerDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/mafiaPlayer")
public class MafiaPlayerController {

    private final MafiaPlayerClient mafiaPlayerClient;
    private static final String SESSION_TOKEN = "sessionToken";

    @GetMapping("/role")
    public String showMyMafiaPlayerRole(@RequestParam Long gameId, Model model, HttpServletRequest request){
        String token = (String) request.getSession().getAttribute(SESSION_TOKEN);
        MafiaPlayerDTO mafiaPlayerDTO = mafiaPlayerClient.findMyPlayer(gameId,token);
        model.addAttribute("mafiaPlayerRole",mafiaPlayerDTO.getRole().toString());
        return "MafiaPlayer/role";
    }


}
