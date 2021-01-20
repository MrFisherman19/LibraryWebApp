package com.mrfisherman.library.persistence.repository;

import com.mrfisherman.library.model.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

//    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.posts WHERE b.id = :id")
//    Page<Book> findByIdWithPosts(@Param(value = "id") Long id, Pageable pageable);

    Page<Book> findAll(Pageable pageable);
}
