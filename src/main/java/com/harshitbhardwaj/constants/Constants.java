package com.harshitbhardwaj.constants;

import static com.harshitbhardwaj.config.ConfigurationManager.configuration;

/**
 * @author Harshit Bhardwaj
 */
public final class Constants {

    private Constants() {
        throw new AssertionError("Cannot instantiate Constants.class");
    }

    public static final class Common {

        public static final String BASE_URL = configuration().baseUrl();
        public static final String healthCheck = BASE_URL + "/health-check";

        /*
         * User End-points
         */
        public static final String registerUser = BASE_URL + "/users/register";
        public static final String loginUser = BASE_URL + "/users/login";
        public static final String userProfile = BASE_URL + "/users/profile";
        public static final String logoutUser = BASE_URL + "/users/logout";
        public static final String deleteUser = BASE_URL + "/users/delete-account";
        public static final String changeUserPassword = BASE_URL + "/users/change-password";

        /*
         * Notes End-points
         */
        public static final String getAllNotesOrPostNote = BASE_URL + "/notes";
        public static final String createReadUpdateOrDeleteNoteById = BASE_URL + "/notes/{id}";

        /*
         *  Success Messages
         */
        public static final String SUCCESS_HEALTH_MESSAGE = "Notes API is Running";
        public static final String VALID_USER_REGISTER_MESSAGE = "User account created successfully";
        public static final String SUCCESSFUL_LOGIN_MESSAGE = "Login successful";
        public static final String SUCCESSFUL_LOGOUT_MESSAGE = "User has been successfully logged out";
        public static final String SUCCESSFUL_ACCOUNT_DELETE_MESSAGE = "Account successfully deleted";
        public static final String SUCCESSFUL_USER_PROFILE_MESSAGE = "Profile successful";

        private Common() {
            throw new AssertionError("Cannot instantiate Constants.Common.class");
        }
    }

    public static final class Errors {

        /*
         *  Error Messages
         */
        public static final String AUTH_MISSING_MESSAGE = "No authentication token specified in x-auth-token header";
        public static final String INVALID_PASSWORD_MESSAGE = "Password must be between 6 and 30 characters";
        public static final String INVALID_EMAIL_MESSAGE = "A valid email address is required";
        public static final String INVALID_CREDENTIALS_MESSAGE = "Incorrect email address or password";
        public static final String INVALID_ACCESS_TOKEN_MESSAGE = "Access token is not valid or has expired, you will need to login";


        private Errors() {
            throw new AssertionError("Cannot instantiate Constants.Errors class");
        }
    }

}


