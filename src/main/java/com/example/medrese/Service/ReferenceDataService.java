package com.example.medrese.Service;

import com.example.medrese.Model.Category;
import com.example.medrese.Repository.CategoryRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ReferenceDataService {


    CategoryRepository categoryRepository;

    public String addReferenceData() {
        List<Category> categories = new ArrayList<>();

        if (categoryRepository.count() > 0) {
            return "Categories already exist";
        }

        categories.add(Category.builder().id(1).name("İslam etiqadı və fiqhi").build());

        categories.add(Category.builder().id(2).name("Etiqad").parentId(1).build());
        categories.add(Category.builder().id(3).name("Matrudi Mədrəsəsi").parentId(2).build());
        categories.add(Category.builder().id(4).name("Əşari Mədrəsəsi").parentId(2).build());
        categories.add(Category.builder().id(5).name("Əsari Mədrəsəsi").parentId(2).build());


        categories.add(Category.builder().id(6).name("Fiqh").parentId(1).build());
        categories.add(Category.builder().id(7).name("Hənəfi Məzhəbi").parentId(6).build());
        categories.add(Category.builder().id(8).name("Maliki Məzhəbi").parentId(6).build());
        categories.add(Category.builder().id(9).name("Şafei Məzhəbi").parentId(6).build());
        categories.add(Category.builder().id(10).name("Hənbəli Məzhəbi").parentId(6).build());


        categories.add(Category.builder().id(11).name("İslam elmləri").build());
        categories.add(Category.builder().id(12).name("Qurani Kərim Dərsləri").parentId(11).build());


        categoryRepository.saveAll(categories);

        return "Categories added";
    }
}
