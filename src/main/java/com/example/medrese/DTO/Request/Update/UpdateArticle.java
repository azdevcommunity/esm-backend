package com.example.medrese.DTO.Request.Update;

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

    Set<Integer> authorIds;

    Set<Integer> categories;
}
