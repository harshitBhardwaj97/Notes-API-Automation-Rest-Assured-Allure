package com.harshitbhardwaj.responses.notes;

import com.github.javafaker.Faker;
import com.harshitbhardwaj.constants.Constants;
import com.harshitbhardwaj.models.NoteResponseAndInfo;
import com.harshitbhardwaj.utils.Utils;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class NotesResponse {

    private final static Faker faker = new Faker();

    public static Response getAllNotesWithToken(String token) {
        return RestAssured.given().filter(new AllureRestAssured()).
                header("x-auth-token", token).when().get(Constants.Common.getAllNotesOrPostNote);
    }

    public static Response getNoteWithTokenAndById(String token, String noteId) {
        return RestAssured.given().filter(new AllureRestAssured())
                .header("x-auth-token", token).pathParam("id", noteId).when()
                .get(Constants.Common.createReadUpdateOrDeleteNoteById);
    }

    public static NoteResponseAndInfo postNoteWithToken(String token) {
        Map<String, Object> notePayloadAndInfo = new HashMap<>();

        String noteTitle = faker.lorem().characters(5);
        String noteDescription = faker.lorem().characters(15);
        String noteType = Utils.getRandomNoteCategory();

        notePayloadAndInfo.put("title", noteTitle);
        notePayloadAndInfo.put("description", noteDescription);
        notePayloadAndInfo.put("category", noteType);

        Response response = RestAssured.given().filter(new AllureRestAssured()).
                header("x-auth-token", token).contentType(ContentType.JSON).body(notePayloadAndInfo)
                .when().post(Constants.Common.getAllNotesOrPostNote).then().extract().response();

        String postedNoteId = response.path("data.id");
        String userId = response.path("data.user_id");
        notePayloadAndInfo.put("note_id", postedNoteId);
        notePayloadAndInfo.put("user_id", userId);
        System.out.println("Posted Note Info (Notes Response) => " + notePayloadAndInfo);

        return new NoteResponseAndInfo(response, notePayloadAndInfo);
    }

    public static NoteResponseAndInfo patchNoteWithTokenAndById(String token, String noteId, boolean completedStatus) {
        Map<String, Object> notePatchInfo = new HashMap<>();
        notePatchInfo.put("completed", completedStatus);

        Response response = RestAssured.given().filter(new AllureRestAssured()).
                header("x-auth-token", token).pathParam("id", noteId).contentType(ContentType.JSON)
                .body(notePatchInfo).when().patch(Constants.Common.createReadUpdateOrDeleteNoteById).then().extract()
                .response();

        String patchedNoteId = response.path("data.id");
        String userId = response.path("data.user_id");
        String patchedNoteTitle = response.path("data.title");
        String patchedNoteCategory = response.path("data.category");
        String patchedNoteDescription = response.path("data.description");
        notePatchInfo.put("note_id", patchedNoteId);
        notePatchInfo.put("patched_note_title", patchedNoteTitle);
        notePatchInfo.put("patched_note_description", patchedNoteDescription);
        notePatchInfo.put("patched_note_category", patchedNoteCategory);
        notePatchInfo.put("user_id", userId);
        System.out.println("Patched Note Info (Notes Response) => " + notePatchInfo);
        return new NoteResponseAndInfo(response, notePatchInfo);
    }

    public static NoteResponseAndInfo putNoteWithTokenAndById(String token, String noteId) {
        Map<String, Object> updatedNotePayloadInfo = new HashMap<>();
        String updatedNoteTitleParam = "Updated_" + faker.lorem().characters(5);
        String updatedNoteDescriptionParam = "Updated_" + faker.lorem().characters(15);
        String updatedNoteTypeParam = Utils.getRandomNoteCategory();

        updatedNotePayloadInfo.put("title", updatedNoteTitleParam);
        updatedNotePayloadInfo.put("description", updatedNoteDescriptionParam);
        updatedNotePayloadInfo.put("category", updatedNoteTypeParam);
        updatedNotePayloadInfo.put("completed", Utils.getRandomNoteCompletion());

        Response response = RestAssured.given().filter(new AllureRestAssured()).
                header("x-auth-token", token).pathParam("id", noteId).contentType(ContentType.JSON)
                .body(updatedNotePayloadInfo).when().put(Constants.Common.createReadUpdateOrDeleteNoteById).then()
                .extract().response();

        String updatedNoteId = response.path("data.id");
        String userId = response.path("data.user_id");
        String updatedNoteTitle = response.path("data.title");
        String updatedNoteCategory = response.path("data.category");
        String updatedNoteDescription = response.path("data.description");
        updatedNotePayloadInfo.put("note_id", updatedNoteId);
        updatedNotePayloadInfo.put("updated_note_title", updatedNoteTitle);
        updatedNotePayloadInfo.put("updated_note_description", updatedNoteDescription);
        updatedNotePayloadInfo.put("updated_note_category", updatedNoteCategory);
        updatedNotePayloadInfo.put("user_id", userId);
        System.out.println("Updated Note Info (Notes Response) => " + updatedNotePayloadInfo);
        return new NoteResponseAndInfo(response, updatedNotePayloadInfo);
    }

    public static Response patchInvalidNoteWithTokenAndById(String token, String noteId) {
        Map<String, Object> notePatchInfo = new HashMap<>();
        String randomBody = faker.bothify("#?#?#?#?"); // Invalid Completed Parameter, it should be either true of false
        notePatchInfo.put("completed", randomBody);

        return RestAssured.given().filter(new AllureRestAssured()).
                header("x-auth-token", token).pathParam("id", noteId).contentType(ContentType.JSON)
                .body(notePatchInfo).when().patch(Constants.Common.createReadUpdateOrDeleteNoteById);
    }

    public static Response deleteNoteWithTokenAndById(String token, String noteId) {
        return RestAssured.given().filter(new AllureRestAssured()).
                header("x-auth-token", token).pathParam("id",
                        noteId).when()
                .delete(Constants.Common.createReadUpdateOrDeleteNoteById);
    }

    public static Response getAllNotesWithoutAuthorization() {
        return RestAssured.given().filter(new AllureRestAssured()).when().get(Constants.Common.getAllNotesOrPostNote);
    }

    public static NoteResponseAndInfo postNoteWithoutAuthorization() {
        Map<String, Object> notePayload = new HashMap<>();

        String noteTitle = faker.lorem().sentence();
        String noteDescription = faker.lorem().paragraph();
        String noteType = Utils.getRandomNoteCategory();

        notePayload.put("title", noteTitle);
        notePayload.put("description", noteDescription);
        notePayload.put("category", noteType);

        Response response = RestAssured.given().filter(new AllureRestAssured()).
                contentType(ContentType.JSON).when().post(Constants.Common.getAllNotesOrPostNote);
        return new NoteResponseAndInfo(response, notePayload);
    }

    public static Response postNoteWithoutAuthorizationAndPayload() {
        return RestAssured.given().filter(new AllureRestAssured()).
                contentType(ContentType.JSON).when().post(Constants.Common.getAllNotesOrPostNote);
    }

    public static Response getNoteWithoutAuthorization() {
        String randomId = Utils.generateRandomHexId(24);
        return RestAssured.given().filter(new AllureRestAssured()).pathParam("id", randomId).when()
                .get(Constants.Common.createReadUpdateOrDeleteNoteById);
    }

    public static Response putNoteWithoutAuthorization() {
        String randomId = Utils.generateRandomHexId(24);
        Map<String, Object> updatedNotePayload = new HashMap<>();

        String updatedNoteTitle = faker.lorem().sentence();
        String updatedNoteDescription = faker.lorem().paragraph();
        String updatedNoteType = Utils.getRandomNoteCategory();
        boolean updatedNoteCompletionStatus = Utils.getRandomNoteCompletion();

        updatedNotePayload.put("title", updatedNoteTitle);
        updatedNotePayload.put("description", updatedNoteDescription);
        updatedNotePayload.put("completed", updatedNoteCompletionStatus);
        updatedNotePayload.put("category", updatedNoteType);

        System.out.println("updatedNote -> " + updatedNotePayload);

        return RestAssured.given().filter(new AllureRestAssured()).
                pathParam("id", randomId).contentType(ContentType.JSON).when()
                .put(Constants.Common.createReadUpdateOrDeleteNoteById);
    }

    public static Response patchNoteWithoutAuthorization() {
        String randomId = Utils.generateRandomHexId(24);
        Map<String, Object> patchedNotePayload = new HashMap<>();

        boolean patchedNoteCompletionStatus = Utils.getRandomNoteCompletion();
        patchedNotePayload.put("completed", patchedNoteCompletionStatus);

        System.out.println("patchedNote -> " + patchedNotePayload);

        return RestAssured.given().filter(new AllureRestAssured()).
                pathParam("id", randomId).contentType(ContentType.JSON).when()
                .patch(Constants.Common.createReadUpdateOrDeleteNoteById);
    }

    public static Response deleteNoteWithoutAuthorization() {
        String randomId = Utils.generateRandomHexId(24);
        return RestAssured.given().filter(new AllureRestAssured()).pathParam("id", randomId).when()
                .delete(Constants.Common.createReadUpdateOrDeleteNoteById);
    }

}
