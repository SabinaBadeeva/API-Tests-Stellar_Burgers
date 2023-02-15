package orderTest;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.order.Ingredients;
import org.example.order.IngredientsClient;
import org.example.order.OrderClient;
import org.example.user.User;
import org.example.user.UserClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class CreateOrderTest {
    private User user;
    private OrderClient orderClient;
    private IngredientsClient ingredientsClient;
    private UserClient userClient;
    private String accessToken;
    List<String> ingredients = new ArrayList<>();


    @Before
    public void setUp() {
        user = User.getRandom();
        userClient = new UserClient();
        orderClient = new OrderClient();
        ingredientsClient = new IngredientsClient();
        ValidatableResponse response = userClient.createUser(user);
        accessToken = response.extract().path("accessToken");
    }

    @After
    public void tearDown() {
        userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Проверка создания заказа с ингредиентами и авторизацией")
    @Description("Успешный запрос - возвращает success: true")
    public void orderCreationWithIngredientsAndAuthorization() {
        //Получить ингредиенты
        ingredients = ingredientsClient.getIngredients().extract().path("data._id");
        Ingredients orderIngredients = new Ingredients(ingredients.get(1));
        // Создать заказ с авторизацией
        ValidatableResponse response = orderClient.create(orderIngredients, accessToken);
        // Получить статус код
        int statusCode = response.extract().statusCode();
        //Получить значение ключа "success"
        boolean isOrderCreationSuccess = response.extract().path("success");
        // Получить номер заказа
        int orderNumber = response.extract().path("order.number");
        //Asserts
        assertThat(statusCode, equalTo(200));
        assertTrue(isOrderCreationSuccess);
        assertThat(orderNumber, notNullValue());
    }

    @Test
    @DisplayName("Проверка создания заказа без авторизации")
    @Description("Можно создать заказ без авторизации, успешный запрос возвращает статус код 200")
    public void orderCreationWithoutAuth() {
        //Получить ингредиенты
        ingredients = ingredientsClient.getIngredients().extract().path("data._id");
        Ingredients orderIngredients = new Ingredients(ingredients.get(0));
        // Создать заказ без авторизации
        ValidatableResponse response = orderClient.createOrderWithoutAuth(orderIngredients);
        int statusCode = response.extract().statusCode();
        boolean isOrderCreationSuccess = response.extract().path("success");
        int orderNumber = response.extract().path("order.number");
        //Asserts
        assertThat(statusCode, equalTo(200));
        assertTrue(isOrderCreationSuccess);
        assertThat(orderNumber, notNullValue());
    }

    @Test
    @DisplayName("Проверка создания заказа без ингредиентов")
    @Description("Заказ без ингредиентов возвращает Ingredient ids must be provided")
    public void orderCreationWithoutIngredients() {
        // Оставить список ингредиентов пустым
        Ingredients orderIngredients = new Ingredients("");
        // Попытаться создать заказ с авторизацией
        ValidatableResponse response = orderClient.create(orderIngredients, accessToken);
        int statusCode = response.extract().statusCode();
        String errorMessage = response.extract().path("message");
        //Asserts
        assertThat(statusCode, equalTo(400));
        assertThat(errorMessage, equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Успешный запрос возвращает 500")
    public void checkCreateOrderWithIncorrectHashIngredient() {
        Ingredients orderIngredients = new Ingredients("some ingredient");
        ValidatableResponse response = orderClient.create(orderIngredients, accessToken);
        int statusCode = response.extract().statusCode();
        assertThat(statusCode, equalTo(500));
    }
}
