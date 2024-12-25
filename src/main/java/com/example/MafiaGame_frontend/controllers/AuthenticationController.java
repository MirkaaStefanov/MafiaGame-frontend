package com.example.MafiaGame_frontend.controllers;

import com.example.MafiaGame_frontend.clients.AuthenticationClient;
import com.example.MafiaGame_frontend.clients.UserClient;
import com.example.MafiaGame_frontend.dtos.AuthenticationRequest;
import com.example.MafiaGame_frontend.dtos.AuthenticationResponse;
import com.example.MafiaGame_frontend.dtos.UserDTO;
import com.example.MafiaGame_frontend.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationClient authenticationClient;
    private final SessionManager sessionManager;
    private static final String REDIRECTTXT = "redirect:/";
    private final UserClient userClient;


    @GetMapping("/")
    public String index(Model model,HttpServletRequest request){
        try {
            String token = (String) request.getSession().getAttribute("sessionToken");
            UserDTO userDTO = userClient.findAuthenticatedUser(token);
            model.addAttribute("gameId",userDTO.getGameId());
        }catch(Exception e){

        }
        return "index";

    }

    @GetMapping("/login")
    public String login(Model model, AuthenticationRequest authenticationRequest) {
        return "login";
    }


    @GetMapping("/logout")
    public ModelAndView logout(HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute("sessionToken");
        authenticationClient.logout(token);
        sessionManager.invalidateSession(request);
        return new ModelAndView(REDIRECTTXT);
    }

    @PostMapping("/login")
    public ModelAndView login(AuthenticationRequest authenticationRequest, HttpServletRequest httpServletRequest) {
        try {
            AuthenticationResponse authenticationResponse = authenticationClient.authenticate(authenticationRequest);
            sessionManager.setSessionToken(httpServletRequest, authenticationResponse.getAccessToken(), authenticationResponse.getUser().getRole().toString());
            return new ModelAndView(REDIRECTTXT);
        } catch (Exception e) {
            ModelAndView modelAndView = new ModelAndView("redirect:/login");
            modelAndView.addObject("error", "Невалидно име или парола");
            return modelAndView;
        }
    }
}
