package userTest;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.user.User;
import org.example.user.UserClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;


@RunWith(Parameterized.class)
public class UserEmptyDataTest {

    private final User user;
    private final int expectedStatusCode;
    private final String errorMessage;
    private final boolean booleanExpected;
    private String accessToken;
    UserClient userClient;

    public UserEmptyDataTest(User user, int expectedStatusCode, String errorMessage, boolean booleanExpected) {
        this.user = user;
        this.expectedStatusCode = expectedStatusCode;
        this.errorMessage = errorMessage;
        this.booleanExpected = booleanExpected;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
                {User.getUserWithoutEmail(), 403, "Email, password and name are required fields", false},
                {User.getUserWithoutPassword(), 403, "Email, password and name are required fields", false},
                {User.getUserWithoutName(), 403, "Email, password and name are required fields", false}
        };
    }

    @Test
    @DisplayName("Создать пользователя и не заполнить одно из обязательных полей")
    @Description("Не заполнить одно из полей, вернётся код ответа 403")
    public void createUserWithoutRequiredField() {
        // Создать пользователя
        ValidatableResponse response = new UserClient().createUser(user);
        // Получить статус кода
        int statusCode = response.extract().statusCode();
        //Получить значение ключа "success"
        boolean actualBoolean = response.extract().path("success");
        //Получить значение ключа "message"
        String actualMessage = response.extract().path("message");
        //Asserts
        // Проверить статус код
        assertEquals(expectedStatusCode, statusCode);
        //Проверить значение ключа "success"
        assertEquals(booleanExpected, actualBoolean);
        //Проверить значение ключа "message"
        assertEquals(errorMessage, actualMessage);
    }
}

