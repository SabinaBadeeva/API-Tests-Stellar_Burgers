package org.example.order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.example.user.BaseApi;

import static io.restassured.RestAssured.given;
import static org.example.user.Constants.ORDER_CREATE;

public class OrderClient extends BaseApi {

    @Step("Создать заказ")
    public ValidatableResponse create(Ingredients ingredient, String token) {
        return given()
                .spec(getSpec())
                .body(ingredient)
                .auth().oauth2(token.substring(7))
                .when()
                .post(ORDER_CREATE)
                .then()
                .log().all();
    }

    @Step("Получить заказ")
    public ValidatableResponse get(String token) {
        return given()
                .spec(getSpec())
                .auth().oauth2(token.substring(7))
                .when()
                .get(ORDER_CREATE)
                .then();
    }

    @Step("Create order without auth")
    public ValidatableResponse createOrderWithoutAuth(Ingredients ingredient) {
        return given()
                .spec(getSpec())
                .body(ingredient)
                .when()
                .post(ORDER_CREATE)
                .then();
    }
    @Step("Get user orders")
    public ValidatableResponse getUserOrdersNonAuth() {
        return given()
                .spec(getSpec())
                .when()
                .get(ORDER_CREATE)
                .then();
    }

}

