package com.mrfisherman.library.service.domain;

import com.mrfisherman.library.model.entity.Book;
import com.mrfisherman.library.model.entity.Category;
import com.mrfisherman.library.persistence.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryService categoryService;
    private final ExceptionHelper<Book> exceptionHelper;

    @Transactional
    public Book save(Book book) {
        Set<Category> categories = book.getCategories().stream()
                .map(categoryService::save)
                .collect(Collectors.toSet());
        categories.forEach(category -> category.addBook(book));
        book.setCategories(categories);
        return bookRepository.save(book);
    }

    @Transactional
    @Cacheable(cacheNames = "findBookById", key = "#id")
    public Book findById(Long id) {
        return bookRepository.findById(id).orElseThrow(
                exceptionHelper.getEntityNotFoundException(id, Book.class));
    }

    @Transactional
    @Cacheable(cacheNames = "findAllBooks")
    public Page<Book> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Transactional
    @CachePut(cacheNames = "findBookById", key = "#result.id")
    public Book update(Long id, Book book) {
        Book bookToUpdate = findById(id);
        bookToUpdate.setTitle(book.getTitle());
        bookToUpdate.setSummary(book.getSummary());
        bookToUpdate.setNumberOfPages(book.getNumberOfPages());
        bookToUpdate.setType(book.getType());
        bookToUpdate.setDescription(book.getDescription());
        bookToUpdate.setPublishYear(book.getPublishYear());
        bookToUpdate.setCategories(book.getCategories());
        bookToUpdate.setUpdated(LocalDateTime.now());
        return bookToUpdate;
    }

    @Transactional
    @CacheEvict(cacheNames = "findBookById")
    public void deleteById(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
        }
    }
}
