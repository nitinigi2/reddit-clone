package com.reddit.clone.controller;

import com.reddit.clone.dto.SubredditDto;
import com.reddit.clone.service.SubredditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/subreddit")
public class SubredditController {

    @Autowired
    private SubredditService subRedditService;

    @PostMapping
    public ResponseEntity<?> createSubreddit(@RequestBody SubredditDto subredditDto) {
        subRedditService.save(subredditDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(subredditDto);
    }

    @GetMapping
    public ResponseEntity<?> getAllSubreddits() {
        return ResponseEntity.status(HttpStatus.OK).body(subRedditService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSubreddit(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(subRedditService.getSubreddit(id));
    }
}
