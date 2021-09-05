package com.reddit.clone.service;

import com.reddit.clone.dto.VoteDto;
import com.reddit.clone.exception.SpringRedditException;
import com.reddit.clone.model.Post;
import com.reddit.clone.model.User;
import com.reddit.clone.model.Vote;
import com.reddit.clone.model.VoteType;
import com.reddit.clone.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class VoteService {

    private final AuthService authService;
    private final PostService postService;
    private final VoteRepository voteRepository;

    //1. If up/down already and now down/up -> -2/+2
    //2. If none then up/down -> +1/-1
    //3. If up/down already and now up/down -> remove entry
    public VoteDto save(VoteDto voteDto) {
        Vote vote = mapToModel(voteDto);
        Post post = vote.getPost();
        User user = vote.getUser();
        Optional<Vote> existingVoteOptional = voteRepository.findByPostAndUser(post, user);
        if (existingVoteOptional.isPresent()) {
            Vote existingVote = existingVoteOptional.get();
            if (existingVote.getVoteType().equals(voteDto.getVoteType())) {
                throw new SpringRedditException("User: " + user.getUsername() + " has already " +
                        voteDto.getVoteType() + "'d for post with id= " + voteDto.getPostId());
            }else {
                if (VoteType.UPVOTE.equals(existingVote.getVoteType())) {
                    post.setVoteCount(post.getVoteCount() - 2);
                }else{
                    post.setVoteCount(post.getVoteCount() + 2);
                }
                existingVote.setVoteType(voteDto.getVoteType());
            }
            voteRepository.save(existingVote);
            return voteDto;
        }
        if (VoteType.UPVOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        }

        if (VoteType.DOWNVOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() - 1);
        }
        voteRepository.save(vote);
        return mapToDto(vote);
    }

    private VoteDto mapToDto(Vote vote) {
        return VoteDto.builder()
                .postId(vote.getPost().getPostId())
                .voteType(vote.getVoteType())
                .build();
    }

    private Vote mapToModel(VoteDto voteDto) {
        return Vote.builder()
                .post(postService.getPost(voteDto.getPostId()))
                .user(authService.getCurrentUser())
                .voteType(voteDto.getVoteType())
                .build();
    }
}
