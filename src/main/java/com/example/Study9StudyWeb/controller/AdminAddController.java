package com.example.Study9StudyWeb.controller;

import com.example.Study9StudyWeb.DTO.QuestionDto;
import com.example.Study9StudyWeb.entity.AnswerEntity;
import com.example.Study9StudyWeb.entity.ExamEntity;
import com.example.Study9StudyWeb.entity.ExamTypeEntity;
import com.example.Study9StudyWeb.entity.QuestionEntity;
import com.example.Study9StudyWeb.repository.AnswerRepository;
import com.example.Study9StudyWeb.repository.ExamRepository;
import com.example.Study9StudyWeb.repository.ExamTypeRepository;
import com.example.Study9StudyWeb.repository.QuestionRepository;
import com.example.Study9StudyWeb.service.CustomUserDetailsService;
import com.example.Study9StudyWeb.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class AdminAddController {

    @Autowired
    ExamTypeRepository examTypeRepository;
    @Autowired
    ExamRepository examRepository;
    @Autowired
    QuestionService questionService;
    @Autowired
    CustomUserDetailsService customUserDetailsService;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    AnswerRepository answerRepository;

    @GetMapping("/home")
    public String home(Model model){
        boolean logIn = customUserDetailsService.isUserLoggedIn();
        model.addAttribute("logIn", logIn);

        model.addAttribute("examTypes", examTypeRepository.findAll());
        model.addAttribute("exams", examRepository.findAll());

        return "adminHome";
    }

    @GetMapping("/addTypeExam")
    public String addTypeExam(){
        return "/addData/addTypeExam";
    }

    @PostMapping("/exam-type-save")
    public String examTypeSave(@RequestParam("name") String name){
        ExamTypeEntity examTypeEntity = new ExamTypeEntity();
        examTypeEntity.setName(name);
        examTypeRepository.save(examTypeEntity);
        return "/adminHome";
    }

    @GetMapping("/addExam")
    public String addExam(Model model){
        model.addAttribute("typeExam", examTypeRepository.findAll());
        return "/addData/addExam";
    }

    @PostMapping("/exam-save")
    public String saveExam(@RequestParam("name") String name,
                           @RequestParam("typeExam") Long typeExam){
        ExamEntity exam = new ExamEntity();
        exam.setName(name);

        ExamTypeEntity examType = examTypeRepository.findById(typeExam)
                .orElseThrow(() -> new RuntimeException("ExamType not found with id: " + typeExam));

        exam.setExamType(examType);

        examRepository.save(exam);
        return "/adminHome";
    }

    @GetMapping("/addQuestion")
    public String addQuestion(Model model){
        model.addAttribute("exams", examRepository.findAll());
        return "/addData/addQuestion";
    }

    @PostMapping("/addQuestion")
    public String addQuestion(@ModelAttribute QuestionDto questionDto) {
        questionService.saveQuestion(questionDto);
        return "adminHome";
    }

    @PostMapping("/typeExam/update")
    public String updateTypeExam(@RequestParam Long id, @RequestParam String name) {
        ExamTypeEntity typeExam = examTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid TypeExam ID: " + id));
        typeExam.setName(name);
        examTypeRepository.save(typeExam);
        return "redirect:/home";
    }

    @PostMapping("/exam/update")
    public String updateExam(@RequestParam Long id, @RequestParam String name, @RequestParam Long examTypeId) {
        ExamEntity exam = examRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Exam ID: " + id));

        ExamTypeEntity examType = examTypeRepository.findById(examTypeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid TypeExam ID: " + examTypeId));
        exam.setName(name);
        exam.setExamType(examType);
        examRepository.save(exam);
        return "redirect:/home";
    }

    @GetMapping("/editQuestionExam/{examId}")
    public String showExamEditPage(@PathVariable Long examId, Model model) {
        Optional<ExamEntity> examOptional = examRepository.findById(examId);
        if (examOptional.isEmpty()) {
            return "error";
        }
        boolean logIn = customUserDetailsService.isUserLoggedIn();
        model.addAttribute("logIn", logIn);
        ExamEntity exam = examOptional.get();
        model.addAttribute("exam", exam);
        model.addAttribute("questions", exam.getQuestions());
        return "editQuestion";
    }

    @PostMapping("/editSingleQuestion")
    public String editSingleQuestion(@RequestParam("questionId") Long questionId,
                                     @RequestParam("content") String content,
                                     @RequestParam("imageUrl") String imageUrl,
                                     @RequestParam("audioUrl") String audioUrl,
                                     @RequestParam(value = "correctAnswers", required = false) List<Long> correctAnswers,
                                     @RequestParam Map<String, String> allParams) {

        QuestionEntity question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        question.setContent(content);
        question.setImageUrl(imageUrl);
        question.setAudioUrl(audioUrl);

        for (AnswerEntity answer : question.getAnswers()) {
            String newContent = allParams.get("answer_" + answer.getId());
            if (newContent != null) {
                answer.setContent(newContent);
            }

            answer.setCorrect(correctAnswers != null && correctAnswers.contains(answer.getId()));
        }

        questionRepository.save(question);
        return "redirect:/exam/" + question.getExam().getId();
    }

}
