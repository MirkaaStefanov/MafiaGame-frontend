package com.example.MafiaGame_frontend.controllers;

import com.example.MafiaGame_frontend.clients.VoteClient;
import com.example.MafiaGame_frontend.dtos.VoteDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/vote")
public class VoteController {
    VoteClient voteClient;
    private static final String SESSION_TOKEN = "sessionToken";

    /*@GetMapping("/out")
    public String vote(@PathVariable(name = "id") Long id, Model model, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute(SESSION_TOKEN);
        model.addAttribute("voted", )
        return "Vote/voting";
    }
    @PostMapping("/out")
    public void submitVote(@PathVariable(name = "id") Long id, Model model, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute(SESSION_TOKEN);
        try {
            VoteDTO voteDTO = voteClient.vote(id, token).getBody();
            model.addAttribute("")

        } catch (Exception ex)
    }
    @PostMapping("/all-votes")
            public ResponseEntity<>
    }*/
}
