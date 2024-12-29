package com.example.medrese.Repository;

import com.example.medrese.Model.QuestionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionCategoryRepository extends JpaRepository<QuestionCategory, Integer> {
    boolean existsByQuestionId(int id);

    @Modifying
    void deleteByQuestionId(int id);
}
