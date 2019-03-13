package com.example.vaibh.mosaic;

/**
 * Created by vaibh on 1/26/2019.
 */

public class User {

    private String Name;
    private String LastName;
    private String Email;
    private String Phone;
    private String Password;
    private String ConfirmPassword;

    public User() {
    }

    public User(String name, String lastName, String email, String phone, String password, String confirmPassword) {
        Name = name;
        LastName = lastName;
        Email = email;
        Phone = phone;
        Password = password;
        ConfirmPassword = confirmPassword;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getConfirmPassword() {
        return ConfirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        ConfirmPassword = confirmPassword;
    }
}