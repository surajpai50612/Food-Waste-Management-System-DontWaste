package com.example.dontwaste;

public class Users {
    public String orgName,regName,email,phone,password;

    public Users(){

    }

    // constructor to initialize variables
    public Users(String orgName,String regName,String email,String phone,String password){
        this.orgName=orgName;
        this.regName=regName;
        this.email=email;
        this.phone=phone;
        this.password=password;
    }
}