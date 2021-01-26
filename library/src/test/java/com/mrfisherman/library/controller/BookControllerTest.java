package com.mrfisherman.library.controller;

import com.mrfisherman.library.model.entity.Book;
import com.mrfisherman.library.model.entity.types.BookFormat;
import com.mrfisherman.library.service.domain.BookService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @MockBean
    private BookService bookService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "testUser", roles = {"USER_ADMIN"})
    void shouldReturnBookById() throws Exception {
        long bookId = 1L;

        var bookToReturn = new Book();
        bookToReturn.setId(bookId);
        bookToReturn.setType(BookFormat.REAL);
        bookToReturn.setIsbn("1234");
        bookToReturn.setTitle("Nineteen Eighty-Four");

        when(bookService.getBook(bookId)).thenReturn(bookToReturn);

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

        var bookToReturn = new Book();
        bookToReturn.setId(bookId);
        bookToReturn.setType(BookFormat.REAL);
        bookToReturn.setIsbn("1234");
        bookToReturn.setTitle("Nineteen Eighty-Four");

        when(bookService.getBook(bookId)).thenReturn(bookToReturn);

        mockMvc.perform(get("/api/books/" + bookId)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}