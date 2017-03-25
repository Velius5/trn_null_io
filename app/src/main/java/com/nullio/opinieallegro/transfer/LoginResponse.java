package com.nullio.opinieallegro.transfer;

public class LoginResponse {

    public String userId;

    public LoginResponse(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
