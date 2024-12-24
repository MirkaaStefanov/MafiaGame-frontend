package com.example.MafiaGame_frontend.dtos;

import com.example.MafiaGame_frontend.enums.PlayerRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MafiaPlayerDTO {
    private Long id;
    private UserDTO user;
    private PlayerRole role;
    private boolean killed;
    private boolean removed;
    private boolean healed;
}
