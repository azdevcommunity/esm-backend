package com.example.medrese.Core.DTO.Request.Create;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateBook {

    @NotBlank(message = "Title is required")
    String title;

    @NotNull(message = "Author Id is required")
    Integer authorId;

    @NotNull(message = "Image can not be null")
    String image;

}
