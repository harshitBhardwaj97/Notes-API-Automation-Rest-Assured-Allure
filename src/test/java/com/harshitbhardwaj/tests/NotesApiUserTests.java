package com.harshitbhardwaj.tests;

import com.github.javafaker.Faker;
import com.harshitbhardwaj.models.UserResponseAndInfo;
import com.harshitbhardwaj.responses.user.UserResponse;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.Objects;

import static com.harshitbhardwaj.constants.Constants.Common.*;
import static com.harshitbhardwaj.constants.Constants.Errors.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

public class NotesApiUserTests {

    private static final Logger logger = LoggerFactory.getLogger(NotesApiUserTests.class);
    private final static Faker faker = new Faker();

    private Map<String, Object> validUserPayload;
    private Map<String, Object> userWithInvalidEmailPayload;
    private Map<String, Object> userWithInvalidPasswordPayload;
    private Map<String, Object> userInfoAfterSuccessfulLogin;

    @BeforeTest
    public void beforeNotesApiUserTestsBlock() {
        logger.info(" ============================================== ");
        logger.info(" ### Starting to run NotesApiUserTests ### ");
        logger.info(" ============================================== ");
    }

    @Test
    @Step
    public void registeringValidUser_returnsSuccessStatus() {
        logger.info(" ### Checking registeringValidUser_returnsSuccessStatus ### ");
        UserResponseAndInfo userRegistrationResponse = UserResponse.registerValidUser();
        Response response = userRegistrationResponse.getResponse();
        response.then().log().body();

        validUserPayload = userRegistrationResponse.getPayloadOrInfo();
        Assert.assertEquals(response.statusCode(), 201);

        response.then().body("success", equalTo(true));
        response.then().body("status", equalTo(201));
        response.then().body("message", equalTo(VALID_USER_REGISTER_MESSAGE));
        response.then().assertThat().body(matchesJsonSchemaInClasspath("register-valid-user.json"));

        System.out.println("Valid User Payload Details -> " + validUserPayload);
        logger.info(" ============================================== ");

    }

    @Test
    @Step
    public void registeringUserWithInvalidEmail_returnsBadRequestStatus() {
        logger.info(" ### Checking registeringUserWithInvalidEmail_returnsBadRequestStatus ### ");
        UserResponseAndInfo userResponseAndInfo = UserResponse.registerUserWithInvalidEmail();
        Response response = userResponseAndInfo.getResponse();
        response.then().log().body();

        userWithInvalidEmailPayload = userResponseAndInfo.getPayloadOrInfo();

        Assert.assertEquals(response.statusCode(), 400);
        response.then().body("success", equalTo(false));
        response.then().body("status", equalTo(400));
        response.then().body("message", equalTo(INVALID_EMAIL_MESSAGE));

        response.then().assertThat().body(matchesJsonSchemaInClasspath("register-user-invalid-email.json"));

        System.out.println("Invalid Email User Payload Details -> " + userWithInvalidEmailPayload);
        logger.info(" ============================================== ");
    }

    @Test
    @Step
    public void registeringUserWithInvalidPassword_returnsBadRequestStatus() {
        logger.info(" ### Checking registeringUserWithInvalidPassword_returnsBadRequestStatus ### ");
        UserResponseAndInfo userResponseAndInfo = UserResponse.registerUserWithInvalidPassword();
        Response response = userResponseAndInfo.getResponse();
        response.then().log().body();

        userWithInvalidPasswordPayload = userResponseAndInfo.getPayloadOrInfo();

        Assert.assertEquals(response.statusCode(), 400);
        response.then().body("success", equalTo(false));
        response.then().body("status", equalTo(400));
        response.then().body("message", equalTo(INVALID_PASSWORD_MESSAGE));

        response.then().assertThat().body(matchesJsonSchemaInClasspath("register-user-invalid-email.json"));

        System.out.println("Invalid Password User Payload Details -> " + userWithInvalidPasswordPayload);
        logger.info(" ============================================== ");
    }

    @Test(dependsOnMethods = "registeringValidUser_returnsSuccessStatus")
    @Step
    public void loggingUserWithValidCredentials_returnsSuccessStatusAndToken() {
        logger.info(" ### Checking loggingUserWithValidCredentials_returnsSuccessStatusAndToken ### ");
        String email = (String) validUserPayload.get("email");
        String password = (String) validUserPayload.get("password");
        String name = (String) validUserPayload.get("name");

        UserResponseAndInfo userResponseAndInfo = UserResponse.loginUserWithCredentials(email, password);
        Response response = userResponseAndInfo.getResponse();

        response.then().log().body();

        Assert.assertEquals(response.statusCode(), 200);
        response.then().body("success", equalTo(true));
        response.then().body("status", equalTo(200));
        response.then().body("message", equalTo(SUCCESSFUL_LOGIN_MESSAGE));
        response.then().body("data.email", equalTo(email));
        response.then().body("data.name", equalTo(name));
        response.then().assertThat().body(matchesJsonSchemaInClasspath("login-valid-user.json"));

        userInfoAfterSuccessfulLogin = userResponseAndInfo.getPayloadOrInfo();

        System.out.println("userInfoAfterSuccessfulLogin -> " + userInfoAfterSuccessfulLogin);
        logger.info(" ============================================== ");
    }

    @Test(dependsOnMethods = "loggingUserWithValidCredentials_returnsSuccessStatusAndToken")
    @Step
    public void getUserProfile_returnsSuccessStatus() {
        logger.info(" ### Checking getUserProfile_returnsSuccessStatus ### ");
        String token = (String) userInfoAfterSuccessfulLogin.get("token");
        String email = (String) validUserPayload.get("email");
        String name = (String) validUserPayload.get("name");

        Response response = UserResponse.getUserProfileWithToken(token);
        response.then().log().body();

        Assert.assertEquals(response.statusCode(), 200);
        response.then().body("success", equalTo(true));
        response.then().body("status", equalTo(200));
        response.then().body("message", equalTo(SUCCESSFUL_USER_PROFILE_MESSAGE));
        response.then().body("data.email", equalTo(email));
        response.then().body("data.name", equalTo(name));
        response.then().assertThat().body(matchesJsonSchemaInClasspath("get-userprofile.json"));
        logger.info(" ============================================== ");
    }

    @Test
    @Step
    public void loggingUserWithInvalidCredentials_returnsBadRequestStatus() {
        logger.info(" ### Checking loggingUserWithValidCredentials_returnsSuccessStatusAndToken ### ");
        String randomEmail = faker.internet().emailAddress();
        String randomPassword = faker.bothify("????####", false);

        UserResponseAndInfo userResponseAndInfo = UserResponse.loginUserWithCredentials(randomEmail, randomPassword);
        Response response = userResponseAndInfo.getResponse();
        response.then().log().body();

        Assert.assertEquals(response.statusCode(), 401);
        response.then().body("success", equalTo(false));
        response.then().body("status", equalTo(401));
        response.then().body("message", equalTo(INVALID_CREDENTIALS_MESSAGE));
        logger.info(" ============================================== ");
    }

    @Test(dependsOnMethods = "loggingUserWithValidCredentials_returnsSuccessStatusAndToken")
    @Step
    public void logoutUserWithValidToken_returnsSuccessStatus() {
        logger.info(" ### Checking logoutUserWithValidToken_returnsSuccessStatus ### ");
        String token = (String) userInfoAfterSuccessfulLogin.get("token");

        Response response = UserResponse.logoutUserWithToken(token);
        response.then().log().body();

        Assert.assertEquals(response.statusCode(), 200);
        response.then().body("success", equalTo(true));
        response.then().body("status", equalTo(200));
        response.then().body("message", equalTo(SUCCESSFUL_LOGOUT_MESSAGE));
        logger.info(" ============================================== ");
    }

    @Test
    @Step
    public void logoutUserWithInvalidToken_returnsBadRequestStatus() {
        logger.info(" ### Checking logoutUserWithInvalidToken_returnsBadRequestStatus ### ");
        String randomToken = faker.bothify("??##??##??");

        Response response = UserResponse.logoutUserWithToken(randomToken);
        response.then().log().body();

        Assert.assertEquals(response.statusCode(), 401);
        response.then().body("success", equalTo(false));
        response.then().body("status", equalTo(401));
        response.then().body("message", equalTo(INVALID_ACCESS_TOKEN_MESSAGE));
        logger.info(" ============================================== ");
    }

    @Test
    @Step
    public void deleteUserWithInvalidToken_returnsBadRequestStatus() {
        logger.info(" ### Checking deleteUserWithInvalidToken_returnsBadRequestStatus ### ");
        String randomToken = faker.bothify("??##??##??");

        Response response = UserResponse.deleteUserWithToken(randomToken);
        response.then().log().body();

        Assert.assertEquals(response.statusCode(), 401);
        response.then().body("success", equalTo(false));
        response.then().body("status", equalTo(401));
        response.then().body("message", equalTo(INVALID_ACCESS_TOKEN_MESSAGE));
        logger.info(" ============================================== ");
    }

    @AfterTest
    public void deleteUser() {
        logger.info(" ============================================== ");
        logger.info(" ### NotesApiUserTests Executed ### ");
        logger.info(" ============================================== ");
        // User has been logged in successfully once
        if (!Objects.isNull(validUserPayload)) {
            String email = (String) validUserPayload.get("email");
            String password = (String) validUserPayload.get("password");

            UserResponseAndInfo userResponseAndInfo = UserResponse.loginUserWithCredentials(email, password);
            Response loginResponse = userResponseAndInfo.getResponse();

            loginResponse.then().log().body();

            userInfoAfterSuccessfulLogin = userResponseAndInfo.getPayloadOrInfo();

            System.out.println("userInfoAfterSuccessfulLogin -> " + userInfoAfterSuccessfulLogin);

            String token = (String) userInfoAfterSuccessfulLogin.get("token");

            Response deleteUserResponse = UserResponse.deleteUserWithToken(token);
            deleteUserResponse.then().log().body();

            Assert.assertEquals(deleteUserResponse.statusCode(), 200);
            deleteUserResponse.then().body("success", equalTo(true));
            deleteUserResponse.then().body("status", equalTo(200));
            deleteUserResponse.then().body("message", equalTo(SUCCESSFUL_ACCOUNT_DELETE_MESSAGE));
            logger.info(" ============================================== ");

        } else {
            System.out.println("No user has been logged in, hence no user deletion is possible !");
            logger.info(" ============================================== ");
        }

    }

}
