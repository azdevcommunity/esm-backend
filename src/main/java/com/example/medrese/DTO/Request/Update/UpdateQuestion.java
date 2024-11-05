package com.example.medrese.DTO.Request.Update;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateQuestion {

    String question;

    String answer;

    Set<Integer> categories;

}
