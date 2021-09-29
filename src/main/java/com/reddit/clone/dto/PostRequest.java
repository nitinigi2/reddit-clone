package com.reddit.clone.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    private Long postId;
    private String subredditName;
    @Size(min = 5, message = "Post name should be at-least of size 5")
    private String postName;
    private String url;
    private String description;
}