package com.reddit.clone.service;

import com.reddit.clone.dto.CommentsDto;
import com.reddit.clone.model.Comment;
import com.reddit.clone.model.Post;
import com.reddit.clone.model.User;
import com.reddit.clone.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {

    private final AuthService authService;
    private final PostService postService;
    private final UserService userService;
    private final CommentRepository commentRepository;

    public CommentsDto save(CommentsDto commentsDto) {
        Comment comment = commentRepository.save(mapToModel(commentsDto));
        return mapToDto(comment);
    }

    private Comment mapToModel(CommentsDto commentsDto) {
        return Comment.builder()
                .createdDate(Instant.now())
                .user(authService.getCurrentUser())
                .post(postService.getPost(commentsDto.getPostId()))
                .text(commentsDto.getText())
                .build();
    }

    private CommentsDto mapToDto(Comment comment) {
        return CommentsDto.builder()
                .id(comment.getId())
                .createdAt(comment.getCreatedDate())
                .postId(comment.getPost().getPostId())
                .text(comment.getText())
                .username(comment.getUser().getUsername())
                .build();
    }

    public List<CommentsDto> getAllCommentsForPost(Long id) {
        Post post = postService.getPost(id);
        return commentRepository.findByPost(post)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CommentsDto> getAllCommentsForUser(String username) {
        User user = userService.findByUserName(username);

        return commentRepository.findByUser(user)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}
