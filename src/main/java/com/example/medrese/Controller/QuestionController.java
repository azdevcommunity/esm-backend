package com.example.medrese.Controller;


import com.example.medrese.DTO.Request.Create.CreateQuestionDTO;
import com.example.medrese.DTO.Response.QuestionResponse;
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
    @Autowired
    private QuestionService questionService;

    @GetMapping
    public List<Question> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable Integer id) {
        return ResponseEntity.ok(questionService.getQuestionById(id));
    }

    @PostMapping
    public ResponseEntity<QuestionResponse> createQuestion(@RequestBody CreateQuestionDTO createQuestionDTO) {
        return ResponseEntity.ok(questionService.createQuestion(createQuestionDTO)) ;
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

}
