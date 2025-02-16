package com.example.Study9StudyWeb.controller;

import com.example.Study9StudyWeb.entity.AccountEntity;
import com.example.Study9StudyWeb.repository.*;
import com.example.Study9StudyWeb.service.CustomUserDetailsService;
import com.example.Study9StudyWeb.service.QuestionService;
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
public class UserController {
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    ExamRepository examRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserAnswerRepository userAnswerRepository;
    @Autowired
    UserExamRepository userExamRepository;
    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    QuestionService questionService;

    @GetMapping("/study9/myprofile")
    public String myProfile(Model model){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        AccountEntity account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        boolean logIn = customUserDetailsService.isUserLoggedIn();
        model.addAttribute("logIn", logIn);
        model.addAttribute("account", account);
        model.addAttribute("questionService", questionService);
        return "myProfile";
    }


}
