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
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertTrue;

public class GetOrderTest {
    List<String> ingredients = new ArrayList<>();
    private UserClient userClient;
    private IngredientsClient ingredientsClient;
    private OrderClient orderClient;
    private String accessToken;

    @Before
    public void setUp() {
        User user = User.getRandom();
        userClient = new UserClient();
        ingredientsClient = new IngredientsClient();
        orderClient = new OrderClient();
        ValidatableResponse response = userClient.createUser(user);
        accessToken = response.extract().path("accessToken");
        ingredients = ingredientsClient.getIngredients().extract().path("data._id");
        Ingredients orderIngredients = new Ingredients(ingredients.get(0));
        orderClient.create(orderIngredients, accessToken);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Получение заказов конкретного пользователя")
    @Description("Список заказов пользователя с авторизацией")

    public void getOrderListAuthTest() {
        ValidatableResponse responseList = orderClient.get(accessToken);
        int statusCode = responseList.extract().statusCode();
        List<Map<String, String>> orders = responseList.extract().path("orders");
        boolean isMessageListOrders = responseList.extract().path("success");
        assertThat(statusCode, equalTo(200));
        assertThat(orders, hasSize(1));
        assertTrue(isMessageListOrders);
    }

    @Test
    @DisplayName("Получение заказов конкретного пользователя")
    @Description("Список заказов без авторизации, успешный запрос возвращает You should be authorised")
    public void getOrderListWithoutAuthTest() {
        ValidatableResponse responseList = orderClient.getUserOrdersNonAuth();
        int statusCode = responseList.extract().statusCode();
        String errorMessage = responseList.extract().path("message");
        assertThat(statusCode, equalTo(401));
        assertThat(errorMessage, equalTo("You should be authorised"));
    }
}
