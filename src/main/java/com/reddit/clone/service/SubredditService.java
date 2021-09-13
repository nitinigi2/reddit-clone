package com.reddit.clone.service;

import com.reddit.clone.dto.SubredditDto;
import com.reddit.clone.exception.SubRedditNotFoundException;
import com.reddit.clone.model.Subreddit;
import com.reddit.clone.repository.SubredditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubredditService {

    @Autowired
    private SubredditRepository subredditRepository;

    @Autowired
    private AuthService authService;

    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {
        Subreddit subreddit = mapToSubreddit(subredditDto);

        Long id = subredditRepository.save(subreddit).getSubredditId();
        subredditDto.setId(id);
        return subredditDto;
    }

    private Subreddit mapToSubreddit(SubredditDto subredditDto) {
        return Subreddit.builder()
                .name(subredditDto.getName())
                .description(subredditDto.getDescription())
                .createdDate(Instant.now())
                .user(authService.getCurrentUser())
                .build();
    }

    private SubredditDto mapToDTO(Subreddit subreddit) {
        return SubredditDto.builder()
                .description(subreddit.getDescription())
                .numberOfPosts(subreddit.getPosts().size())
                .name(subreddit.getName())
                .id(subreddit.getSubredditId())
                .build();
    }

    @Transactional
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public SubredditDto getSubredditDto(Long id) {
        Subreddit subreddit = getSubreddit(id);
        return mapToDTO(subreddit);
    }

    public Subreddit getSubredditByName(String subredditName) {

        return subredditRepository.findByName(subredditName)
                .orElseThrow(() -> new SubRedditNotFoundException("No Subreddit found with name = " + subredditName));
    }

    public Subreddit getSubreddit(Long id) {
        return subredditRepository.findById(id)
                .orElseThrow(() -> new SubRedditNotFoundException("No Subreddit found with given Id"));
    }
}
