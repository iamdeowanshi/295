package com.abacus.android.model;

import java.util.List;

public class WordProblem {
    private List<String> answers;
    private String question;

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
