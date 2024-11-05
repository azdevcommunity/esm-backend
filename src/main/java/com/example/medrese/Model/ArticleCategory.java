package com.example.medrese.Model;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "categories", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class ArticleCategory {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    Integer articleId;
    Integer categoryId;
}
