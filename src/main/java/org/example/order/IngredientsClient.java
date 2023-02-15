package org.example.order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.example.user.BaseApi;

import static io.restassured.RestAssured.given;
import static org.example.user.Constants.INGREDIENTS;

public class IngredientsClient extends BaseApi {
    @Step("Get ingredients")
    public ValidatableResponse getIngredients() {
        return given()
                .spec(getSpec())
                .and()
                .get(INGREDIENTS)
                .then()
                .log().all();

    }
}
