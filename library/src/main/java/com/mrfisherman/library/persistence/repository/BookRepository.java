package com.mrfisherman.library.persistence.repository;

import com.mrfisherman.library.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.posts WHERE b.id = :id")
    Book findByIdWithPosts(@Param(value = "id") Long id);
}
