package com.reddit.clone.service;

import com.reddit.clone.dto.SubredditDto;
import com.reddit.clone.exception.SpringRedditException;
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

        Long id = subredditRepository.save(subreddit).getId();
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
                .id(subreddit.getId())
                .build();
    }

    @Transactional
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public SubredditDto getSubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new SpringRedditException("No Subreddit found with id = " + id));

        return mapToDTO(subreddit);
    }
}
