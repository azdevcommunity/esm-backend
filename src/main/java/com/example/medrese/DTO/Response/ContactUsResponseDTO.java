package com.example.medrese.DTO.Response;

import lombok.Data;

@Data
public class ContactUsResponseDTO {
    Integer id;
    String name;
    String email;
    String subject;
    String phone;
    String message;
    String createdAt;
    Boolean read;
}