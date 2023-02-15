package userTest;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.user.User;
import org.example.user.UserClient;
import org.example.user.UserCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class UserLoginTest {
    private User user;
    private UserClient userClient;
    private String accessToken;
    private String refreshToken;


    @Before
    public void setUp() {
        user = User.getRandom();
        userClient = new UserClient();
    }

    @After
    public void tearDown() {
        if (accessToken != null)
            userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Вход под существующим пользователем")
    @Description("Успешная авторизация с валидными email  и паролем возвращает success: true")
    public void courierSuccessLogIn() {
        //Создать пользователя
        userClient.createUser(user);
        //Авторизоваться пользователем
        ValidatableResponse response = userClient.login(UserCredentials.from(new User(user.getEmail(), user.getPassword())));
        //Получить статус код запроса
        int statusCodeResponse = response.extract().statusCode();
        //Получить значение ключа "success"
        boolean isUserCreated = response.extract().path("success");
        //Получить access токен
        accessToken = response.extract().path("accessToken");
        //Получить refresh токен
        String refreshToken = response.extract().path("refreshToken");
        //Получить значение ключа "email"
        String actualEmail = response.extract().path("user.email");
        //Получить значение ключа "name"
        String actualName = response.extract().path("user.name");
        //Проверить статус код
        assertThat("Status code is not correct", statusCodeResponse, equalTo(200));
        //Проверить значение ключа "success"
        assertTrue("User not logged in", isUserCreated);
        //Проверить токены
        assertNotNull(accessToken);
        assertNotNull(refreshToken);
        //Проверить значение ключей "email" и "name"
        assertThat("Пользователь авторизовался под другим email", actualEmail, equalTo(user.getEmail()));
        assertThat("Пользователь авторизовался под другим name", actualName, equalTo(user.getName()));
    }

    @Test
    @DisplayName("Неуспешная авторизация пользователя")
    @Description("Попытка авторизации с неверным email")
    public void unsuccessfulValidationUserEmailTest() {
        //Создать пользователя
        userClient.createUser(user);
        //Авторизоваться с несуществующим email
        ValidatableResponse response = userClient.login(UserCredentials.getUserWithRandomEmail(user));
        //Получить статус код запроса
        int statusCodeResponse = response.extract().statusCode();
        //Получить значение ключа "success"
        boolean isUserNotLogged = response.extract().path("success");
        //Получить значение ключа "message"
        String message = response.extract().path("message");
        //Получить access токен
        accessToken = response.extract().path("accessToken");
        //Получить refresh токен
        String refreshToken = response.extract().path("refreshToken");
        //Assert
        //Проверить статус код
        assertThat(statusCodeResponse, equalTo(401));
        //Проверить значение ключа "message"
        assertThat(message, equalTo("email or password are incorrect"));
        //Проверить значение ключа "success"
        assertFalse(isUserNotLogged);
    }

    @Test
    @DisplayName("Неуспешная авторизация пользователя")
    @Description("Попытка авторизации с неверным паролем")
    public void unsuccessfulValidationUserPasswordTest() {
        //Создать пользователя
        userClient.createUser(user);
        //Авторизоваться с несуществующим email
        ValidatableResponse response = userClient.login(UserCredentials.getUserWithRandomPassword(user));
        //Получить статус код запроса
        int statusCodeResponse = response.extract().statusCode();
        //Получить значение ключа "success"
        boolean isUserNotLogged = response.extract().path("success");
        //Получить значение ключа "message"
        String message = response.extract().path("message");
        //Получить access токен
        accessToken = response.extract().path("accessToken");
        //Получить refresh токен
        String refreshToken = response.extract().path("refreshToken");
        //Assert
        //Проверить статус код
        assertThat(statusCodeResponse, equalTo(401));
        //Проверить значение ключа "message"
        assertThat(message, equalTo("email or password are incorrect"));
        //Проверить значение ключа "success"
        assertFalse(isUserNotLogged);
    }
}


