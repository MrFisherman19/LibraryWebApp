package com.mrfisherman.library.controller;

import com.mrfisherman.library.controller.hateoas.AuthorResourceAssembler;
import com.mrfisherman.library.controller.hateoas.BookResourceAssembler;
import com.mrfisherman.library.model.dto.AuthorDto;
import com.mrfisherman.library.model.dto.AuthorInsertDto;
import com.mrfisherman.library.model.dto.BookDto;
import com.mrfisherman.library.model.entity.Author;
import com.mrfisherman.library.service.domain.AuthorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final ModelMapper mapper;
    private final AuthorResourceAssembler assembler;
    private final BookResourceAssembler bookAssembler;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDto createAuthor(@Valid @RequestBody AuthorInsertDto authorInsertDto) {
        Author author = mapper.map(authorInsertDto, Author.class);
        Author authorCreated = authorService.save(author);
        return mapper.map(authorCreated, AuthorDto.class);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AuthorDto getAuthor(@PathVariable Long id) {
        return assembler.toModel(authorService.findById(id));
    }

    @GetMapping("/{id}/books")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<BookDto> getAuthorBooks(@PathVariable Long id) {
        return bookAssembler.toCollectionModel(authorService.getAuthorBooks(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAuthor(@PathVariable Long id) {
        authorService.deleteById(id);
    }
}
