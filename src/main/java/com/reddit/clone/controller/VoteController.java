package com.reddit.clone.controller;

import com.reddit.clone.dto.VoteDto;
import com.reddit.clone.service.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/votes")
@AllArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<?> createVote(@RequestBody VoteDto voteDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(voteService.save(voteDto));
    }
}
