package com.example.medrese.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthorResponse {
    Integer id;
    String name;
    String image;
    Set<Integer> books;

    public AuthorResponse(Integer id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }
}
