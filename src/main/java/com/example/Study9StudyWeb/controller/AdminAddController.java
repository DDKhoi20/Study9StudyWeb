package com.example.Study9StudyWeb.controller;

import com.example.Study9StudyWeb.DTO.QuestionDto;
import com.example.Study9StudyWeb.entity.ExamEntity;
import com.example.Study9StudyWeb.entity.ExamTypeEntity;
import com.example.Study9StudyWeb.repository.ExamRepository;
import com.example.Study9StudyWeb.repository.ExamTypeRepository;
import com.example.Study9StudyWeb.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class AdminAddController {

    @Autowired
    ExamTypeRepository examTypeRepository;
    @Autowired
    ExamRepository examRepository;
    @Autowired
    QuestionService questionService;

    @GetMapping("/addTypeExam")
    public String addTypeExam(){
        return "/addData/addTypeExam";
    }

    @PostMapping("/exam-type-save")
    public String examTypeSave(@RequestParam("name") String name){
        ExamTypeEntity examTypeEntity = new ExamTypeEntity();
        examTypeEntity.setName(name);
        examTypeRepository.save(examTypeEntity);
        return "/hello";
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
        return "/hello";
    }

    @GetMapping("/addQuestion")
    public String addQuestion(Model model){
        model.addAttribute("exams", examRepository.findAll());
        return "/addData/addQuestion";
    }

    @PostMapping("/addQuestion")
    public String addQuestion(@ModelAttribute QuestionDto questionDto) {
        questionService.saveQuestion(questionDto);
        return "hello";
    }

}
