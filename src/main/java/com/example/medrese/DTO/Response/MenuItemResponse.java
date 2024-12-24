package com.example.medrese.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MenuItemResponse {
    Integer id;
    String name;
    Integer parentId;
    List<MenuItemResponse> subcategories;
}
