package com.mrfisherman.library.controller.hateoas;

import com.mrfisherman.library.controller.BookController;
import com.mrfisherman.library.controller.PostController;
import com.mrfisherman.library.model.dto.BookDto;
import com.mrfisherman.library.model.entity.Book;
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
public class BookResourceAssembler implements RepresentationModelAssembler<Book, BookDto> {

    private final ModelMapper mapper;

    @Override
    public BookDto toModel(Book entity) {
        BookDto bookDto = mapper.map(entity, BookDto.class);
        Link selfLink = getSelfLink(entity);
        Link linkToPosts = getLinkToPosts(entity);
        bookDto.add(selfLink, linkToPosts);
        return bookDto;
    }

    @Override
    public CollectionModel<BookDto> toCollectionModel(Iterable<? extends Book> entities) {
        List<BookDto> books = new ArrayList<>();
        entities.forEach(entity -> {
            BookDto bookDto = toModel(entity);
            books.add(bookDto);
        });
        Link linkToAllBooks = linkTo(methodOn(BookController.class).getBooks(null)).withSelfRel();
        return CollectionModel.of(books, linkToAllBooks);
    }

    private Link getLinkToPosts(Book entity) {
        return linkTo(methodOn(PostController.class).getPosts(entity.getId(), null))
                .withRel("posts");
    }

    private Link getSelfLink(Book entity) {
        return linkTo(methodOn(BookController.class).getBook(entity.getId()))
                .withSelfRel();
    }
}
