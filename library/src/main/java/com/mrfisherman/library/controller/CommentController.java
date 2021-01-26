package com.mrfisherman.library.controller;

import com.mrfisherman.library.controller.hateoas.CommentResourceAssembler;
import com.mrfisherman.library.model.dto.CommentDto;
import com.mrfisherman.library.model.dto.CommentInsertDto;
import com.mrfisherman.library.model.entity.Comment;
import com.mrfisherman.library.service.domain.CommentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;
    private final ModelMapper mapper;
    private final CommentResourceAssembler assembler;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto getComment(@PathVariable Long id) {
        return assembler.toModel(commentService.getComment(id));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateComment(@PathVariable Long id, @Valid @RequestBody CommentInsertDto commentToUpdate) {
        Comment updated = commentService.update(id, mapper.map(commentToUpdate, Comment.class));
        return mapper.map(updated, CommentDto.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long id) {
        commentService.removeById(id);
    }
}
