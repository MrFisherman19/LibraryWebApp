package com.mrfisherman.library.service;

import com.mrfisherman.library.exception.NoBookFoundException;
import com.mrfisherman.library.model.entity.Book;
import com.mrfisherman.library.model.entity.Category;
import com.mrfisherman.library.persistence.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryService categoryService;

    public BookService(BookRepository bookRepository, CategoryService categoryService) {
        this.bookRepository = bookRepository;
        this.categoryService = categoryService;
    }

    @Transactional
    public Book saveBook(Book book) {
        Set<Category> categories = book.getCategories().stream()
                .map(categoryService::saveCategory)
                .collect(Collectors.toSet());
        categories.forEach(category -> category.addBook(book));
        book.setCategories(categories);
        return bookRepository.save(book);
    }

    public Book getBook(Long id) {
        return bookRepository.findById(id).orElseThrow(
                () -> new NoBookFoundException(format("Cannot find book with given id: %d", id)));
    }

    public Book getBookWithPosts(Long id) {
        return bookRepository.findByIdWithPosts(id);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public void updateBook(Book book) {
        if (bookRepository.existsById(book.getId())) {
            bookRepository.save(book);
        }
    }

    public void deleteById(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
        }
    }
}
