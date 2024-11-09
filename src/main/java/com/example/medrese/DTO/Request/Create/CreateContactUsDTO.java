package com.example.medrese.DTO.Request.Create;

import lombok.Data;

@Data
public class CreateContactUsDTO {
    String name;
    String email;
    String subject;
    String phone;
    String message;
}
