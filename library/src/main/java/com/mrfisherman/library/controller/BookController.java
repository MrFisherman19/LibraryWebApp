package com.mrfisherman.library.controller;

import com.mrfisherman.library.controller.hateoas.BookResourceAssembler;
import com.mrfisherman.library.model.dto.*;
import com.mrfisherman.library.model.entity.Book;
import com.mrfisherman.library.service.domain.BookService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final ModelMapper mapper;
    private final BookResourceAssembler assembler;
    private final PagedResourcesAssembler<Book> pagedResourcesAssembler;

    @GetMapping
    public ResponseEntity<PagedModel<BookDto>> getBooks(@PageableDefault Pageable pageable) {
        PagedModel<BookDto> page = getBookDtoModel(pageable);
        String a = null;
        a.equals("sa");
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public BookDto getBook(@PathVariable Long id) {
        return assembler.toModel(bookService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto createBook(@Valid @RequestBody BookInsertDto bookInsertDto) {
        Book book = mapper.map(bookInsertDto, Book.class);
        Book bookCreated = bookService.save(book);
        return mapper.map(bookCreated, BookDto.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookDto updateBook(@PathVariable Long id, @Valid @RequestBody BookUpdateDto bookToUpdate) {
        Book updated = bookService.update(id, mapper.map(bookToUpdate, Book.class));
        return mapper.map(updated, BookDto.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    private PagedModel<BookDto> getBookDtoModel(Pageable pageable) {
        Page<Book> books = bookService.findAll(pageable);
        return pagedResourcesAssembler.toModel(books, assembler);
    }
}
