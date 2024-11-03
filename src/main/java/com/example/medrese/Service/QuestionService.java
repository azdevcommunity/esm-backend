package com.example.medrese.Service;


import com.example.medrese.Model.Question;
import com.example.medrese.Repository.QuestionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Optional<Question> getQuestionById(Integer id) {
        return questionRepository.findById(id);
    }

    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }

    public Question updateQuestion(Integer id, Question questionDetails) {
        return questionRepository.findById(id).map(question -> {
            question.setQuestion(questionDetails.getQuestion());
            question.setAnswer(questionDetails.getAnswer());
            return questionRepository.save(question);
        }).orElseThrow(() -> new RuntimeException("Question not found with id " + id));
    }

    public void deleteQuestion(Integer id) {
        questionRepository.deleteById(id);
    }
}

