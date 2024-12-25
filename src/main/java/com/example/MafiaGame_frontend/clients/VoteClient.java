package com.example.MafiaGame_frontend.clients;

import com.example.MafiaGame_frontend.dtos.ResultDTO;
import com.example.MafiaGame_frontend.dtos.VoteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "mafia-game-vote", url = "${backend.base-url}/vote")
public interface VoteClient {
    @PostMapping("/out")
    ResponseEntity<VoteDTO> vote(@PathVariable(name = "id") Long id, @RequestHeader("Authorization") String auth);

    @PostMapping("/all-votes")
    ResponseEntity<List<ResultDTO>>getAllVotes(@PathVariable(name = "id") Long id, @RequestHeader("Authorization") String auth);
}
