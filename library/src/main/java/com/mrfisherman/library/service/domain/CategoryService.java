package com.mrfisherman.library.service.domain;

import com.mrfisherman.library.model.entity.Category;
import com.mrfisherman.library.persistence.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public Category saveCategory(final Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            return categoryRepository.findByName(category.getName());
        } else {
            return categoryRepository.save(category);
        }
    }
}
