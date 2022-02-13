package com.dev.assignment.Model;

import java.util.List;

public class Users {
    List<LoginResponse> result;

    public Users(){

    }

    public Users(List<LoginResponse> result) {
        this.result = result;
    }

    public List<LoginResponse> getResult() {
        return result;
    }

    public void setResult(List<LoginResponse> result) {
        this.result = result;
    }
}
