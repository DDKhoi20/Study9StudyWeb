package com.example.Study9StudyWeb.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_exam")
public class UserExamEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AccountEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    private ExamEntity exam;

    @OneToMany(mappedBy = "userExam", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserAnswerEntity> userAnswers = new ArrayList<>();

    private int score;

    private LocalDateTime examDate = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AccountEntity getUser() {
        return user;
    }

    public void setUser(AccountEntity user) {
        this.user = user;
    }

    public ExamEntity getExam() {
        return exam;
    }

    public void setExam(ExamEntity exam) {
        this.exam = exam;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public LocalDateTime getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDateTime examDate) {
        this.examDate = examDate;
    }

    public List<UserAnswerEntity> getUserAnswers() {
        return userAnswers;
    }

    public void setUserAnswers(List<UserAnswerEntity> userAnswers) {
        this.userAnswers = userAnswers;
    }
}
