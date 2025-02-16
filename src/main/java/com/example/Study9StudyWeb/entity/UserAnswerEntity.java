package com.example.Study9StudyWeb.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "userAnswer")
public class UserAnswerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_exam_id", nullable = false)
    private UserExamEntity userExam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private QuestionEntity question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id", nullable = true)
    private AnswerEntity selectedAnswer;

    @Column(nullable = false)
    private boolean isCorrect;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserExamEntity getUserExam() {
        return userExam;
    }

    public void setUserExam(UserExamEntity userExam) {
        this.userExam = userExam;
    }

    public QuestionEntity getQuestion() {
        return question;
    }

    public void setQuestion(QuestionEntity question) {
        this.question = question;
    }

    public AnswerEntity getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(AnswerEntity selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}
