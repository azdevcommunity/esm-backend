package com.example.medrese.Controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.medrese.DTO.Request.Create.CreateContactUsDTO;
import com.example.medrese.DTO.Response.ContactUsResponseDTO;
import com.example.medrese.Service.ContactUsService;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
@AllArgsConstructor
public class ContactUsController {

    private final ContactUsService contactUsService;

    @PostMapping
    public ResponseEntity<ContactUsResponseDTO> createContact(@RequestBody CreateContactUsDTO dto) {
        return ResponseEntity.ok(contactUsService.createContactUs(dto));
    }

    @GetMapping
    public ResponseEntity<Page<ContactUsResponseDTO>> getAllContacts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sortBy);
        return ResponseEntity.ok(contactUsService.getAllContacts(pageRequest));
    }


    @PutMapping("/update-batch")
    public ResponseEntity<Void> updateBatch(@RequestBody List<Integer> contacts) {
        contactUsService.updateBatch(contacts);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactUsResponseDTO> getContactById(@PathVariable Integer id) {
        return ResponseEntity.ok(contactUsService.getContactById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Integer id) {
        contactUsService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }
}
