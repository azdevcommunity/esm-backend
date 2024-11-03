package com.example.medrese.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString(exclude = {"categories"},doNotUseGetters = true)
@EqualsAndHashCode(exclude = {"categories"},doNotUseGetters = true)
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
    @Column(name = "question")
    String question;

    @NotBlank
    @Column(name = "answer")
    String answer;


}
