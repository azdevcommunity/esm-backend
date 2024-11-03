package com.example.medrese.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "articles", uniqueConstraints = {@UniqueConstraint(columnNames = "title")})
public class Article {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "published_at")
    String publishedAt;

    @NotBlank
    @Column(name = "title", unique = true)
    String title;

    @NotBlank
    @Column(name = "content", columnDefinition = "text")
    String content;


}
