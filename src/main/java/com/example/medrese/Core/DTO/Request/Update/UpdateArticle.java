package com.example.medrese.Core.DTO.Request.Update;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateArticle {

    String publishedAt;

    String title;

    String content;

    int author_id;

    Set<Integer> categories;
}
