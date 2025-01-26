package com.example.medrese.DTO.Response;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookResponse {

    Integer id;

    String title;

    String image;

    Integer authorId;

    String authorName;
    public BookResponse(String title, String image, Integer authorId, String authorName) {
        Objects.isNull(title);
    }
}
