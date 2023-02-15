package org.example.user;


import org.apache.commons.lang3.RandomStringUtils;

public class UserCredentials {
    private String email;
    private String password;

    public UserCredentials(String email, String password) {
        this.email = email;
        this.password = password;}

    public UserCredentials() {

    }

    public static UserCredentials from(User user) {
        return new UserCredentials(user.getEmail(), user.getPassword());
    }



    public static UserCredentials getUserWithRandomEmail (User user) {
        return new UserCredentials(RandomStringUtils.randomAlphabetic(6).toLowerCase() + "@mail.ru", user.getPassword());
    }
    public static UserCredentials getUserWithRandomPassword (User user) {
        return new UserCredentials(user.getEmail(), RandomStringUtils.randomAlphabetic(10));
    }


}
