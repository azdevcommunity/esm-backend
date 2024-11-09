package com.example.medrese.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "contact_us")
public class ContactUs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String email;

    String subject;

    @Column(nullable = false)
    String phone;

    @Column(nullable = false, columnDefinition = "TEXT")
    String message;

    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;

    @Column(name = "read", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    Boolean read = false;
}