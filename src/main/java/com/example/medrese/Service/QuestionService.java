package com.example.medrese.Service;


import com.example.medrese.DTO.Request.Create.CreateQuestionDTO;
import com.example.medrese.DTO.Response.QuestionResponse;
import com.example.medrese.Model.Question;
import com.example.medrese.Repository.QuestionRepository;
import com.example.medrese.mapper.QuestionMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)

public class QuestionService {

     QuestionRepository questionRepository;
     QuestionMapper questionMapper;

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Question getQuestionById(Integer id) {
        return questionRepository.findById(id).orElseThrow(()-> new RuntimeException("question not found"));
    }

    public QuestionResponse createQuestion(CreateQuestionDTO createQuestionDTO) {
        Question question = questionMapper.toEntity(createQuestionDTO);
        question = questionRepository.save(question);
        return  questionMapper.toResponse(question);
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

