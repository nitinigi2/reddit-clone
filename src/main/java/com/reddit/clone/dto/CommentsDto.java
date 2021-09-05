package com.reddit.clone.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@Builder
public class CommentsDto {
    private Long id;
    private Long postId;
    private Instant createdAt;
    private String text;
    private String username;
}
