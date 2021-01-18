package com.mrfisherman.library.controller;

import com.mrfisherman.library.model.dto.*;
import com.mrfisherman.library.model.entity.Book;
import com.mrfisherman.library.model.entity.Post;
import com.mrfisherman.library.service.BookService;
import com.mrfisherman.library.service.PostService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final PostService postService;
    private final ModelMapper mapper;

    public BookController(BookService bookService, PostService postService, ModelMapper mapper) {
        this.bookService = bookService;
        this.postService = postService;
        this.mapper = mapper;
    }

    @GetMapping("/{id}")
    public BookDto getBook(@PathVariable Long id) {
        return mapper.map(bookService.getBook(id), BookDto.class);
    }


    @GetMapping
    public List<BookDto> getBooks() {
        return mapper.map(bookService.getAllBooks(), new TypeToken<List<BookDto>>() {}.getType());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto createBook(@RequestBody BookInsertDto bookInsertDto) {
        Book book = mapper.map(bookInsertDto, Book.class);
        Book bookCreated = bookService.saveBook(book);
        return mapper.map(bookCreated, BookDto.class);
    }

    @GetMapping("/{id}/posts")
    public List<PostMinimalDto> getPostsForBook(@PathVariable Long id) {
        return mapper.map(postService.findAllByBookId(id), new TypeToken<List<PostMinimalDto>>() {}.getType());
    }

    @PostMapping("/{id}/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public PostMinimalDto createPost(@PathVariable Long id, @RequestBody PostInsertDto postInsertDto) {
        Post post = mapper.map(postInsertDto, Post.class);
        Post postCreated = postService.savePost(id, post);
        return mapper.map(postCreated, PostMinimalDto.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
    }

}
