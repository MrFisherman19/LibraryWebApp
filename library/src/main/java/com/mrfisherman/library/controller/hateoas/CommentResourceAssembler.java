package com.mrfisherman.library.controller.hateoas;

import com.mrfisherman.library.controller.CommentController;
import com.mrfisherman.library.controller.PostController;
import com.mrfisherman.library.model.dto.CommentDto;
import com.mrfisherman.library.model.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@Component
public class CommentResourceAssembler implements RepresentationModelAssembler<Comment, CommentDto> {

    private final ModelMapper mapper;

    @Override
    public CommentDto toModel(Comment entity) {
        CommentDto commentDto = mapper.map(entity, CommentDto.class);
        Link selfLink = getSelfLink(entity);
        commentDto.add(selfLink);
        return commentDto;
    }

    @Override
    public CollectionModel<CommentDto> toCollectionModel(Iterable<? extends Comment> entities) {
        List<CommentDto> comments = new ArrayList<>();
        entities.forEach(entity -> {
            CommentDto commentDto = toModel(entity);
            comments.add(commentDto);
        });
        long postId = 0;
        if (comments.size() > 0) {
            postId = comments.get(0).getPostId();
        }
        Link linkToPost = linkTo(methodOn(PostController.class).getComments(postId)).withSelfRel();
        return CollectionModel.of(comments, linkToPost);
    }

    private Link getSelfLink(Comment entity) {
        return linkTo(methodOn(CommentController.class).getComment(entity.getId()))
                .withSelfRel();
    }
}
