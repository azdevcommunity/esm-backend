package com.example.medrese.Service;


import com.example.medrese.Core.Util.Exceptions.QuestionNotFoundException;
import com.example.medrese.DTO.Request.Create.CreateQuestionDTO;
import com.example.medrese.DTO.Response.QuestionResponse;
import com.example.medrese.DTO.Response.QuestionSearchResponse;
import com.example.medrese.DTO.Response.QuestionStatisticsResponse;
import com.example.medrese.DTO.Response.QuestionTagResponse;
import com.example.medrese.DTO.Response.RelatedQuestionResponse;
import com.example.medrese.Model.Question;
import com.example.medrese.Model.QuestionCategory;
import com.example.medrese.Model.QuestionTag;
import com.example.medrese.Repository.CategoryRepository;
import com.example.medrese.Repository.QuestionCategoryRepository;
import com.example.medrese.Repository.QuestionRepository;
import com.example.medrese.Repository.QuestionTagRepository;
import com.example.medrese.Repository.TagRepository;
import com.example.medrese.mapper.QuestionMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)

public class QuestionService {

    QuestionRepository questionRepository;
    QuestionMapper questionMapper;
    QuestionCategoryRepository questionCategoryRepository;
    TagRepository tagRepository;
    QuestionTagRepository questionTagRepository;
    AuthorService authorService;
    CategoryRepository categoryRepository;

    public Page<?> getAllQuestions(int page, int size, List<Integer> tagIds, List<Integer> categoryIds, int containsTag, int containsCategory, String search) {
        Pageable pageable = PageRequest.of(page, size);

        Page<QuestionSearchResponse> questionPage = questionRepository.searchAllQuestions(pageable,
                ObjectUtils.isEmpty(tagIds) ? null : tagIds, 
                ObjectUtils.isEmpty(categoryIds) ? null : categoryIds, 
                search
        );

        if(containsTag == 1 || containsCategory == 1){
            questionPage.forEach(question -> {
                if (containsTag == 1){
                    question.setTags(questionRepository.findAllTagsByQuestion(question.getId()));
                }
                if (containsCategory == 1){
                    question.setCategories(questionRepository.findAllCategoriesByQuestion(question.getId()));
                }
            });
        }

        return questionPage;
    }

    public QuestionSearchResponse getQuestionById(Integer id) {

        QuestionSearchResponse question = questionRepository.findQuestionById(id)
                .orElseThrow(() -> new QuestionNotFoundException("question not found"));
//        return questionRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("question not found"));
        question.setCategories(questionRepository.findAllCategoriesByQuestion(question.getId()));
        question.setTags(questionRepository.findAllTagsByQuestion(question.getId()));
        return question;
    }

    @Transactional
    public QuestionResponse createQuestion(CreateQuestionDTO createQuestionDTO) {
        //TODO:helelik
//        createQuestionDTO.setAuthor(2);
//
//        if (Objects.nonNull(createQuestionDTO.getAuthor())) {
//            authorService.getAuthorById(createQuestionDTO.getAuthor());
//        }

        Question question = questionMapper.toEntity(createQuestionDTO);
        question = questionRepository.save(question);
//        question.setAuthorId(createQuestionDTO.getAuthor());

        for (Integer category : createQuestionDTO.getCategories()) {
            if (!categoryRepository.existsById(category)) {
                continue;
            }
            QuestionCategory questionCategory = QuestionCategory.builder()
                    .categoryId(category)
                    .questionId(question.getId())
                    .build();
            questionCategoryRepository.save(questionCategory);
        }

        for (Integer category : createQuestionDTO.getTags()) {
            if (!tagRepository.existsById(category)) {
                continue;
            }
            QuestionTag questionTag = QuestionTag.builder()
                    .tagId(category)
                    .questionId(question.getId())
                    .build();
            questionTagRepository.save(questionTag);
        }

        return questionMapper.toResponse(question);
    }

    public Question updateQuestion(Integer id, Question questionDetails) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found with id " + id));
        question.setQuestion(questionDetails.getQuestion());
        question.setAnswer(questionDetails.getAnswer());
        question = questionRepository.save(question);
        return question;
    }

    @Transactional
    public void deleteQuestion(Integer id) {
        if (!questionRepository.existsById(id)) {
            throw new RuntimeException("does not exist");
        }

        questionCategoryRepository.deleteByQuestionId(id);
        questionTagRepository.deleteByQuestionId(id);
        questionRepository.deleteById(id);

    }

    public QuestionStatisticsResponse getQuestionStatistics() {
        Long totalQuestions = questionRepository.count();
        Long totalCategories = questionRepository.countDistinctCategoriesUsedInQuestions();
        Long totalTags = questionRepository.countDistinctTagsUsedInQuestions();
        Long totalViewCount = questionRepository.getTotalViewCount();

        return new QuestionStatisticsResponse(totalQuestions, totalCategories, totalTags, totalViewCount);
    }

    @Transactional
    public void incrementQuestionViewCount(Integer questionId) {
        if (!questionRepository.existsById(questionId)) {
            throw new QuestionNotFoundException("Question not found with id " + questionId);
        }
        questionRepository.incrementViewCount(questionId);
    }

    public List<RelatedQuestionResponse> getRelatedQuestions(Integer questionId) {
        if (!questionRepository.existsById(questionId)) {
            throw new QuestionNotFoundException("Question not found with id " + questionId);
        }

        Pageable pageable = PageRequest.of(0, 4);
        List<Object[]> relatedQuestionsData = questionRepository.findRelatedQuestionsByTagsAndCategories(questionId, pageable);

        List<RelatedQuestionResponse> relatedQuestions = new ArrayList<>();

        for (Object[] data : relatedQuestionsData) {
            Integer id = (Integer) data[0];
            String question = (String) data[1];

            List<QuestionTagResponse> tags = questionRepository.findAllTagsByQuestion(id);

            RelatedQuestionResponse relatedQuestion = new RelatedQuestionResponse(id, question, tags);
            relatedQuestions.add(relatedQuestion);
        }

        return relatedQuestions;
    }
}

