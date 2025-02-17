package com.harshitbhardwaj.responses.user;

import com.github.javafaker.Faker;
import com.harshitbhardwaj.constants.Constants;
import com.harshitbhardwaj.models.UserResponseAndInfo;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class UserResponse {

    private final static Faker faker = new Faker();

    public static UserResponseAndInfo registerValidUser() {
        Map<String, Object> validUserPayload = new HashMap<>();

        String validUserName = faker.name().fullName();
        String validEmail = faker.internet().emailAddress();
        String password = faker.bothify("????####", false);

        validUserPayload.put("name", validUserName);
        validUserPayload.put("email", validEmail);
        validUserPayload.put("password", password);

        Response response = RestAssured.given().filter(new AllureRestAssured()).
                contentType(ContentType.JSON).body(validUserPayload).when()
                .post(Constants.Common.registerUser);
        return new UserResponseAndInfo(response, validUserPayload);
    }

    public static UserResponseAndInfo registerUserWithInvalidEmail() {
        Map<String, Object> userPayloadWithInvalidEmail = new HashMap<>();

        String validUserName = faker.name().fullName();
        String invalidEmail = faker.bothify("?#?#?#?#", false); // Invalid Email
        String password = faker.bothify("????####", false);

        userPayloadWithInvalidEmail.put("name", validUserName);
        userPayloadWithInvalidEmail.put("email", invalidEmail);
        userPayloadWithInvalidEmail.put("password", password);

        Response response = RestAssured.given().filter(new AllureRestAssured()).
                contentType(ContentType.JSON).body(userPayloadWithInvalidEmail).when()
                .post(Constants.Common.registerUser);
        return new UserResponseAndInfo(response, userPayloadWithInvalidEmail);
    }

    public static UserResponseAndInfo registerUserWithInvalidPassword() {
        Map<String, Object> userPayloadWithInvalidPassword = new HashMap<>();

        String validUserName = faker.name().fullName();
        String validEmail = faker.internet().emailAddress();
        String invalidPassword = faker.bothify("?#?", false); // Invalid Password

        userPayloadWithInvalidPassword.put("name", validUserName);
        userPayloadWithInvalidPassword.put("email", validEmail);
        userPayloadWithInvalidPassword.put("password", invalidPassword);

        Response response = RestAssured.given().filter(new AllureRestAssured()).
                contentType(ContentType.JSON).body(userPayloadWithInvalidPassword).when()
                .post(Constants.Common.registerUser);
        return new UserResponseAndInfo(response, userPayloadWithInvalidPassword);
    }

    public static UserResponseAndInfo loginUserWithCredentials(String email, String password) {
        Map<String, Object> userPayloadOrInfo = new HashMap<>();

        userPayloadOrInfo.put("email", email);
        userPayloadOrInfo.put("password", password);

        Response response = RestAssured.given().filter(new AllureRestAssured()).
                contentType(ContentType.JSON).body(userPayloadOrInfo).when()
                .post(Constants.Common.loginUser).then().extract().response();

        String token = response.path("data.token");
        String userId = response.path("data.id");
        userPayloadOrInfo.put("token", token);
        userPayloadOrInfo.put("user_id", userId);
        return new UserResponseAndInfo(response, userPayloadOrInfo);
    }

    public static Response logoutUserWithToken(String token) {
        return RestAssured.given().filter(new AllureRestAssured()).
                header("x-auth-token", token).when().delete(Constants.Common.logoutUser);
    }

    public static Response getUserProfileWithToken(String token) {
        return RestAssured.given().filter(new AllureRestAssured()).
                header("x-auth-token", token).when().get(Constants.Common.userProfile);
    }

    public static Response deleteUserWithToken(String token) {
        return RestAssured.given().filter(new AllureRestAssured()).
                header("x-auth-token", token).when().delete(Constants.Common.deleteUser);
    }

    public static Response getUserProfileWithoutAuthorization() {
        return RestAssured.when().get(Constants.Common.userProfile);
    }

    public static Response logoutUserWithoutAuthorization() {
        return RestAssured.when().delete(Constants.Common.logoutUser);
    }

    public static Response deleteUserAccountWithoutAuthorization() {
        return RestAssured.when().delete(Constants.Common.deleteUser);
    }

    public static Response changeUserPasswordWithoutAuthorization() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("currentPassword", "John");
        payload.put("newPassword", "Doe");

        return RestAssured.given().filter(new AllureRestAssured()).contentType(ContentType.JSON).body(payload).when()
                .post(Constants.Common.changeUserPassword);
    }

    public static Response changeUserPasswordWithoutAuthorizationAndPayload() {
        return RestAssured.given().filter(new AllureRestAssured()).
                contentType(ContentType.JSON).when().post(Constants.Common.changeUserPassword);
    }

}
