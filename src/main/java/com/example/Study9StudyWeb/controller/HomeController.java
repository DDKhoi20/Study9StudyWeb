package com.example.Study9StudyWeb.controller;

import com.example.Study9StudyWeb.entity.AccountEntity;
import com.example.Study9StudyWeb.entity.ExamTypeEntity;
import com.example.Study9StudyWeb.repository.AccountRepository;
import com.example.Study9StudyWeb.repository.ExamRepository;
import com.example.Study9StudyWeb.repository.ExamTypeRepository;
import com.example.Study9StudyWeb.service.CustomUserDetailsService;
import com.example.Study9StudyWeb.service.UserExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserExamService userExamService;

    @GetMapping("/study9/home")
    public String study9Home(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        AccountEntity account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        model.addAttribute("account", account);
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
        model.addAttribute("userExamService", userExamService);
        boolean logIn = customUserDetailsService.isUserLoggedIn();
        model.addAttribute("logIn", logIn);
        return "homePageTest";
    }
}
