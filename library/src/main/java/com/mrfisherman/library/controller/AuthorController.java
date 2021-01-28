package com.mrfisherman.library.controller;

import com.mrfisherman.library.model.dto.AuthorDto;
import com.mrfisherman.library.model.dto.AuthorInsertDto;
import com.mrfisherman.library.model.entity.Author;
import com.mrfisherman.library.service.domain.AuthorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;
    private final ModelMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDto createAuthor(@Valid @RequestBody AuthorInsertDto authorInsertDto) {
        Author author = mapper.map(authorInsertDto, Author.class);
        Author authorCreated = authorService.save(author);
        return mapper.map(authorCreated, AuthorDto.class);
    }

}
