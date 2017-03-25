package com.nullio.opinieallegro.transfer;

public class LoginRequest {

    final String userLogin;
    final String hashPass;

    public LoginRequest(String userLogin, String hashPass) {
        this.userLogin = userLogin;
        this.hashPass = hashPass;
    }
}
