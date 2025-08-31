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
@Table(
        name = "question_tags",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"tag_id", "question_id"})}
)
public class QuestionTag {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "tag_id", nullable = false)
    Integer tagId;

    @Column(name = "question_id", nullable = false)
    Integer questionId;

}
