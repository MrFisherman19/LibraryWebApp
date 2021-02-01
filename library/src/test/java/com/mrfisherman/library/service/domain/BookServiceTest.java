package com.mrfisherman.library.service.domain;

import com.mrfisherman.library.model.entity.Book;
import com.mrfisherman.library.persistence.repository.BookRepository;
import com.mrfisherman.library.service.domain.stubs.BookStub;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@ActiveProfiles("test")
@SpringBootTest
class BookServiceTest {

    private static BookStub bookStubs;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @BeforeAll
    public static void setUp() {
        bookStubs = new BookStub();
    }

    @BeforeEach
    void clearDatabase() {
        bookRepository.deleteAll();
    }

    @Test
    void should_save_book() {
        //given
        Book book = bookStubs.getBook();
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
        Book book1 = bookStubs.getBook();
        book1.setIsbn("1234567890");
        Book book2 = bookStubs.getBook();
        book2.setIsbn("1234567890");
        //then
        assertThatThrownBy(() -> {
            bookService.save(book1);
            bookService.save(book2);
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void should_throw_constraintViolationException_when_isbn_is_null() {
        //given
        Book book1 = bookStubs.getBook();
        book1.setIsbn(null);
        //then
        assertThatThrownBy(() -> bookService.save(book1))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void should_throw_constraintViolationException_when_title_is_null() {
        //given
        Book book1 = bookStubs.getBook();
        book1.setTitle(null);
        //then
        assertThatThrownBy(() -> bookService.save(book1))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void should_delete_book_by_id() {
        //given
        Book book = bookStubs.getBook();
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
        Book book = bookStubs.getBook();
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
        Book book1 = bookStubs.getBook();
        book1.setIsbn("1234567890");
        Book book2 = bookStubs.getBook();
        book2.setIsbn("0987654321");
        bookService.save(book1);
        bookService.save(book2);
        //when
        Page<Book> allBooks = bookService.findAll(PageRequest.of(0, 5));
        //then
        assertThat(allBooks.getTotalElements()).isGreaterThanOrEqualTo(2);
    }
}