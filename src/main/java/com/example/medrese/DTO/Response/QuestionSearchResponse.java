package com.example.medrese.DTO.Response;

import com.example.medrese.Model.QuestionTag;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionSearchResponse {

    int id;
    String question;
    String answer;
    LocalDateTime createdDate;
    List<QuestionCategory> categories;
    List<QuestionTagResponse> tags;

    public QuestionSearchResponse(int id, String question, String answer  ) {
        this.id = id;
        this.question = question;
        this.answer = answer;
    }

    public QuestionSearchResponse(int id, String question, String answer, LocalDateTime createdDate ) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.createdDate = createdDate;
    }

    public QuestionSearchResponse(int id, String question, String answer, Object categories, Object tags) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.categories = (List<QuestionCategory>) categories;
        this.tags = (List<QuestionTagResponse>) tags;
    }
}


