package com.example.bloglv3.comment.repository;

import com.example.bloglv3.comment.entity.Comment;
import com.example.bloglv3.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);

    void deleteAllByPost(Post post);
}
