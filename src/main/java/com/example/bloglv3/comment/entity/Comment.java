package com.example.bloglv3.comment.entity;

import com.example.bloglv3.comment.dto.CommentRequestDto;
import com.example.bloglv3.global.entity.Timestamped;
import com.example.bloglv3.post.entity.Post;
import com.example.bloglv3.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    private User user;

    public Comment(String content) {
        this.content = content;
    }

    public void update(CommentRequestDto commentRequestDto) {
        content = commentRequestDto.getContent();
    }

    public void addPost(Post post) {
        this.post = post;
        post.getComments().add(this);
    }

    public void addUser(User user) {
        this.user = user;
    }
}
