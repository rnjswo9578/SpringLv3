package com.example.bloglv3.post.dto;

import com.example.bloglv3.comment.dto.CommentResponseDto;
import com.example.bloglv3.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String username;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;
    private List<CommentResponseDto> commentList;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createAt = post.getCreateAt();
        this.modifiedAt = post.getModifiedAt();
        this.commentList = post.getComments().stream().map(CommentResponseDto::new).toList();
        this.username = post.getUser().getUsername();
    }
}
