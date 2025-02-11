package com.example.Study9StudyWeb.controller;

import com.example.Study9StudyWeb.entity.ExamEntity;
import com.example.Study9StudyWeb.repository.AnswerRepository;
import com.example.Study9StudyWeb.repository.ExamRepository;
import com.example.Study9StudyWeb.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ExamController {
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    ExamRepository examRepository;

    @GetMapping("/examDetail")
    public String getExamQuestions(@RequestParam(defaultValue = "1") Long idExam, Model model) {
        ExamEntity exam = examRepository.findById(idExam)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        model.addAttribute("exam", exam);
        model.addAttribute("questions", exam.getQuestions());

        return "examDetail";
    }
}
