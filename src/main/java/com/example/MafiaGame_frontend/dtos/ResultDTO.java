package com.example.MafiaGame_frontend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResultDTO {

    private MafiaPlayerDTO killed;
    private boolean gameEnd;
    private boolean winners;
}
