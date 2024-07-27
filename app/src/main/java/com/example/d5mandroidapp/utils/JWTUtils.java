package com.example.d5mandroidapp.utils;

import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class JWTUtils {
    public static void decoded(String JWTEncoded) throws Exception {
        try {
            String[] split = JWTEncoded.split("\\.");
            Log.d("JWT_DECODED", "Header: " + getJson(split[0]));
            Log.d("JWT_DECODED", "Body: " + getJson(split[1]));
        } catch (UnsupportedEncodingException e) {
            //Error
        }
    }
    public static Long getExpirationTime(String JWTEncoded) throws Exception {
        try {
            String[] split = JWTEncoded.split("\\.");
            String body = getJson(split[1]);

            // Parse the body as JSON using a JSON library (recommended)
            JSONObject jsonObject = new JSONObject(body); // Assuming you're using org.json
            long exp = jsonObject.getLong("exp");

            return exp;

        } catch (UnsupportedEncodingException e) {
            throw new Exception("Error decoding JWT body: " + e.getMessage());
        } catch (JSONException e) {
            throw new Exception("Error parsing JWT body as JSON: " + e.getMessage());
        } catch (Exception e) {
            throw new Exception("Unexpected error: " + e.getMessage());
        }
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException{
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }
}
