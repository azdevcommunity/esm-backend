package com.example.medrese.Service;


import com.example.medrese.DTO.Request.Create.CreateCategoryDTO;
import com.example.medrese.DTO.Response.CategoryResponse;
import com.example.medrese.Model.Category;
import com.example.medrese.Repository.ArticleCategoryRepository;
import com.example.medrese.Repository.BookCategoryRepository;
import com.example.medrese.Repository.CategoryRepository;
import com.example.medrese.Repository.QuestionCategoryRepository;
import com.example.medrese.mapper.CategoryMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)

public class CategoryService {
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;
    ArticleCategoryRepository articleCategoryRepository;
    QuestionCategoryRepository questionCategoryRepository;
    BookCategoryRepository bookCategoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public CategoryResponse getCategoryById(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return categoryMapper.toResponse(category);
    }

    public CategoryResponse createCategory(CreateCategoryDTO createCategoryDTO) {
        if (categoryRepository.existsByName(createCategoryDTO.getName())) {
            throw new RuntimeException("Same category name exixts");
        }

        if(Objects.nonNull(createCategoryDTO.getParentId())){
            if(!categoryRepository.existsById(createCategoryDTO.getParentId())){
                throw new RuntimeException("bu parent id tapilamdi");
            }
        }

        Category category = categoryMapper.toEntity(createCategoryDTO);
        category = categoryRepository.save(category);
        return categoryMapper.toResponse(category);
    }

    public Category updateCategory(Integer id, Category categoryDetails) {
        return categoryRepository.findById(id).map(category -> {
            category.setParentId(categoryDetails.getParentId());
            category.setName(categoryDetails.getName());
            return categoryRepository.save(category);
        }).orElseThrow(() -> new RuntimeException("Category not found with id " + id));
    }

    public void deleteCategory(Integer id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("category nor found ");
        }

        if (articleCategoryRepository.existsByCategoryId(id)) {
            throw new RuntimeException("category use for article  doesnt delete");
        }

        if (questionCategoryRepository.existsByQuestionId(id)) {
            throw new RuntimeException("category use for question  doesnt delete");
        }

        if (bookCategoryRepository.existsByBookId((id))) {
            throw new RuntimeException("category use for book doesnt delete");
        }
        categoryRepository.deleteById(id);
    }

}
