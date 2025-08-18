package com.example.medrese.Controller;

import com.example.medrese.DTO.Request.Create.CreateTagDTO;
import com.example.medrese.DTO.Request.Update.UpdateTag;
import com.example.medrese.DTO.Response.TagResponse;
import com.example.medrese.Service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;


    @PostMapping
    public ResponseEntity<TagResponse> createTag(@RequestBody CreateTagDTO tag) {
        return ResponseEntity.ok(tagService.createTag(tag));
    }

    @GetMapping
    public ResponseEntity<List<TagResponse>> getAllTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagResponse> getTagById(@PathVariable Integer id) {
        return ResponseEntity.ok(tagService.getTagById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagResponse> updateTag(@PathVariable Integer id, @RequestBody UpdateTag updatedTag) {
        return ResponseEntity.ok(tagService.updateTag(id, updatedTag));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Integer id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/clear-cache")
    public ResponseEntity<String> clearCache() {
        tagService.clearAllTagsCache();
        return ResponseEntity.ok("Cache cleared");
    }
}