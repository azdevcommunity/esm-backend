package com.example.medrese.Service;


import com.example.medrese.Core.Util.CacheKeys;
import com.example.medrese.DTO.Request.Create.CreateCategoryDTO;
import com.example.medrese.DTO.Request.Update.UpdateCategory;
import com.example.medrese.DTO.Response.CategoryResponse;
import com.example.medrese.DTO.Response.MenuItemResponse;
import com.example.medrese.Model.Category;
import com.example.medrese.Repository.ArticleCategoryRepository;
import com.example.medrese.Repository.BookCategoryRepository;
import com.example.medrese.Repository.CategoryRepository;
import com.example.medrese.Repository.QuestionCategoryRepository;
import com.example.medrese.mapper.CategoryMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)

public class CategoryService {
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;
    ArticleCategoryRepository articleCategoryRepository;
    QuestionCategoryRepository questionCategoryRepository;
    BookCategoryRepository bookCategoryRepository;

    @CachePut(value = CacheKeys.ALL_CATEGORIES)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public CategoryResponse getCategoryById(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return categoryMapper.toResponse(category);
    }

    @CacheEvict(value = {CacheKeys.ALL_CATEGORIES, CacheKeys.MENU_ITEMS}, allEntries = true)
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

    @CacheEvict(value = {CacheKeys.ALL_CATEGORIES, CacheKeys.MENU_ITEMS}, allEntries = true)
    public CategoryResponse updateCategory(Integer id, UpdateCategory categoryDetails) {
        Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Category not found with id " + id));
        category.setParentId(categoryDetails.getParentId());
        category.setName(categoryDetails.getName());
        category =categoryRepository.save(category);
        return categoryMapper.toResponse(category);
    }

    @CacheEvict(value = {CacheKeys.ALL_CATEGORIES, CacheKeys.MENU_ITEMS}, allEntries = true)
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

    @CachePut(value = CacheKeys.MENU_ITEMS)
    public List<MenuItemResponse> getCategoryTree() {
        List<Category> categories = categoryRepository.findAll();

        List<MenuItemResponse> menuItems = categories.stream()
                .map(category -> MenuItemResponse.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .parentId(category.getParentId())
                        .subcategories(new ArrayList<>())
                        .build())
                .collect(Collectors.toList());

        return buildCategoryTree(menuItems);
    }

    private List<MenuItemResponse> buildCategoryTree(List<MenuItemResponse> menuItems) {
        Map<Integer, MenuItemResponse> menuMap = menuItems.stream()
                .collect(Collectors.toMap(MenuItemResponse::getId, item -> item));

        List<MenuItemResponse> rootCategories = new ArrayList<>();

        for (MenuItemResponse item : menuItems) {
            if (item.getParentId() == null) {
                rootCategories.add(item);
            } else {
                MenuItemResponse parent = menuMap.get(item.getParentId());
                if (parent != null) {
                    parent.getSubcategories().add(item);
                }
            }
        }

        return rootCategories;
    }

}
