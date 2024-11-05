package com.example.medrese.Service;


import com.example.medrese.DTO.Request.Create.CreateQuestionDTO;
import com.example.medrese.DTO.Response.QuestionResponse;
import com.example.medrese.Model.Question;
import com.example.medrese.Model.QuestionCategory;
import com.example.medrese.Repository.QuestionCategoryRepository;
import com.example.medrese.Repository.QuestionRepository;
import com.example.medrese.mapper.QuestionMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)

public class QuestionService {

     QuestionRepository questionRepository;
     QuestionMapper questionMapper;
     QuestionCategoryRepository questionCategoryRepository;

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Question getQuestionById(Integer id) {
        return questionRepository.findById(id).orElseThrow(()-> new RuntimeException("question not found"));
    }

    @Transactional
    public QuestionResponse createQuestion(CreateQuestionDTO createQuestionDTO) {
        Question question = questionMapper.toEntity(createQuestionDTO);
        question = questionRepository.save(question);
        for (Integer category : createQuestionDTO.getCategories()) {
            QuestionCategory questionCategory = QuestionCategory.builder()
                    .categoryId(category)
                    .questionId(question.getId())
                    .build();
            questionCategoryRepository.save(questionCategory);
        }

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
        if (!questionRepository.existsById(id)){
            throw new RuntimeException("does not exist");
        }
       questionCategoryRepository.deleteByQuestionId(id);

        questionRepository.deleteById(id);
    }
}

