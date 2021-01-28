package com.mrfisherman.library.controller.hateoas;

import com.mrfisherman.library.controller.BookController;
import com.mrfisherman.library.controller.PostController;
import com.mrfisherman.library.model.dto.PostDto;
import com.mrfisherman.library.model.entity.Post;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RequiredArgsConstructor
@Component
public class PostResourceAssembler implements RepresentationModelAssembler<Post, PostDto> {

    private final ModelMapper mapper;

    @Override
    public PostDto toModel(Post entity) {
        PostDto postDto = mapper.map(entity, PostDto.class);
        postDto.add(getSelfLink(entity).andAffordance(
                afford(methodOn(PostController.class).updatePost(entity.getId(), null))
        ));
        postDto.add(getBookLink(entity));
        postDto.add(getLinkToComments(entity));
        return postDto;
    }

    @Override
    public CollectionModel<PostDto> toCollectionModel(Iterable<? extends Post> entities) {
        List<PostDto> posts = new ArrayList<>();
        entities.forEach(entity -> {
            PostDto postDto = toModel(entity);
            posts.add(postDto);
        });
        Link linkToAllPosts = linkTo(methodOn(PostController.class).getPosts(null, null)).withSelfRel().expand();
        return CollectionModel.of(posts, linkToAllPosts);
    }

    private Link getLinkToComments(Post entity) {
        return linkTo(methodOn(PostController.class).getComments(entity.getId()))
                .withRel("comments");
    }

    private Link getBookLink(Post entity) {
        return linkTo(methodOn(BookController.class).getBook(entity.getBook().getId()))
                .withRel("book");
    }

    private Link getSelfLink(Post entity) {
        return linkTo(methodOn(PostController.class).getPost(entity.getId()))
                .withSelfRel();
    }

}
