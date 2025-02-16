package com.example.Study9StudyWeb.service;

import com.example.Study9StudyWeb.repository.UserExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserExamService {
    @Autowired
    UserExamRepository userExamRepository;

    public int countNumberUserExam(Long examId){
        return userExamRepository.countByExam(examId);
    }
}
