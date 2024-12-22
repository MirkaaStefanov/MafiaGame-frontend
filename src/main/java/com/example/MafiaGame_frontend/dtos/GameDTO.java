package com.example.MafiaGame_frontend.dtos;

import lombok.*;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GameDTO {
    private Long id;
    private List<Long> players;
    private boolean active;
}
