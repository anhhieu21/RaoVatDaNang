package com.dev.assignment.itemFB;

public class BackJson {
    private boolean status;
    private String message;
    private User user;

    public BackJson(boolean status, String message, User user) {
        this.status = status;
        this.message = message;
        this.user = user;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "BackJson{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", user=" + user +
                '}';
    }
}
