package com.harshitbhardwaj.tests;

import com.harshitbhardwaj.responses.common.CommonResponse;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static com.harshitbhardwaj.constants.Constants.Common.SUCCESS_HEALTH_MESSAGE;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

public class NotesApiCommonTests {

    private static final Logger logger = LoggerFactory.getLogger(NotesApiCommonTests.class);

    @BeforeTest
    public void beforeNotesApiCommonTestsBlock() {
        logger.info(" ============================================== ");
        logger.info(" ### Starting to run NotesApiCommonTests ### ");
        logger.info(" ============================================== ");
    }

    @Test
    @Step
    public void healthOfNotesApiService_returnsSuccessStatus() {
        logger.info(" ### Checking healthOfNotesApiService_returnsSuccessStatus ### ");
        Response response = CommonResponse.checkHealthOfNotesApi();
        response.then().log().body();
        Assert.assertEquals(response.statusCode(), 200);
        response.then().body("success", equalTo(true));
        response.then().body("status", equalTo(200));
        response.then().body("message", equalTo(SUCCESS_HEALTH_MESSAGE));
        response.then().assertThat().body(matchesJsonSchemaInClasspath("notes-health-check.json"));
        logger.info(" ============================================== ");
    }

    @AfterTest
    public void afterNotesApiCommonTestsBlock() {
        logger.info(" ============================================== ");
        logger.info(" ### NotesApiCommonTests Executed ### ");
        logger.info(" ============================================== ");
    }
}
