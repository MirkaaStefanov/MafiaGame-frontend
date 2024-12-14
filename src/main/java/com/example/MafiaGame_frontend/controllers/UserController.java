package com.example.MafiaGame_frontend.controllers;

import com.example.MafiaGame_frontend.clients.UserClient;
import com.example.MafiaGame_frontend.dtos.AuthenticationResponse;
import com.example.MafiaGame_frontend.dtos.UserDTO;
import com.example.MafiaGame_frontend.enums.Role;
import com.example.MafiaGame_frontend.models.User;
import com.example.MafiaGame_frontend.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user")
public class UserController {
    private static final String REDIRECTTXT = "redirect:/user/show";
    private static final String REDIRECTTXT2 = "redirect:/user/profile";
    private static final String SESSION_TOKEN = "sessionToken";
    private static final String ERRORTXT = "error";

    private final UserClient userClient;

    private final SessionManager sessionManager;

    @GetMapping("/show")
    public String index(Model model, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute(SESSION_TOKEN);

        List<UserDTO> users = userClient.getAllUsers(token);

        model.addAttribute("users", users);
        return "User/show";
    }

    @GetMapping("/create")
    String createUser(Model model, HttpServletRequest request, @ModelAttribute("emailAlreadyExists") String message) {
        String token = (String) request.getSession().getAttribute(SESSION_TOKEN);



        User user = new User();

        model.addAttribute("user", user);
        model.addAttribute("emailAlreadyExists", message);
        return "User/create";
    }

    @PostMapping("/submit")
    public ModelAndView submitUser(@ModelAttribute("user") UserDTO user, HttpServletRequest request, Model model) {
        String token = (String) request.getSession().getAttribute(SESSION_TOKEN);

        try {
            UserDTO createdUser = userClient.createUser(user, token);

        } catch (Exception ex) {
            model.addAttribute("user", user);
            model.addAttribute("emailAlreadyExists", "Потребител с този имейл вече съществува!");
            return new ModelAndView("User/create");
        }
        return new ModelAndView(REDIRECTTXT);
    }

    @GetMapping("/editUser/{id}")
    String editUser(@PathVariable(name = "id") Long id, Model model, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute(SESSION_TOKEN);
        UserDTO existingUser = userClient.getUserById(id, token);
        model.addAttribute("user", existingUser);

        UserDTO authenticatedUser = userClient.findAuthenticatedUser(token);

        if (existingUser.equals(authenticatedUser)) {
            return "User/profile-edit";
        }
        return "User/edit";
    }

    @PostMapping("/edit/{id}")
    ModelAndView editSubmitUser(@PathVariable(name = "id") Long id, UserDTO userDTO, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute(SESSION_TOKEN);
        try {
            UserDTO authenticatedUser = userClient.findAuthenticatedUser(token);
            UserDTO existingUser = userClient.getUserById(id, token);



            if (existingUser.equals(authenticatedUser)) {
                AuthenticationResponse authenticationResponse = userClient.updateAuthenticatedUser(id, userDTO, token);
                sessionManager.setSessionToken(request, authenticationResponse.getAccessToken(), authenticationResponse.getUser().getRole().toString());
                return new ModelAndView(REDIRECTTXT2);
            }
            userClient.updateUser(id, userDTO, token);
        } catch (Exception ex) {
            ModelAndView modelAndView = new ModelAndView("redirect:/user/editUser/" + userDTO.getId());
            modelAndView.addObject(ERRORTXT, "Потребител с този имейл вече съществува!");
            return modelAndView;
        }


        return new ModelAndView(REDIRECTTXT);
    }


    @PostMapping("/delete/{id}")
    ModelAndView deleteClientById(@PathVariable("id") Long id, Model model, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute(SESSION_TOKEN);
        userClient.deleteUserById(id, token);
        return new ModelAndView(REDIRECTTXT);
    }

    @GetMapping("/profile")
    String showProfile(Model model, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute(SESSION_TOKEN);
        UserDTO user = userClient.findAuthenticatedUser(token);
        model.addAttribute("user", user);
        return "userinfo";
    }

    @GetMapping("/password")
    String ifPassMatch(Model model) {
        model.addAttribute("user", new UserDTO());
        return "User/pass-match";

    }

    @GetMapping("/ifPassMatch")
    ModelAndView ifPassMatch(@ModelAttribute("user") UserDTO userDTO, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute(SESSION_TOKEN);
        boolean ifPassMatch = userClient.ifPassMatch(userDTO.getPassword(), token);
        if (!ifPassMatch) {
            ModelAndView modelAndView = new ModelAndView("redirect:/user/password");
            modelAndView.addObject(ERRORTXT, "Грешна парола!");
            return modelAndView;
        }
        return new ModelAndView("redirect:/user/change-password");
    }

    @GetMapping("/change-password")
    String changePassword(Model model) {
        model.addAttribute("user", new UserDTO());
        return "User/change-password";
    }

    @PostMapping("/change-password")
    ModelAndView changePassword(@ModelAttribute UserDTO userDTO, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute(SESSION_TOKEN);
        boolean response = userClient.changePassword(userDTO, token);
        if (response) {
            return new ModelAndView(REDIRECTTXT2);
        }
        ModelAndView modelAndView = new ModelAndView("redirect:/user/change-password");
        modelAndView.addObject(ERRORTXT, "Паролите не съвпадат!");
        return modelAndView;
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "User/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        boolean result = userClient.forgotPassword(email);
        if (result) {
            model.addAttribute("message", "Линк за възобновяване на парола беше изпратен успешно.");
        } else
            model.addAttribute(ERRORTXT, "Няма потребител регистриран с този email адрес.");
        return "User/forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordPage(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "User/reset-password";
    }

    @PostMapping("/reset-password")
    public ModelAndView processResetPassword(@RequestParam("token") String token,
                                             @RequestParam("password") String password,
                                             @RequestParam("confirmPassword") String confirmPassword) {
        ModelAndView modelAndView = new ModelAndView("redirect:/login");

        Boolean isPasswordResetSuccessful = userClient.processResetPassword(token, password);
        if (Boolean.TRUE.equals(isPasswordResetSuccessful)) {
            modelAndView.addObject("message", "Паролата е променена успешно!");
        } else {
            modelAndView.addObject(ERRORTXT, "Паролата не е променена успешно! Опитайте отново.");
        }
        return modelAndView;
    }

}

