package com.harshitbhardwaj.responses.common;

import com.harshitbhardwaj.constants.Constants;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class CommonResponse {
    
    public static Response checkHealthOfNotesApi() {
        return RestAssured.when().get(Constants.Common.healthCheck);
    }

}