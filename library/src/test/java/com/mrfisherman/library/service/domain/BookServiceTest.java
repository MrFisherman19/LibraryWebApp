package com.mrfisherman.library.service.domain;

import com.mrfisherman.library.model.entity.Book;
import com.mrfisherman.library.model.entity.Category;
import com.mrfisherman.library.model.entity.types.BookFormat;
import com.mrfisherman.library.persistence.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void clearDatabase() {
        bookRepository.deleteAll();
    }

    @Test
    void should_save_book() {
        //given
        Book book = getExampleBook();
        //when
        bookService.save(book);
        //then
        Optional<Book> loaded = bookRepository.findById(book.getId());
        assertAll(()->{
            assertThat(loaded).isPresent();
            Book savedBook = loaded.get();
            assertThat(savedBook.getCategories()).isEqualTo(book.getCategories());
            assertThat(savedBook.getDescription()).isEqualTo(book.getDescription());
            assertThat(savedBook.getTitle()).isEqualTo(book.getTitle());
            assertThat(savedBook.getCreated()).isNotNull();
            assertThat(savedBook.getSummary()).isEqualTo(book.getSummary());
            assertThat(savedBook.getNumberOfPages()).isEqualTo(book.getNumberOfPages());
            assertThat(savedBook.getPublishYear()).isEqualTo(book.getPublishYear());
            assertThat(savedBook.getType()).isEqualTo(book.getType());
        });
    }

    @Test
    void should_throw_dataIntegrationException_when_isbn_is_not_unique() {
        //given
        Book book1 = getExampleBook();
        book1.setIsbn("1234567890");
        Book book2 = getExampleBook();
        book2.setIsbn("1234567890");
        //then
        assertThrows(DataIntegrityViolationException.class, () -> {
            bookService.save(book1);
            bookService.save(book2);
        });
    }

    @Test
    void should_delete_book_by_id() {
        //given
        Book book = getExampleBook();
        bookRepository.save(book);
        Long id = book.getId();
        //when
        bookService.deleteById(id);
        //then
        Optional<Book> loaded = bookRepository.findById(id);
        assertThat(loaded).isEmpty();
    }

    @Test
    void should_update_book() {
        //given
        Book book = getExampleBook();
        bookRepository.save(book);
        Long id = book.getId();
        //when
        book.setTitle("Another title");
        book.setNumberOfPages(192);
        book.setSummary("Updated summary");
        bookService.update(id, book);
        bookRepository.flush();
        //then
        Book bookAfterUpdate = bookService.findById(id);
        assertAll(() -> {
            assertThat(bookAfterUpdate.getSummary()).isEqualTo("Updated summary");
            assertThat(bookAfterUpdate.getNumberOfPages()).isEqualTo(192);
            assertThat(bookAfterUpdate.getTitle()).isEqualTo("Another title");
        });
    }

    @Test
    void should_find_books() {
        //given
        Book book1 = getExampleBook();
        book1.setIsbn("1234567890");
        Book book2 = getExampleBook();
        book2.setIsbn("0987654321");
        bookService.save(book1);
        bookService.save(book2);
        //when
        Page<Book> allBooks = bookService.findAll(PageRequest.of(0, 5));
        //then
        assertThat(allBooks.getTotalElements()).isGreaterThanOrEqualTo(2);
    }

    private Book getExampleBook() {
        Book book = new Book();
        book.setTitle("Book 1");
        book.setPublishYear(1990);
        book.setType(BookFormat.REAL);
        book.setIsbn("1231124412132"); //have to be unique
        book.setDescription("Very good book");
        book.setNumberOfPages(190);
        book.setSummary("Very short summary");

        Set<Category> categories = new HashSet<>();
        categories.add(new Category("horror"));
        categories.add(new Category("drama"));
        book.setCategories(categories);

        return book;
    }
}