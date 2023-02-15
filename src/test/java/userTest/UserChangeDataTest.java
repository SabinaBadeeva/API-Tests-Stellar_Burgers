package userTest;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.user.User;
import org.example.user.UserClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;

public class UserChangeDataTest {
    private User user;
    private User newUser;
    private UserClient userClient;
    private String accessToken;
    private String refreshToken;

    @Before
    public void setUp() {
        user = User.getRandom();
        userClient = new UserClient();
        newUser = User.getRandom();
    }

    @After
    public void tearDown() {
        userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Изменение данных пользователя с авторизацией")
    @Description("Сервер вернёт обновлённого пользователя success: true")
    public void changeUserDataWithAuthTest() {
        //Создать пользователя и получить токен
        ValidatableResponse response = userClient.createUser(user);
        accessToken = response.extract().path("accessToken");
        refreshToken = accessToken.substring(7);
        //Изменить данные пользователя
        ValidatableResponse responseChangeData = userClient.changeData(newUser, refreshToken);
        //Получить значение ключа "success"
        boolean isUserDataChanged = responseChangeData.extract().path("success");
        //Получить значение ключа "email"
        String actualEmail = responseChangeData.extract().path("user.email");
        //Получить значение ключа "name"
        String actualName = responseChangeData.extract().path("user.name");
        // Получить статус кода запроса
        int statusCode = responseChangeData.extract().statusCode();
        // Assert
        //Успешный запрос возвращает success: true и статус код 200
        assertThat(isUserDataChanged, is(true));
        assertThat(statusCode, equalTo(200));
        //Проверить токен
        assertNotNull("access token is incorrect", accessToken);
        //Проверить значение  "email"
        assertThat(actualEmail, equalTo(newUser.getEmail()));
        //Проверить значение  "name"
        assertThat(actualName, equalTo(newUser.getName()));
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    @Description("Если выполнить запрос без авторизации, вернётся код ответа 401 Unauthorized")
    public void changeUserDataWithoutAuthTest() {
        //создать пользователя
        userClient.createUser(user);
        //Изменить данные пользователя
        ValidatableResponse response = userClient.changeDataWithoutToken(newUser);
        //Получить значение ключа "success"
        boolean isUserDataChanged = response.extract().path("success");
        //Получить значение ключа "message"
        String message = response.extract().path("message");
        // Получить статус кода запроса
        int statusCode = response.extract().statusCode();
        // Assert
        //Проверить значение ключа "success"
        assertThat(isUserDataChanged, is(false));
        // Проверить значение статус кода
        assertThat(statusCode, equalTo(401));
        //Проверить значение ключа "message"
        assertThat(message, equalTo("You should be authorised"));
    }
}
