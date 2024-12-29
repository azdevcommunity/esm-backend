package com.example.medrese.Model;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.checkerframework.checker.units.qual.C;

@Data
@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(
        name = "article_categories",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"article_id", "category_id"})
        }
)
public class ArticleCategory {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "article_id", nullable = false)
    Integer articleId;

    @Column(name = "category_id", nullable = false)
    Integer categoryId;
}