package com.abacus.android.model;

public class History {

    private String question;
    private String solution;
    private boolean isLatex;
    private String time;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public boolean isLatex() {
        return isLatex;
    }

    public void setLatex(boolean latex) {
        isLatex = latex;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
