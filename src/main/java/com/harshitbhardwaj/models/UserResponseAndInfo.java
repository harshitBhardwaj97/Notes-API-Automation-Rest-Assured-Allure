package com.harshitbhardwaj.models;

import io.restassured.response.Response;

import java.util.Map;

public class UserResponseAndInfo {

    private final Response response;
    private final Map<String, Object> payloadOrInfo;

    public UserResponseAndInfo(Response response, Map<String, Object> payloadOrInfo) {
        this.response = response;
        this.payloadOrInfo = payloadOrInfo;
    }

    public Response getResponse() {
        return response;
    }

    public Map<String, Object> getPayloadOrInfo() {
        return payloadOrInfo;
    }

}
