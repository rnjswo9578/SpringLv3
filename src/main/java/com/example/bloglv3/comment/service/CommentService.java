package com.example.bloglv3.comment.service;

import com.example.bloglv3.comment.dto.CommentRequestDto;
import com.example.bloglv3.comment.dto.CommentResponseDto;
import com.example.bloglv3.comment.entity.Comment;
import com.example.bloglv3.comment.repository.CommentRepository;
import com.example.bloglv3.global.jwt.JwtUtil;
import com.example.bloglv3.post.entity.Post;
import com.example.bloglv3.post.repository.PostRepository;
import com.example.bloglv3.user.entity.User;
import com.example.bloglv3.user.entity.UserRoleEnum;
import com.example.bloglv3.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public CommentResponseDto createComment(CommentRequestDto commentRequestDto, HttpServletRequest httpServletRequest) {
        User user  = getUserByTokenInHttpRequest(httpServletRequest);

        Post post = postRepository.findById(commentRequestDto.getPostId()).orElseThrow(
                () -> new IllegalArgumentException("post 가 없습니다!")
        );

        Comment comment = new Comment(commentRequestDto.getContent());
        comment.addUser(user);
        comment.addPost(post);

        commentRepository.saveAndFlush(comment);

        return new CommentResponseDto(comment);

    }

    @Transactional
    public CommentResponseDto updateComment( Long id, CommentRequestDto commentRequestDto, HttpServletRequest httpServletRequest) {
        User user  = getUserByTokenInHttpRequest(httpServletRequest);

        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Comment 가 없습니다!")
        );

        if (user.getUsername().equals(comment.getUser().getUsername())|| user.getUserRole().equals(UserRoleEnum.ADMIN)) {
            comment.update(commentRequestDto);
            return new CommentResponseDto(comment);
        } else {
            throw new IllegalArgumentException("다른 유져의 댓글입니다!");
        }
    }

    @Transactional
    public ResponseEntity<String> deleteComment(Long id, HttpServletRequest httpServletRequest) {
        User user  = getUserByTokenInHttpRequest(httpServletRequest);

        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Comment 가 없습니다!")
        );

        if (user.getUsername().equals(comment.getUser().getUsername()) || user.getUserRole().equals(UserRoleEnum.ADMIN)) {
            commentRepository.delete(comment);
            return new ResponseEntity<>("댓글 삭제 성공", HttpStatus.OK);
        }

        return new ResponseEntity<>("댓글 삭제 실패", HttpStatus.BAD_REQUEST);
    }



    private User getUserByTokenInHttpRequest(HttpServletRequest httpServletRequest) {
        Claims claims;
        String token = jwtUtil.resolveToken(httpServletRequest);
        if (token == null) {
            throw new IllegalArgumentException("토큰 오류");
        }
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("토큰 오류");
        }

        claims = jwtUtil.getUserInfoFromToken(token);
        return getUserOrElseThrow(claims);
    }

    private User getUserOrElseThrow(Claims claims) {
        return userRepository.findByUsername(claims.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
        );
    }
}


