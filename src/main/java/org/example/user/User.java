package org.example.user;

import org.apache.commons.lang3.RandomStringUtils;

public class User {
    private String email;
    private String password;
    public String name;

    public User() {
    }

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static User getRandom() {
        final String email = RandomStringUtils.randomAlphabetic(6).toLowerCase() + "@yandex.ru";
        final String password = RandomStringUtils.randomAlphabetic(10);
        final String name = RandomStringUtils.randomAlphabetic(10);
        return new User(email, password, name);
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }
    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public static User getUserWithoutEmail() {
        return new User().setPassword(RandomStringUtils.randomAlphabetic(10)).setName(RandomStringUtils.randomAlphabetic(10));
    }
    public static User getUserWithoutPassword() {
        return new User().setEmail(RandomStringUtils.randomAlphabetic(6) + "@mail.ru").setName(RandomStringUtils.randomAlphabetic(10));
    }
    public static User getUserWithoutName() {
        return new User().setEmail(RandomStringUtils.randomAlphabetic(6) + "@mail.ru").setPassword(RandomStringUtils.randomAlphabetic(10));
    }


}


