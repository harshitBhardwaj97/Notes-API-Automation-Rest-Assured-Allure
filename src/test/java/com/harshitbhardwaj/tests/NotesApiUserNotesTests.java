package com.harshitbhardwaj.tests;

import com.github.javafaker.Faker;
import com.harshitbhardwaj.models.NoteResponseAndInfo;
import com.harshitbhardwaj.models.UserResponseAndInfo;
import com.harshitbhardwaj.responses.notes.NotesResponse;
import com.harshitbhardwaj.responses.user.UserResponse;
import com.harshitbhardwaj.utils.Utils;
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

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

public class NotesApiUserNotesTests {

    private static final Logger logger = LoggerFactory.getLogger(NotesApiUserNotesTests.class);
    private final static Faker faker = new Faker();
    private final String NO_NOTES_FOUND_MESSAGE = "No notes found";
    private final String SUCCESSFUL_ACCOUNT_DELETE_MESSAGE = "Account successfully deleted";
    private final String SUCCESSFUL_NOTE_CREATED_MESSAGE = "Note successfully created";
    private final String NOTE_NOT_FOUND_VALID_ID_MESSAGE = "No note was found with the provided ID, Maybe it was deleted";
    private final String INVALID_NOTE_ID_MESSAGE = "Note ID must be a valid ID";
    private final String SUCCESSFUL_NOTE_FOUND_MESSAGE = "Note successfully retrieved";
    private final String SUCCESSFUL_NOTE_PATCH_OR_PUT_MESSAGE = "Note successfully Updated";
    private final String INVALID_NOTE_PATCH_MESSAGE = "Note completed status must be boolean";
    private final String SUCCESSFUL_NOTE_DELETE_MESSAGE = "Note successfully deleted";
    private Map<String, Object> validUserPayload;
    private Map<String, Object> postedNotePayloadAndInfo;
    private Map<String, Object> patchedNotePayloadAndInfo;
    private Map<String, Object> updatedNotePayloadAndInfo;
    private Map<String, Object> userInfoAfterSuccessfulLogin;

    @BeforeTest
    public void beforeNotesApiUserNotesTestsBlock() {
        logger.info(" ============================================== ");
        logger.info(" ### Starting to run NotesApiUserNotesTests ### ");
        logger.info(" ============================================== ");

        /*
         * Before running these tests, register the user and login successfully to get
         * the authorization token
         */
        UserResponseAndInfo userRegistrationResponse = UserResponse.registerValidUser();
        validUserPayload = userRegistrationResponse.getPayloadOrInfo();

        /*
         * User registered, now login with this registered user
         */

        String email = (String) validUserPayload.get("email");
        String password = (String) validUserPayload.get("password");

        UserResponseAndInfo userResponseAndInfo = UserResponse.loginUserWithCredentials(email, password);
        userInfoAfterSuccessfulLogin = userResponseAndInfo.getPayloadOrInfo();
        String token = (String) userInfoAfterSuccessfulLogin.get("token");

        Response response = UserResponse.getUserProfileWithToken(token);
        response.then().log().body();
        Assert.assertEquals(response.statusCode(), 200);

    }

    @Test(priority = 1)
    @Step
    public void getAllNotesInitially_returnsNoNotesFound() {
        logger.info(" ### Checking getAllNotesInitially_returnsNoNotesFound ### ");
        String token = (String) userInfoAfterSuccessfulLogin.get("token");

        Response response = NotesResponse.getAllNotesWithToken(token);
        response.then().log().body();

        Assert.assertEquals(response.statusCode(), 200);
        response.then().body("success", equalTo(true));
        response.then().body("status", equalTo(200));
        response.then().body("message", equalTo(NO_NOTES_FOUND_MESSAGE));
        response.then().body("data.size()", equalTo(0));
        response.then().assertThat().body(matchesJsonSchemaInClasspath("no-notes-found.json"));

        System.out.println("Valid User Payload Details -> " + validUserPayload);
        logger.info(" ============================================== ");

    }

    @Test(priority = 2)
    @Step
    public void postValidNote_returnsSuccessStatus() {
        logger.info(" ### Checking postValidNote_returnsSuccessStatus ### ");

        String token = (String) userInfoAfterSuccessfulLogin.get("token");
        String userId = (String) userInfoAfterSuccessfulLogin.get("user_id");

        NoteResponseAndInfo noteResponseAndInfo = NotesResponse.postNoteWithToken(token);
        postedNotePayloadAndInfo = noteResponseAndInfo.getNotePayloadOrInfo();
        Response response = noteResponseAndInfo.getResponse();

        String postedNoteTitle = (String) postedNotePayloadAndInfo.get("title");
        String postedNoteDescription = (String) postedNotePayloadAndInfo.get("description");
        String postedNoteCategory = (String) postedNotePayloadAndInfo.get("category");
        String postedNoteId = (String) postedNotePayloadAndInfo.get("note_id");

        response.then().log().body();
        response.then().body("success", equalTo(true));
        response.then().body("status", equalTo(200));
        response.then().body("message", equalTo(SUCCESSFUL_NOTE_CREATED_MESSAGE));
        response.then().body("data.title", equalTo(postedNoteTitle));
        response.then().body("data.category", equalTo(postedNoteCategory));
        response.then().body("data.description", equalTo(postedNoteDescription));
        response.then().body("data.id", equalTo(postedNoteId));
        response.then().body("data.user_id", equalTo(userId));
        System.out.println("postedNoteDetails -> " + postedNotePayloadAndInfo);
        logger.info(" ============================================== ");
    }

    @Test(dependsOnMethods = {"postValidNote_returnsSuccessStatus"})
    @Step
    public void getNoteWithValidId_returnsSuccessStatusAndNote() {
        logger.info(" ### Checking getNoteWithValidId_returnsSuccessStatusAndNote ### ");

        String token = (String) userInfoAfterSuccessfulLogin.get("token");
        String userId = (String) userInfoAfterSuccessfulLogin.get("user_id");
        String postedNoteId = (String) postedNotePayloadAndInfo.get("note_id");
        String postedNoteTitle = (String) postedNotePayloadAndInfo.get("title");
        String postedNoteDescription = (String) postedNotePayloadAndInfo.get("description");
        String postedNoteCategory = (String) postedNotePayloadAndInfo.get("category");
        Response response = NotesResponse.getNoteWithTokenAndById(token, postedNoteId);

        response.then().log().body();
        response.then().body("success", equalTo(true));
        response.then().body("status", equalTo(200));
        response.then().body("message", equalTo(SUCCESSFUL_NOTE_FOUND_MESSAGE));
        response.then().body("data.title", equalTo(postedNoteTitle));
        response.then().body("data.category", equalTo(postedNoteCategory));
        response.then().body("data.description", equalTo(postedNoteDescription));
        response.then().body("data.id", equalTo(postedNoteId));
        response.then().body("data.user_id", equalTo(userId));
        logger.info(" ============================================== ");
    }

    @Test
    @Step
    public void getNoteWithValidNotFoundId_returnsNotFoundStatus() {
        logger.info(" ### Checking getNoteWithValidNotFoundId_returnsNotFoundStatus ### ");

        String token = (String) userInfoAfterSuccessfulLogin.get("token");
        String randomNoteId = Utils.generateRandomHexId(24);
        System.out.println("Random valid not found id => " + randomNoteId);
        Response response = NotesResponse.getNoteWithTokenAndById(token, randomNoteId);

        response.then().log().body();
        response.then().body("success", equalTo(false));
        response.then().body("status", equalTo(404));
        response.then().body("message", equalTo(NOTE_NOT_FOUND_VALID_ID_MESSAGE));
        logger.info(" ============================================== ");
    }

    @Test
    @Step
    public void getNoteWithInvalidId_returnsBadRequestStatus() {
        logger.info(" ### Checking getNoteWithInvalidId_returnsBadRequestStatus ### ");

        String token = (String) userInfoAfterSuccessfulLogin.get("token");
        String invalidNoteId = faker.bothify("#?#?#?#?"); // Invalid Note Id
        Response response = NotesResponse.getNoteWithTokenAndById(token, invalidNoteId);

        response.then().log().body();
        response.then().body("success", equalTo(false));
        response.then().body("status", equalTo(400));
        response.then().body("message", equalTo(INVALID_NOTE_ID_MESSAGE));
        logger.info(" ============================================== ");
    }

    @Test(dependsOnMethods = {"postValidNote_returnsSuccessStatus"})
    @Step
    public void patchNoteWithValidIdAndBody_returnsSuccessStatusAndNote() {
        logger.info(" ### Checking patchNoteWithValidIdAndBody_returnsSuccessStatusAndNote ### ");

        String token = (String) userInfoAfterSuccessfulLogin.get("token");
        String userId = (String) userInfoAfterSuccessfulLogin.get("user_id");
        String postedNoteId = (String) postedNotePayloadAndInfo.get("note_id");

        NoteResponseAndInfo noteResponseAndInfo = NotesResponse.patchNoteWithTokenAndById(token, postedNoteId, true);
        patchedNotePayloadAndInfo = noteResponseAndInfo.getNotePayloadOrInfo();
        Response patchResponse = noteResponseAndInfo.getResponse();

        String patchedNoteId = (String) patchedNotePayloadAndInfo.get("note_id");
        String patchedNoteTitle = (String) patchedNotePayloadAndInfo.get("patched_note_title");
        String patchedNoteDescription = (String) patchedNotePayloadAndInfo.get("patched_note_description");
        String patchedNoteCategory = (String) patchedNotePayloadAndInfo.get("patched_note_category");

        patchResponse.then().log().body();
        patchResponse.then().body("success", equalTo(true));
        patchResponse.then().body("status", equalTo(200));
        patchResponse.then().body("message", equalTo(SUCCESSFUL_NOTE_PATCH_OR_PUT_MESSAGE));
        patchResponse.then().body("data.title", equalTo(patchedNoteTitle));
        patchResponse.then().body("data.category", equalTo(patchedNoteCategory));
        patchResponse.then().body("data.completed", equalTo(true));
        patchResponse.then().body("data.description", equalTo(patchedNoteDescription));
        patchResponse.then().body("data.id", equalTo(patchedNoteId));
        patchResponse.then().body("data.user_id", equalTo(userId));
        logger.info(" ============================================== ");
    }

    @Test(dependsOnMethods = {"postValidNote_returnsSuccessStatus",
            "patchNoteWithValidIdAndBody_returnsSuccessStatusAndNote"})
    @Step
    public void putNoteWithValidIdAndBody_returnsSuccessStatusAndNote() {
        logger.info(" ### Checking putNoteWithValidIdAndBody_returnsSuccessStatusAndNote ### ");

        String token = (String) userInfoAfterSuccessfulLogin.get("token");
        String userId = (String) userInfoAfterSuccessfulLogin.get("user_id");
        String noteId = (String) postedNotePayloadAndInfo.get("note_id");

        NoteResponseAndInfo noteResponseAndInfo = NotesResponse.putNoteWithTokenAndById(token, noteId);
        updatedNotePayloadAndInfo = noteResponseAndInfo.getNotePayloadOrInfo();
        Response putResponse = noteResponseAndInfo.getResponse();

        String updatedNoteId = (String) updatedNotePayloadAndInfo.get("note_id");
        String updatedNoteTitle = (String) updatedNotePayloadAndInfo.get("updated_note_title");
        String updatedNoteDescription = (String) updatedNotePayloadAndInfo.get("updated_note_description");
        String updatedNoteCategory = (String) updatedNotePayloadAndInfo.get("updated_note_category");

        putResponse.then().log().body();
        putResponse.then().body("success", equalTo(true));
        putResponse.then().body("status", equalTo(200));
        putResponse.then().body("message", equalTo(SUCCESSFUL_NOTE_PATCH_OR_PUT_MESSAGE));
        putResponse.then().body("data.title", equalTo(updatedNoteTitle));
        putResponse.then().body("data.category", equalTo(updatedNoteCategory));
        putResponse.then().body("data.description", equalTo(updatedNoteDescription));
        putResponse.then().body("data.id", equalTo(updatedNoteId));
        putResponse.then().body("data.user_id", equalTo(userId));
        logger.info(" ============================================== ");
    }

    @Test(dependsOnMethods = {"postValidNote_returnsSuccessStatus"})
    @Step
    public void patchNoteWithinvalidBody_returnsBadRequestStatus() {
        logger.info(" ### Checking patchNoteWithinvalidBody_returnsBadRequestStatus ### ");

        String token = (String) userInfoAfterSuccessfulLogin.get("token");
        String postedNoteId = (String) postedNotePayloadAndInfo.get("note_id");
        Response patchResponse = NotesResponse.patchInvalidNoteWithTokenAndById(token, postedNoteId);

        patchResponse.then().log().body();
        patchResponse.then().body("success", equalTo(false));
        patchResponse.then().body("status", equalTo(400));
        patchResponse.then().body("message", equalTo(INVALID_NOTE_PATCH_MESSAGE));
        logger.info(" ============================================== ");
    }

    @Test(dependsOnMethods = {"postValidNote_returnsSuccessStatus",
            "patchNoteWithValidIdAndBody_returnsSuccessStatusAndNote",
            "putNoteWithValidIdAndBody_returnsSuccessStatusAndNote"})
    @Step
    public void deleteValidNote_returnsSuccessStatus() {
        logger.info(" ### Checking deleteValidNote_returnsSuccessStatus ### ");

        String token = (String) userInfoAfterSuccessfulLogin.get("token");
        String postedNoteId = (String) postedNotePayloadAndInfo.get("note_id");
        Response deleteResponse = NotesResponse.deleteNoteWithTokenAndById(token, postedNoteId);

        deleteResponse.then().log().body();
        deleteResponse.then().body("success", equalTo(true));
        deleteResponse.then().body("status", equalTo(200));
        deleteResponse.then().body("message", equalTo(SUCCESSFUL_NOTE_DELETE_MESSAGE));
        logger.info(" ============================================== ");
    }

    @AfterTest
    public void deleteUser() {
        logger.info(" ============================================== ");
        logger.info(" ### NotesApiUserNotesTests Executed ### ");
        logger.info(" ============================================== ");
        // User has been logged in successfully once
        if (!Objects.isNull(validUserPayload)) {
            String email = (String) validUserPayload.get("email");
            String password = (String) validUserPayload.get("password");

            UserResponseAndInfo userResponseAndInfo = UserResponse.loginUserWithCredentials(email, password);
            Response loginResponse = userResponseAndInfo.getResponse();
            loginResponse.then().log().body();

            userInfoAfterSuccessfulLogin = userResponseAndInfo.getPayloadOrInfo();

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
