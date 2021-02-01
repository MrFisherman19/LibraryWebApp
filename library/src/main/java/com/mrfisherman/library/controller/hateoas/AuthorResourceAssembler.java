package com.mrfisherman.library.controller.hateoas;

import com.mrfisherman.library.controller.AuthorController;
import com.mrfisherman.library.controller.BookController;
import com.mrfisherman.library.model.dto.AuthorDto;
import com.mrfisherman.library.model.entity.Author;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@Component
public class AuthorResourceAssembler implements RepresentationModelAssembler<Author, AuthorDto> {

    private final ModelMapper mapper;

    @Override
    public AuthorDto toModel(Author entity) {
        AuthorDto authorDto = mapper.map(entity, AuthorDto.class);
        Link toSelf = getSelfLink(entity);
        Link toBooks = getLinkToBooks(entity);
        authorDto.add(toSelf, toBooks);
        return authorDto;
    }

    private Link getSelfLink(Author entity) {
        return linkTo(methodOn(AuthorController.class).getAuthor(entity.getId()))
                .withSelfRel();
    }

    private Link getLinkToBooks(Author entity) {
        return linkTo(methodOn(AuthorController.class).getAuthorBooks(entity.getId())).withRel("books");
    }
}
