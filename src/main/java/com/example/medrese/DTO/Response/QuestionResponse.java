package com.example.medrese.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionResponse {

    int id;
    String question;
    String answer;
    Set<Integer> categories;
}
