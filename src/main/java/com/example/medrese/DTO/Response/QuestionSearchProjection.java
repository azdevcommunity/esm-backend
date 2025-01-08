package com.example.medrese.DTO.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

//public interface QuestionSearchProjection {
//    Integer getId();
//    String getQuestionText();
//    String getAnswerText();
//    String getCategories(); // JSON result as String
//    String getTags();       // JSON result as String
//}

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionSearchProjection {
    private Integer id;
    private String questionText;
    private String answerText;
    private List<Map<String, Object>> categories; // Parsed JSON
    private List<Map<String, Object>> tags;       // Parsed JSON

    public void setCategories(String categoriesJson) {
        this.categories = parseJsonArray(categoriesJson);
    }

    public void setTags(String tagsJson) {
        this.tags = parseJsonArray(tagsJson);
    }

    private List<Map<String, Object>> parseJsonArray(String json) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse JSON: " + json, e);
        }
    }
}