package com.example.medrese.DTO.Request.Create;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateQuestionDTO {

    @NotBlank(message = "Question is required")
    String question;

    @NotBlank(message = "Answer is required")
    String answer;

    Set<Integer> categories;

}
