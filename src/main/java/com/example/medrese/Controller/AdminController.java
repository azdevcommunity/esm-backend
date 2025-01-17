package com.example.medrese.Controller;

import com.example.medrese.Service.ReferenceDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Validated
public class AdminController {

    private final ReferenceDataService referenceDataService;


    @GetMapping("/initialize-menu")
    public ResponseEntity<String> initializeMenu() {
        return ResponseEntity.ok(referenceDataService.addReferenceData());
    }

}
