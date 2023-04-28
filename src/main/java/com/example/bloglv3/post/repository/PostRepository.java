package com.example.bloglv3.post.repository;

import com.example.bloglv3.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    Optional<Post> findByIdAndUserId(Long id, Long userId);
}
