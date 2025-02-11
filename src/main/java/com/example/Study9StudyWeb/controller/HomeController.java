package com.example.Study9StudyWeb.controller;

import com.example.Study9StudyWeb.entity.ExamTypeEntity;
import com.example.Study9StudyWeb.repository.ExamRepository;
import com.example.Study9StudyWeb.repository.ExamTypeRepository;
import com.example.Study9StudyWeb.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    ExamTypeRepository examTypeRepository;
    @Autowired
    ExamRepository examRepository;
    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @GetMapping("/study9/home")
    public String study9Home(Model model){
        boolean logIn = customUserDetailsService.isUserLoggedIn();
        model.addAttribute("logIn", logIn);
        return "homePage";
    }

    @GetMapping("/study9/test")
    public String study9Test(Model model,
                         @RequestParam(defaultValue = "1") Long idExamType){
        ExamTypeEntity examType = examTypeRepository.findById(idExamType)
                .orElseThrow(() -> new RuntimeException("ExamType not found with id: " + idExamType));

        model.addAttribute("examTypes", examTypeRepository.findAll());
        model.addAttribute("exam", examRepository.findByExamType(examType));
        model.addAttribute("idExamTypeCurrent", idExamType);
        boolean logIn = customUserDetailsService.isUserLoggedIn();
        model.addAttribute("logIn", logIn);
        return "homePageTest";
    }
}
