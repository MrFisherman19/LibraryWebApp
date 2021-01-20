package com.mrfisherman.library.controller;

import com.mrfisherman.library.controller.hateoas.BookResourceAssembler;
import com.mrfisherman.library.model.dto.BookDto;
import com.mrfisherman.library.model.dto.BookInsertDto;
import com.mrfisherman.library.model.entity.Book;
import com.mrfisherman.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public BookDto getBook(@PathVariable Long id) {
        return assembler.toModel(bookService.getBook(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto createBook(@RequestBody BookInsertDto bookInsertDto) {
        Book book = mapper.map(bookInsertDto, Book.class);
        Book bookCreated = bookService.saveBook(book);
        return mapper.map(bookCreated, BookDto.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    private PagedModel<BookDto> getBookDtoModel(Pageable pageable) {
        Page<Book> books = bookService.getAllBooks(pageable);
        return pagedResourcesAssembler.toModel(books, assembler);
    }
}
