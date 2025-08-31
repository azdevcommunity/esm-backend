package com.example.medrese.Model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "question_categories", uniqueConstraints = {@UniqueConstraint(columnNames = {"category_id", "question_id"})})
public class QuestionCategory {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "question_id", nullable = false)
    Integer questionId;
    
    @Column(name = "category_id", nullable = false)
    Integer categoryId;
}
