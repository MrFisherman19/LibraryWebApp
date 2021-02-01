package com.mrfisherman.library.service.domain;

import com.mrfisherman.library.model.entity.Author;
import com.mrfisherman.library.model.entity.Book;
import com.mrfisherman.library.persistence.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

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

    @Transactional
    public void deleteById(Long id) {
        Author author = findById(id);
        author.getBooks().forEach(book -> book.removeAuthor(author));
        authorRepository.deleteById(id);
    }

    public Set<Book> getAuthorBooks(Long id) {
        return findById(id).getBooks();
    }
}
