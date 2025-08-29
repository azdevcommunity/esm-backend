package com.example.medrese.Model;

import com.example.medrese.Core.Util.DateUtil;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.checkerframework.checker.units.qual.C;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "questions", uniqueConstraints = {@UniqueConstraint(columnNames = "question")})
public class Question  {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotBlank
    @Column(name = "question", columnDefinition = "TEXT")
    String question;

    @NotBlank
    @Column(name = "answer", columnDefinition = "TEXT")
    String answer;

    @Enumerated(EnumType.STRING)
    @Column(name = "answer_type", nullable = false, length = 10)
    AnswerType answerType;

    @Column(name = "author_id")
    Integer authorId;

    @Column(name = "created_date")
    LocalDateTime createdDate;

    @Column(name = "view_count", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    Integer viewCount = 0;

    @PrePersist
    protected void onCreate() {
        createdDate = DateUtil.getCurrentDateTime();
        if (viewCount == null) {
            viewCount = 0;
        }
    }
}
