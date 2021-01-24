package com.mrfisherman.library.service;

import com.mrfisherman.library.model.entity.Book;
import com.mrfisherman.library.model.entity.Category;
import com.mrfisherman.library.persistence.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;

@AllArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryService categoryService;
    private final ExceptionHelper<Book> exceptionHelper;

    @Transactional
    public Book saveBook(Book book) {
        Set<Category> categories = book.getCategories().stream()
                .map(categoryService::saveCategory)
                .collect(Collectors.toSet());
        categories.forEach(category -> category.addBook(book));
        book.setCategories(categories);
        return bookRepository.save(book);
    }

    @Transactional
    public Book getBook(Long id) {
        return bookRepository.findById(id).orElseThrow(
                exceptionHelper.getEntityNotFoundException(id, Book.class));
    }

    @Transactional
    public Page<Book> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Transactional
    public void updateBook(Book book) {
        if (bookRepository.existsById(book.getId())) {
            bookRepository.save(book);
        }
    }

    @Transactional
    public void deleteById(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
        }
    }
}
