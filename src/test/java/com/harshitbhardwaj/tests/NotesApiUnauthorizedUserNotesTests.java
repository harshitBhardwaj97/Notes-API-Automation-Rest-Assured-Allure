package com.harshitbhardwaj.tests;

import com.harshitbhardwaj.models.NoteResponseAndInfo;
import com.harshitbhardwaj.responses.notes.NotesResponse;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Map;

import static com.harshitbhardwaj.constants.Constants.Errors.AUTH_MISSING_MESSAGE;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

public class NotesApiUnauthorizedUserNotesTests {

    private static final Logger logger = LoggerFactory.getLogger(NotesApiUnauthorizedUserNotesTests.class);
    private Map<String, Object> withoutAuthNotePayload;

    @BeforeTest
    public void beforeNotesApiUnauthorizedUserNotesBlock() {
        logger.info(" ============================================== ");
        logger.info(" ### Starting to run NotesApiUnauthorizedUserNotesTests ### ");
        logger.info(" ============================================== ");
    }

    @Test
    @Step
    public void getNote_withoutAuthorization_returnsUnauthorizedRequestStatus() {
        logger.info(" ### Checking getNote_withoutAuthorization ### ");
        Response response = NotesResponse.getNoteWithoutAuthorization();
        response.then().log().body();
        Assert.assertEquals(response.statusCode(), 401);
        response.then().body("success", equalTo(false));
        response.then().body("status", equalTo(401));
        response.then().body("message", equalTo(AUTH_MISSING_MESSAGE));
        response.then().assertThat().body(matchesJsonSchemaInClasspath("userprofile-without-auth.json"));
        logger.info(" ============================================== ");
    }

    @Test
    @Step
    public void getAllNotes_withoutAuthorization_returnsUnauthorizedRequestStatus() {
        logger.info(" ### Checking getAllNotes_withoutAuthorization ### ");
        Response response = NotesResponse.getAllNotesWithoutAuthorization();
        response.then().log().body();
        Assert.assertEquals(response.statusCode(), 401);
        response.then().body("success", equalTo(false));
        response.then().body("status", equalTo(401));
        response.then().body("message", equalTo(AUTH_MISSING_MESSAGE));
        response.then().assertThat().body(matchesJsonSchemaInClasspath("userprofile-without-auth.json"));
        logger.info(" ============================================== ");
    }

    @Test
    @Step
    public void postNote_withoutAuthorizationAndPayload_returnsUnauthorizedRequestStatus() {
        logger.info(" ### Checking postNote_withoutAuthorizationAndPayload ### ");
        Response response = NotesResponse.postNoteWithoutAuthorizationAndPayload();
        response.then().log().body();
        Assert.assertEquals(response.statusCode(), 401);
        response.then().body("success", equalTo(false));
        response.then().body("status", equalTo(401));
        response.then().body("message", equalTo(AUTH_MISSING_MESSAGE));
        response.then().assertThat().body(matchesJsonSchemaInClasspath("userprofile-without-auth.json"));
        logger.info(" ============================================== ");
    }

    @Test
    @Step
    public void postNote_withoutAuthorization_returnsUnauthorizedRequestStatus() {
        logger.info(" ### Checking postNote_withoutAuthorization ### ");
        NoteResponseAndInfo noteResponseAndInfo = NotesResponse.postNoteWithoutAuthorization();
        Response response = noteResponseAndInfo.getResponse();
        response.then().log().body();

        withoutAuthNotePayload = noteResponseAndInfo.getNotePayloadOrInfo();

        Assert.assertEquals(response.statusCode(), 401);
        response.then().body("success", equalTo(false));
        response.then().body("status", equalTo(401));
        response.then().body("message", equalTo(AUTH_MISSING_MESSAGE));
        response.then().assertThat().body(matchesJsonSchemaInClasspath("userprofile-without-auth.json"));

        System.out.println("Without User Auth Note Payload Details -> " + withoutAuthNotePayload);
        logger.info(" ============================================== ");
    }

    @Test
    @Step
    public void putNote_withoutAuthorization_returnsUnauthorizedRequestStatus() {
        logger.info(" ### Checking putNote_withoutAuthorization ### ");
        Response response = NotesResponse.putNoteWithoutAuthorization();
        response.then().log().body();
        Assert.assertEquals(response.statusCode(), 401);
        response.then().body("success", equalTo(false));
        response.then().body("status", equalTo(401));
        response.then().body("message", equalTo(AUTH_MISSING_MESSAGE));
        response.then().assertThat().body(matchesJsonSchemaInClasspath("userprofile-without-auth.json"));
        logger.info(" ============================================== ");
    }

    @Test
    @Step
    public void patchNote_withoutAuthorization_returnsUnauthorizedRequestStatus() {
        logger.info(" ### Checking patchNote_withoutAuthorization ### ");
        Response response = NotesResponse.patchNoteWithoutAuthorization();
        response.then().log().body();
        Assert.assertEquals(response.statusCode(), 401);
        response.then().body("success", equalTo(false));
        response.then().body("status", equalTo(401));
        response.then().body("message", equalTo(AUTH_MISSING_MESSAGE));
        response.then().assertThat().body(matchesJsonSchemaInClasspath("userprofile-without-auth.json"));
        logger.info(" ============================================== ");
    }

    @Test
    @Step
    public void deleteNote_withoutAuthorization_returnsUnauthorizedRequestStatus() {
        logger.info(" ### Checking deleteNote_withoutAuthorization ### ");
        Response response = NotesResponse.deleteNoteWithoutAuthorization();
        response.then().log().body();
        Assert.assertEquals(response.statusCode(), 401);
        response.then().body("success", equalTo(false));
        response.then().body("status", equalTo(401));
        response.then().body("message", equalTo(AUTH_MISSING_MESSAGE));
        response.then().assertThat().body(matchesJsonSchemaInClasspath("userprofile-without-auth.json"));
        logger.info(" ============================================== ");
    }

    @AfterTest
    public void afterNotesApiUnauthorizedUserNotesBlock() {
        logger.info(" ============================================== ");
        logger.info(" ### NotesApiUnauthorizedUserNotesTests Executed ### ");
        logger.info(" ============================================== ");
    }

}
