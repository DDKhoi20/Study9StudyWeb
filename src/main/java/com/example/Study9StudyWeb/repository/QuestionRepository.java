package com.example.Study9StudyWeb.repository;

import com.example.Study9StudyWeb.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<QuestionEntity, Long> {
    @Query("SELECT COUNT(q) FROM QuestionEntity q WHERE q.exam.id = :examId")
    int countQuestionsByExamId(@Param("examId") Long examId);

}
