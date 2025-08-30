package com.example.medrese.Repository;

import com.example.medrese.DTO.Response.QuestionCategory;
import com.example.medrese.DTO.Response.QuestionSearchProjection;
import com.example.medrese.DTO.Response.QuestionSearchResponse;
import com.example.medrese.DTO.Response.QuestionTagResponse;
import com.example.medrese.Model.Question;
import com.example.medrese.Model.QuestionTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {

    @Query("""
                SELECT DISTINCT new com.example.medrese.DTO.Response.QuestionSearchResponse(
                    q.id,
                    q.question,
                    q.answer,
                    q.createdDate,
                    q.viewCount
                )
                FROM Question q
                LEFT JOIN QuestionTag qTag ON q.id = qTag.questionId
                LEFT JOIN QuestionCategory qCat ON q.id = qCat.questionId
                WHERE (:tagIds IS NULL OR qTag.tagId IN :tagIds)
                    AND (:categoryIds IS NULL OR qCat.categoryId IN :categoryIds)
                    and ((:search IS NULL OR :search = '')
                       OR (( LOWER(q.question) LIKE LOWER(CONCAT('%', :search, '%'))  ) or ( LOWER(q.answer) LIKE LOWER(CONCAT('%', :search, '%'))))
                                       )
                order by q.createdDate desc
            """)
    Page<QuestionSearchResponse> searchAllQuestions(Pageable pageable, @Param("tagIds") List<Integer> tagIds, @Param("categoryIds") List<Integer> categoryIds, String search);


    @Query("""
             SELECT DISTINCT new com.example.medrese.DTO.Response.QuestionCategory(qc.categoryId, c.name)
                        FROM QuestionCategory qc
                        JOIN Category c ON qc.categoryId = c.id
                        WHERE qc.questionId = :questionId
            """)
    List<QuestionCategory> findAllCategoriesByQuestion(int questionId);

    @Query("""
             SELECT DISTINCT new com.example.medrese.DTO.Response.QuestionTagResponse(qt.tagId, t.name)
                        FROM QuestionTag qt
                        JOIN Tag t ON qt.tagId = t.id
                        WHERE qt.questionId = :questionId
            """)
    List<QuestionTagResponse> findAllTagsByQuestion(int questionId);

    @Query("""
                SELECT new com.example.medrese.DTO.Response.QuestionSearchResponse(
                    q.id,
                    q.question,
                    q.answer,
                    q.createdDate,
                    q.viewCount
                )
                FROM Question q
                where q.id = :questionId
            """)
    Optional<QuestionSearchResponse> findQuestionById(int questionId);

    @Query(value = """

            SELECT q.id                          AS questionId,
                              q.question                    AS questionText,
                              q.answer                      AS answerText,
                              (SELECT json_agg(
                                              json_build_object('categoryId', qc.category_id, 'name', c.name)
                                      )
                               FROM question_categories qc
                                        JOIN categories c ON qc.category_id = c.id
                               WHERE qc.question_id = q.id) AS categories,
                              (SELECT json_agg(
                                              json_build_object('tagId', qt.tag_id, 'name', t.name)
                                      )
                               FROM question_tags qt
                                        JOIN tags t ON qt.tag_id = t.id
                               WHERE qt.question_id = q.id) AS tags
                       FROM questions q
            """,
            countQuery = """

                    SELECT COUNT(*) FROM questions q
    """,
            nativeQuery = true)
    Page<QuestionSearchProjection> searchAllQuestionsOptimized(Pageable pageable);

    @Query("SELECT COUNT(DISTINCT qc.categoryId) FROM QuestionCategory qc")
    Long countDistinctCategoriesUsedInQuestions();

    @Query("SELECT COUNT(DISTINCT qt.tagId) FROM QuestionTag qt")
    Long countDistinctTagsUsedInQuestions();

    @Modifying
    @Query("UPDATE Question q SET q.viewCount = q.viewCount + 1 WHERE q.id = :questionId")
    void incrementViewCount(@Param("questionId") Integer questionId);

    @Query("SELECT COALESCE(SUM(q.viewCount), 0) FROM Question q")
    Long getTotalViewCount();

    @Query("""
            SELECT DISTINCT q.id, q.question, q.viewCount, q.createdDate
            FROM Question q
            WHERE q.id != :questionId
            AND (
                q.id IN (
                    SELECT qt1.questionId
                    FROM QuestionTag qt1
                    WHERE qt1.tagId IN (
                        SELECT qt2.tagId
                        FROM QuestionTag qt2
                        WHERE qt2.questionId = :questionId
                    )
                )
                OR q.id IN (
                    SELECT qc1.questionId
                    FROM QuestionCategory qc1
                    WHERE qc1.categoryId IN (
                        SELECT qc2.categoryId
                        FROM QuestionCategory qc2
                        WHERE qc2.questionId = :questionId
                    )
                )
            )
            ORDER BY q.viewCount DESC, q.createdDate DESC
            """)
    List<Object[]> findRelatedQuestionsByTagsAndCategories(@Param("questionId") Integer questionId, Pageable pageable);

    }

