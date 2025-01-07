package com.example.medrese.Controller;

import com.example.medrese.DTO.Response.SearchResponse;
import com.example.medrese.Service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Log4j2
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<SearchResponse> search(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String search
    ) {
            return ResponseEntity.ok(searchService.search(categoryId, search));
    }
}
