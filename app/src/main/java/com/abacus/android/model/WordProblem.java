package com.abacus.android.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WordProblem {
    private List<String> answers;
    private String question;
    @SerializedName("userID")
    private String userId;
    @SerializedName("predicted-operator")
    private String predictedOperator;
    private int operand1;
    private int operand2;
    private boolean isCorrect;
    private String email;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPredictedOperator() {
        return predictedOperator;
    }

    public void setPredictedOperator(String predictedOperator) {
        this.predictedOperator = predictedOperator;
    }

    public int getOperand1() {
        return operand1;
    }

    public void setOperand1(int operand1) {
        this.operand1 = operand1;
    }

    public int getOperand2() {
        return operand2;
    }

    public void setOperand2(int operand2) {
        this.operand2 = operand2;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
