package com.example.Study9StudyWeb.repository;

import com.example.Study9StudyWeb.entity.ExamEntity;
import com.example.Study9StudyWeb.entity.UserExamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserExamRepository extends JpaRepository<UserExamEntity, Long> {
    @Query("SELECT COUNT(q) FROM UserExamEntity q WHERE q.exam.id = :exam")
    int countByExam(@Param("exam") Long exam);
}
