package com.example.rentit;

/**
 * Created by aashish on 2/2/18.
 */

public class User {
    String email,password,phone,username;
    boolean emailVerified;
    public User(){

    }
    public User(String email,String password,String phone,boolean emailVerified,String username){
        this.email=email;
        this.password=password;
        this.phone=phone;
        this.emailVerified=emailVerified;
        this.username=username;
    }
    public String getEmail(){
        return this.email;
    }
    public String getPassword(){
        return this.password;
    }
    public String getPhone(){
        return this.phone;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public void setPassword(String Password){
        this.password=Password;
    }
    public void setPhone(String phone){
        this.phone=phone;
    }
    public void setEmailVerified(boolean emailVerified){
        this.emailVerified=emailVerified;
    }
    public boolean getEmailVerified(){
        return this.emailVerified;
    }
}
