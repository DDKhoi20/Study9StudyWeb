package com.example.Study9StudyWeb.controller;

import com.example.Study9StudyWeb.entity.*;
import com.example.Study9StudyWeb.repository.*;
import com.example.Study9StudyWeb.service.CustomUserDetailsService;
import com.example.Study9StudyWeb.service.QuestionService;
import com.example.Study9StudyWeb.service.UserExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Map;

@Controller
public class ExamController {
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
    @Autowired
    UserExamService userExamService;

    @GetMapping("/examDetail")
    public String getExamQuestions(@RequestParam(defaultValue = "1") Long idExam, Model model) {
        ExamEntity exam = examRepository.findById(idExam)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        model.addAttribute("exam", exam);
        model.addAttribute("questions", exam.getQuestions());
        boolean logIn = customUserDetailsService.isUserLoggedIn();
        model.addAttribute("logIn", logIn);
        return "examDetail";
    }

    @PostMapping("/submitExam")
    public String submitExam(@RequestParam Map<String, String> answers, Principal principal, Model model,
                             RedirectAttributes redirectAttributes) {
        AccountEntity user = accountRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ExamEntity exam = examRepository.findById(Long.parseLong(answers.get("examId")))
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        UserExamEntity userExam = new UserExamEntity();
        userExam.setUser(user);
        userExam.setExam(exam);
        userExamRepository.save(userExam);

        int correctCount = 0;

        for (String key : answers.keySet()) {
            if (key.startsWith("question-")) {
                Long questionId = Long.parseLong(key.replace("question-", ""));
                Long selectedAnswerId = Long.parseLong(answers.get(key));

                QuestionEntity question = questionRepository.findById(questionId)
                        .orElseThrow(() -> new RuntimeException("Question not found"));

                AnswerEntity selectedAnswer = answerRepository.findById(selectedAnswerId).orElse(null);

                boolean isCorrect = selectedAnswer != null && selectedAnswer.isCorrect();

                UserAnswerEntity userAnswer = new UserAnswerEntity();
                userAnswer.setUserExam(userExam);
                userAnswer.setQuestion(question);
                userAnswer.setSelectedAnswer(selectedAnswer);
                userAnswer.setCorrect(isCorrect);

                userAnswerRepository.save(userAnswer);

                if (isCorrect) {
                    correctCount++;
                }
            }
        }
        userExam.setScore(correctCount);
        userExamRepository.save(userExam);
        model.addAttribute("correctCount", correctCount);
//        redirectAttributes.addFlashAttribute("message", "Nộp bài thành công!");
        redirectAttributes.addFlashAttribute("userExam", userExam);
        boolean logIn = customUserDetailsService.isUserLoggedIn();
        redirectAttributes.addFlashAttribute("logIn", logIn);
        return "redirect:/study9/result?idUserExam=" + userExam.getId();
    }

    @GetMapping("/study9/result")
    public String stuty9Result(Model model,
                               @RequestParam("idUserExam") Long idUserExam){
        UserExamEntity userExam = userExamRepository.findById(idUserExam)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        model.addAttribute("userExam", userExam);
        boolean logIn = customUserDetailsService.isUserLoggedIn();
        model.addAttribute("logIn", logIn);

        model.addAttribute("countQuestion", questionRepository.countQuestionsByExamId(userExam.getExam().getId()));
        return "examResult";
    }
}
