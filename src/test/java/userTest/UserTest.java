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

public class UserTest {
    private User user;
    private UserClient userClient;
    private String accessToken;


    @Before
    public void setUp() {
        user = User.getRandom();
        userClient = new UserClient();
    }

    @After
    public void tearDown() {
        userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Успешное создание пользователя")
    @Description("Проверка успешной регистрации пользователя с корректным логином, паролем и именем")
    public void createUserTest() {
        // Создать пользователя
        ValidatableResponse response = userClient.createUser(user);
        // Получить статус кода запроса
        int statusCode = response.extract().statusCode();
        //Получить значение ключа "success"
        boolean isUserCreated = response.extract().path("success");
        //Авторизоваться пользователем
        ValidatableResponse responseUserLogged = userClient.login(UserCredentials.from(new User(user.getEmail(), user.getPassword())));
        //Получить refresh токен
        String refreshToken = responseUserLogged.extract().path("refreshToken");
        //Получить access токен
        accessToken = responseUserLogged.extract().path("accessToken");
        // Asserts
        // Успешный запрос возвращает ok: true и status code: 200, )
        assertTrue("User is not created", isUserCreated);
        assertThat("Status code is incorrect", statusCode, equalTo(200));
        //Проверить токены
        assertNotNull("User access token is incorrect", accessToken);
        assertNotNull("User refresh token is incorrect", refreshToken);
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых пользователей")
    @Description("Если пользователь существует, вернётся код ответа 403")
    public void createSimilarUserTest() {
        //Создать пользователя
        userClient.createUser(user);
        //Повторно создать пользователя
        ValidatableResponse response = userClient.createUser(user);
        // Получить статус кода запроса
        int statusCode = response.extract().statusCode();
        //Получить значение ключа "success"
        boolean isSuccess = response.extract().path("success");
        //Получить значение ключа "message"
        String message = response.extract().path("message");
        //Проверить статус кода
        assertThat(statusCode, equalTo(403));
        //Проверить значение ключа "message"
        assertThat(message, equalTo("User already exists"));
        //Проверить значение ключа "success"
        assertFalse(isSuccess);
    }
}
