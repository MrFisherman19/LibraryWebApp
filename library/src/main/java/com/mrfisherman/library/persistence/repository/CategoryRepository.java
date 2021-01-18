package com.mrfisherman.library.persistence.repository;

import com.mrfisherman.library.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByName(String name);

    Set<Category> findAllByNameIn(Set<Category> categories);

    boolean existsByName(String name);

}
