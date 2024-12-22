package com.example.MafiaGame_frontend.clients;

import com.example.MafiaGame_frontend.dtos.AuthenticationResponse;
import com.example.MafiaGame_frontend.dtos.UserDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "mafia-game-user", url = "${backend.base-url}/user")
public interface UserClient {
    @GetMapping
    List<UserDTO> getAllUsers(@RequestHeader("Authorization") String auth);

    @GetMapping("/{id}")
    UserDTO getUserById(@PathVariable(name = "id") Long id, @RequestHeader("Authorization") String auth);

    @PostMapping
    UserDTO createUser(@Valid @RequestBody UserDTO user, @RequestHeader("Authorization") String auth);

    @PutMapping("/authenticated/{id}")
    AuthenticationResponse updateAuthenticatedUser(@PathVariable("id") Long id, @Valid @RequestBody UserDTO userDTO, @RequestHeader("Authorization") String auth);

    @PutMapping("/{id}")
    UserDTO updateUser(@PathVariable("id") Long id, @Valid @RequestBody UserDTO userDTO, @RequestHeader("Authorization") String auth);

    @DeleteMapping("/{id}")
    String deleteUserById(@PathVariable("id") Long id, @RequestHeader("Authorization") String auth);

    @GetMapping("/me")
    UserDTO findAuthenticatedUser(@RequestHeader("Authorization") String auth);

    @GetMapping("/ifPassMatch")
    boolean ifPassMatch(@RequestParam String password, @RequestHeader("Authorization") String auth);

    @PutMapping("/change-pass")
    boolean changePassword(@RequestBody UserDTO userDTO, @RequestHeader("Authorization") String auth);

    @PostMapping("/forgot-password")
     Boolean forgotPassword(@RequestParam String email);

    @PostMapping("/reset-password")
     Boolean processResetPassword(@RequestParam("token") String token,
                                                        @RequestParam("password") String newPassword);
}
