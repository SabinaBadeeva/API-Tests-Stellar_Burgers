package org.example.user;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static org.example.user.Constants.*;


public class UserClient  extends BaseApi  {
        @Step("create user")
        public ValidatableResponse createUser(User user){
            return given()
                    .spec(getSpec())
                    .body(user)
                    .when()
                    .post(CREATE_USER)
                    .then()
                    .log().all();}


        @Step ("login user")
        public  ValidatableResponse login(UserCredentials userCredentials) {
            return given()
                    .spec(getSpec())
                    .body(userCredentials)
                    .when()
                    .post(LOGIN_USER)
                    .then()
                    .log().all();
        }
        @Step ("Change data user")
        public ValidatableResponse changeData(User user, String accessToken) {
            return given()
                    .spec(getSpec())
                    .auth().oauth2(accessToken)
                    .body(user)
                    .when()
                    .patch(CHANGE_USER_DATA)
                    .then();
        }

    @Step ("Change data user without token")
    public ValidatableResponse changeDataWithoutToken(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .patch(CHANGE_USER_DATA)
                .then()
                .log().all();
    }


        @Step ("delete user")
        public ValidatableResponse delete(String idUser) {
            return given()
                    .spec(getSpec())
                    .when()
                    .delete(DELETE_USER + idUser)
                    .then()
                    .log().all();
        }
        public int loginUserGetID(UserCredentials userCredentials) {
            return login(userCredentials).extract().path("id");
        }
}
