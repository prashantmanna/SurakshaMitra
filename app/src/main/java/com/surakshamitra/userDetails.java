package com.surakshamitra;

public class userDetails {
    public String username,email,mobile,password,passwordConfirm;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public userDetails(String username,String email, String mobile, String password, String passwordConfirm)
    {

        this.username = username;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }
    public userDetails()
    {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
