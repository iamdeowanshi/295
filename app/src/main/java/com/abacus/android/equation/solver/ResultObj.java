package com.abacus.android.equation.solver;

import java.util.List;

public class ResultObj {
    private List<String> steps;
    private Double answer;
    private String status;

    public ResultObj(List<String> steps, Double ans, String _status){
        this.steps = steps;
        this.answer = ans;
        this.status = _status;
    }

    public List<String> getSteps() {
        return steps;
    }

    public Double getAnswer() {
        return answer;
    }

    public String getStatus() {
        return this.status;
    }
}
