package com.mrfisherman.library.service.domain.stubs;

import com.mrfisherman.library.model.entity.Book;
import com.mrfisherman.library.model.entity.Category;
import com.mrfisherman.library.model.entity.types.BookFormat;

import java.util.HashSet;
import java.util.Set;

public class BookStub {

    public Book getBook() {
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
