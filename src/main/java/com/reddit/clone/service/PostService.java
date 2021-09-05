package com.reddit.clone.service;

import com.reddit.clone.dto.PostRequest;
import com.reddit.clone.dto.PostResponse;
import com.reddit.clone.exception.SpringRedditException;
import com.reddit.clone.model.Post;
import com.reddit.clone.model.User;
import com.reddit.clone.repository.PostRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Slf4j
public class PostService {

    private final AuthService authService;
    private final SubredditService subredditService;
    private final PostRepository postRepository;
    private final UserService userService;

    @Transactional
    public PostResponse save(PostRequest postRequest) {
        return mapToDto(postRepository.save(mapToModel(postRequest)));
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        return mapToDto(postRepository.getById(id));
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<PostResponse> getPostsBySubreddit(Long id) {
        System.out.println("getPostsBySubreddit is called");
        //System.out.println("Get posts by subreddit::::::: " + subredditService.getSubreddit(id));
        return subredditService.getSubreddit(id)
                .getPosts()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<PostResponse> getPostsByUsername(String username) {
        User user = userService.findByUserName(username)
                .orElseThrow(() -> new SpringRedditException("User not found with given username: " + username));
        return postRepository.findByUser(user).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private Post mapToModel(PostRequest postRequest) {
        return Post.builder()
                .createdDate(Instant.now())
                .description(postRequest.getDescription())
                .postName(postRequest.getPostName())
                .subreddit(subredditService.getSubredditByName(postRequest.getSubredditName()))
                .user(authService.getCurrentUser())
                .url(postRequest.getUrl())
                .voteCount(0)
                .build();
    }

    private PostResponse mapToDto(Post post) {
        return PostResponse.builder()
                .id(post.getPostId())
                .postName(post.getPostName())
                .description(post.getDescription())
                .subredditName(post.getSubreddit().getName())
                .build();
    }
}
