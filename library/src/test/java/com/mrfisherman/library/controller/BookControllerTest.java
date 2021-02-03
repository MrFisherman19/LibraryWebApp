package com.mrfisherman.library.controller;

import com.mrfisherman.library.model.entity.Book;
import com.mrfisherman.library.model.entity.types.BookFormat;
import com.mrfisherman.library.persistence.repository.BookRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "testUser", roles = {"ADMIN"})
    void shouldReturnBookById() throws Exception {
        long bookId = 1L;

        Book bookToReturn = new Book();
        bookToReturn.setId(bookId);
        bookToReturn.setType(BookFormat.REAL);
        bookToReturn.setIsbn("1234");
        bookToReturn.setTitle("Nineteen Eighty-Four");

        when(bookRepository.findById(bookId)).thenReturn(java.util.Optional.of(bookToReturn));

        mockMvc.perform(get("/api/books/" + bookId)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.isbn", Matchers.is("1234")))
                .andExpect(jsonPath("$.title", Matchers.is("Nineteen Eighty-Four")))
                .andExpect(jsonPath("$.type", Matchers.is("REAL")));
    }

    @Test
    @WithAnonymousUser
    void shouldNotReturnBookForNotLoggedUser() throws Exception {
        long bookId = 1L;

        Book bookToReturn = new Book();
        bookToReturn.setId(bookId);
        bookToReturn.setType(BookFormat.REAL);
        bookToReturn.setIsbn("1234");
        bookToReturn.setTitle("Nineteen Eighty-Four");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookToReturn));

        mockMvc.perform(get("/api/books/" + bookId)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}