package com.example.medrese.Service;


import com.example.medrese.DTO.Request.Create.CreateCategoryDTO;
import com.example.medrese.DTO.Response.CategoryResponse;
import com.example.medrese.Model.Category;
import com.example.medrese.Repository.CategoryRepository;
import com.example.medrese.mapper.CategoryMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)

public class CategoryService  {
     CategoryRepository categoryRepository;
     CategoryMapper categoryMapper;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public CategoryResponse getCategoryById(Integer id) {
        Category category =  categoryRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Category not found"));
        return categoryMapper.toResponse(category);
    }

    public CategoryResponse createCategory(CreateCategoryDTO createCategoryDTO) {
        Category category = categoryMapper.toEntity(createCategoryDTO);

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
        categoryRepository.deleteById(id);
    }

}
