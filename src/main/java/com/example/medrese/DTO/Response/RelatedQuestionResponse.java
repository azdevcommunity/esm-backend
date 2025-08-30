package com.example.medrese.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RelatedQuestionResponse {
    Integer questionId;
    String question;
    List<QuestionTagResponse> tags;
}