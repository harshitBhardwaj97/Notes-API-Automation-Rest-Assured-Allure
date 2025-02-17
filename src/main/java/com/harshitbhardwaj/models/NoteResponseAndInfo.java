package com.harshitbhardwaj.models;

import io.restassured.response.Response;

import java.util.Map;

public class NoteResponseAndInfo {

    private final Response response;
    private final Map<String, Object> notePayloadOrInfo;

    public NoteResponseAndInfo(Response response, Map<String, Object> notePayloadOrInfo) {
        this.response = response;
        this.notePayloadOrInfo = notePayloadOrInfo;
    }

    public Response getResponse() {
        return response;
    }

    public Map<String, Object> getNotePayloadOrInfo() {
        return notePayloadOrInfo;
    }

}
