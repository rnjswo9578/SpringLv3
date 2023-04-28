package com.example.bloglv3.comment.controller;

import com.example.bloglv3.comment.dto.CommentRequestDto;
import com.example.bloglv3.comment.dto.CommentResponseDto;
import com.example.bloglv3.comment.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @ResponseBody
    @PostMapping("/comment")
    public CommentResponseDto createComment(
            @RequestBody CommentRequestDto commentRequestDto,
            HttpServletRequest httpServletRequest) {
        return commentService.createComment(commentRequestDto, httpServletRequest);
    }

    @ResponseBody
    @PutMapping("/comment/{id}")
    public CommentResponseDto updateComment(
            @PathVariable Long id,
            @RequestBody CommentRequestDto commentRequestDto,
            HttpServletRequest httpServletRequest) {
        return commentService.updateComment(id, commentRequestDto, httpServletRequest);
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity deleteComment(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        return commentService.deleteComment(id, httpServletRequest);
    }
}
