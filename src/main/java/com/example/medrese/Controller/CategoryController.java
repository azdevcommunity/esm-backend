package com.example.medrese.Controller;

import com.example.medrese.DTO.Request.Create.CreateCategoryDTO;
import com.example.medrese.DTO.Request.Update.UpdateCategory;
import com.example.medrese.DTO.Response.CategoryResponse;
import com.example.medrese.DTO.Response.MenuItemResponse;
import com.example.medrese.Model.Category;
import com.example.medrese.Service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Integer id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @GetMapping("/menu")
    public ResponseEntity<List<MenuItemResponse>> getMenuItems() {
        return ResponseEntity.ok(categoryService.getCategoryTree());
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody @Valid CreateCategoryDTO createCategoryDTO) {
        return ResponseEntity.ok(categoryService.createCategory(createCategoryDTO)) ;
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Integer id,@Valid @RequestBody UpdateCategory categoryDetails) {
            return ResponseEntity.ok(categoryService.updateCategory(id, categoryDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

}
