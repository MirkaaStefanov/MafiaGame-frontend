package com.example.MafiaGame_frontend.clients;


import com.example.MafiaGame_frontend.dtos.AuthenticationRequest;
import com.example.MafiaGame_frontend.dtos.AuthenticationResponse;
import com.example.MafiaGame_frontend.dtos.RefreshTokenBodyDTO;
import com.example.MafiaGame_frontend.dtos.RegisterRequest;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.io.IOException;

@FeignClient(name = "mafia-game-auth", url = "${backend.base-url}/auth")
public interface AuthenticationClient {
    @PostMapping("/register")
    AuthenticationResponse register(@RequestBody RegisterRequest request);

    @PostMapping("/authenticate")
    ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request);

    @PostMapping("/refresh-token")
    AuthenticationResponse refreshToken(@RequestBody RefreshTokenBodyDTO refreshTokenBody) throws IOException;

    @GetMapping("/logout")
    void logout(@RequestHeader("Authorization") String auth);
}
