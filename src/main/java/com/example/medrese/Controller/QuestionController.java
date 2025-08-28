package com.example.medrese.Controller;


import com.example.medrese.DTO.Request.Create.CreateQuestionDTO;
import com.example.medrese.DTO.Response.QuestionResponse;
import com.example.medrese.DTO.Response.QuestionSearchResponse;
import com.example.medrese.Model.Question;
import com.example.medrese.Service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping
    public ResponseEntity<?> getAllQuestions(@RequestParam(defaultValue = "0", required = false) int page,
                                             @RequestParam(name = "maxResult", defaultValue = "10", required = false) int size,
                                             @RequestParam(name = "tagIds", required = false) List<Integer> tagIds,
                                             @RequestParam(name = "containsTag",defaultValue = "0", required = false) int containsTag,
                                             @RequestParam(name = "searchQuery", required = false) String search,
                                             @RequestParam(name = "containsCategory",defaultValue = "0", required = false) int containsCategory) {
        return ResponseEntity.ok(questionService.getAllQuestions(page, size, tagIds, containsTag, containsCategory,search));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getQuestionById(@PathVariable Integer id) {
        return ResponseEntity.ok(questionService.getQuestionById(id));
    }

    @PostMapping
    public ResponseEntity<QuestionResponse> createQuestion(@RequestBody CreateQuestionDTO createQuestionDTO) {
        return ResponseEntity.ok(questionService.createQuestion(createQuestionDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Question> updateQuestion(@PathVariable Integer id, @RequestBody Question questionDetails) {
        try {
            Question updatedQuestion = questionService.updateQuestion(id, questionDetails);
            return ResponseEntity.ok(updatedQuestion);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Integer id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statistics")
    public ResponseEntity<?> getQuestionStatistics() {
        return ResponseEntity.ok(questionService.getQuestionStatistics());
    }

    @PutMapping("/{id}/increment-view")
    public ResponseEntity<Void> incrementQuestionViewCount(@PathVariable Integer id) {
        questionService.incrementQuestionViewCount(id);
        return ResponseEntity.ok().build();
    }

}
