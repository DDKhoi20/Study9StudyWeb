package com.example.Study9StudyWeb.service;

import com.example.Study9StudyWeb.DTO.QuestionDto;
import com.example.Study9StudyWeb.entity.AnswerEntity;
import com.example.Study9StudyWeb.entity.ExamEntity;
import com.example.Study9StudyWeb.entity.QuestionEntity;
import com.example.Study9StudyWeb.repository.ExamRepository;
import com.example.Study9StudyWeb.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private ExamRepository examRepository;

    public void saveQuestion(QuestionDto dto) {
        ExamEntity exam = examRepository.findById(dto.getExamId())
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        QuestionEntity question = new QuestionEntity();
        question.setContent(dto.getContent());
        question.setImageUrl(dto.getImageUrl());
        question.setAudioUrl(dto.getAudioUrl());
        question.setExam(exam);

        List<AnswerEntity> answers = dto.getAnswers().stream().map(a -> {
            AnswerEntity answer = new AnswerEntity();
            answer.setContent(a.getContent());
            answer.setCorrect(a.isCorrect());
            answer.setQuestion(question);
            return answer;
        }).collect(Collectors.toList());

        question.setAnswers(answers);
        questionRepository.save(question);
    }
}
