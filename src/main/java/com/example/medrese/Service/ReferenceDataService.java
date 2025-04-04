package com.example.medrese.Service;

import com.example.medrese.Model.Category;
import com.example.medrese.Repository.CategoryRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ReferenceDataService {

    CategoryRepository categoryRepository;

    public String addReferenceData() {

        if (categoryRepository.count() > 0) {
            return "Categories already exist";
        }

        // 1. Kök kateqoriyalar
        Category islamEtiqadiVeFiqhi = categoryRepository.save(Category.builder().name("İslam etiqadı və fiqhi").isActive(true).build());
        Category islamElmleri = categoryRepository.save(Category.builder().name("İslam elmləri").isActive(true).build());

        // 2. Alt kateqoriyalar
        Category etiqad = categoryRepository.save(Category.builder().name("Etiqad").parentId(islamEtiqadiVeFiqhi.getId()).isActive(true).build());

        categoryRepository.save(Category.builder().name("Matrudi Mədrəsəsi").parentId(etiqad.getId()).isActive(true).build());
        categoryRepository.save(Category.builder().name("Əşari Mədrəsəsi").parentId(etiqad.getId()).isActive(true).build());
        categoryRepository.save(Category.builder().name("Əsari Mədrəsəsi").parentId(etiqad.getId()).isActive(false).build());

        Category fiqh = categoryRepository.save(Category.builder().name("Fiqh").parentId(islamEtiqadiVeFiqhi.getId()).isActive(true).build());

        categoryRepository.save(Category.builder().name("Hənəfi Məzhəbi").parentId(fiqh.getId()).isActive(true).build());
        categoryRepository.save(Category.builder().name("Maliki Məzhəbi").parentId(fiqh.getId()).isActive(false).build());
        categoryRepository.save(Category.builder().name("Şafei Məzhəbi").parentId(fiqh.getId()).isActive(true).build());
        categoryRepository.save(Category.builder().name("Hənbəli Məzhəbi").parentId(fiqh.getId()).isActive(false).build());

        categoryRepository.save(Category.builder().name("Qurani Kərim Dərsləri").parentId(islamElmleri.getId()).build());


        return "Categories added";
    }
}
