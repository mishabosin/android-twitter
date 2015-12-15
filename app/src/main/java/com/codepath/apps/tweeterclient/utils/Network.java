package com.codepath.apps.tweeterclient.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Network {
    public static String parseJsonErrorResponse(JSONObject errorResponse) {
        String errorMessage = "";

        JSONArray errors;
        try {
            errors = errorResponse.getJSONArray("errors");
        } catch (JSONException e) {
            return "Unable to parse error message";
        }

        for (int i = 0; i < errors.length(); i++) {
            try {
                JSONObject error = errors.getJSONObject(i);
                errorMessage += error.getString("message");
            } catch (JSONException e) {
                errorMessage += "Unable to parse error message";
            }
            if (i != errors.length() - 1) {
                errorMessage += " ";
            }
        }

        return errorMessage;
    }
}
