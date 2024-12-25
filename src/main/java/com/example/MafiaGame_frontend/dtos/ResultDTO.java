package com.example.MafiaGame_frontend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResultDTO {
    MafiaPlayerDTO votedPlayer;
    int voteCount;
}
