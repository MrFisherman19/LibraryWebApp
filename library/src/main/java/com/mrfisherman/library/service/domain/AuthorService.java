package com.mrfisherman.library.service.domain;

import com.mrfisherman.library.model.entity.Author;
import com.mrfisherman.library.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookService bookService;
    private final ExceptionHelper<Author> exceptionHelper;

    public AuthorService(AuthorRepository authorRepository, BookService bookService,
                         ExceptionHelper<Author> exceptionHelper) {
        this.authorRepository = authorRepository;
        this.bookService = bookService;
        this.exceptionHelper = exceptionHelper;
    }

    @Transactional
    public Author save(Author author) {
        author.getBooks().forEach(book -> {
            book.addAuthor(author);
            bookService.save(book);
        });
        return authorRepository.save(author);
    }

    @Transactional
    public Author findById(Long id) {
        return authorRepository.findById(id).orElseThrow(exceptionHelper.getEntityNotFoundException(id, Author.class));
    }

}
