package com.example.medrese.Service;

import com.example.medrese.DTO.Request.Create.CreateTagDTO;
import com.example.medrese.DTO.Request.Update.UpdateTag;
import com.example.medrese.DTO.Response.TagResponse;
import com.example.medrese.Model.Tag;
import com.example.medrese.Repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    // Create a new tag
    public TagResponse createTag(CreateTagDTO request) {
     Tag tag = Tag.builder()
             .name(request.getName())
             .description(request.getDescription())
             .build();

        return toResponse(tagRepository.save(tag));
    }

    // Get all tags
    public List<TagResponse> getAllTags() {
        return tagRepository.findAll().stream().map(this::toResponse).toList();
    }

    public TagResponse getTagById(Integer id) {
        return tagRepository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Tag not found with id: " + id));
    }

    public TagResponse updateTag(Integer id, UpdateTag updatedTag) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found with id: " + id));

        tag.setName(updatedTag.getName());
        tag.setDescription(updatedTag.getDescription());
        return toResponse(tagRepository.save(tag));
    }

    public void deleteTag(Integer id) {
        if (!tagRepository.existsById(id)) {
            throw new RuntimeException("Tag not found with id: " + id);
        }
        tagRepository.deleteById(id);
    }

    private TagResponse toResponse(Tag tag) {
        return TagResponse.builder()
                .id(tag.getId())
                .name(tag.getName())
                .description(tag.getDescription())
                .build();
    }
}