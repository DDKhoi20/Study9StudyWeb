package com.example.Study9StudyWeb.repository;

import com.example.Study9StudyWeb.entity.ExamEntity;
import com.example.Study9StudyWeb.entity.ExamTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamRepository extends JpaRepository<ExamEntity, Long> {
    List<ExamEntity> findByExamType(ExamTypeEntity examType);

}
