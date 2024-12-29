package com.example.medrese.Repository;

import com.example.medrese.Model.QuestionTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface QuestionTagRepository extends JpaRepository<QuestionTag, Integer> {
    @Modifying
    void deleteByQuestionId(Integer id);
}
