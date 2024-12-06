package isetb.mobileelite.login.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

public class TokenHelper {

    private static final String PREFS_NAME = "auth_prefs";
    private static final String ACCESS_TOKEN_KEY = "access_token";
    private static final String REFRESH_TOKEN_KEY = "refresh_token";
    private static final String REMEMBER_ME_KEY = "remember_me";
    private static final String TOKEN_EXPIRATION_KEY = "token_expiration"; // Optional for inactivity handling

    /**
     * Save access and refresh tokens.
     */
    public static void saveTokens(Context context, String accessToken, String refreshToken) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ACCESS_TOKEN_KEY, accessToken);
        editor.putString(REFRESH_TOKEN_KEY, refreshToken);
        editor.apply();
    }

    /**
     * Retrieve the access token.
     */
    public static String getAccessToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null);
    }

    /**
     * Retrieve the refresh token.
     */
    public static String getRefreshToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(REFRESH_TOKEN_KEY, null);
    }

    /**
     * Save the Remember Me state.
     */
    public static void saveRememberMe(Context context, boolean rememberMe) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(REMEMBER_ME_KEY, rememberMe);
        editor.apply();
    }

    /**
     * Retrieve the Remember Me state.
     */
    public static boolean isRememberMe(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(REMEMBER_ME_KEY, false);
    }

    /**
     * Clear all tokens (for logout functionality).
     */
    public static void clearTokens(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(ACCESS_TOKEN_KEY);
        editor.remove(REFRESH_TOKEN_KEY);
        editor.remove(REMEMBER_ME_KEY);
        editor.remove(TOKEN_EXPIRATION_KEY);
        editor.apply();
    }

    /**
     * Save token expiration timestamp (optional for inactivity).
     */
    public static void saveTokenExpiration(Context context, long expirationTimeMillis) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(TOKEN_EXPIRATION_KEY, expirationTimeMillis);
        editor.apply();
    }

    /**
     * Get the token expiration timestamp.
     */
    public static long getTokenExpiration(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(TOKEN_EXPIRATION_KEY, 0);
    }

    /**
     * Check if the token has expired.
     */
    public static boolean isTokenExpired(Context context) {
        long expirationTime = getTokenExpiration(context);
        return expirationTime != 0 && System.currentTimeMillis() > expirationTime;
    }

    public static JSONObject decodeToken(String token) {
        try {
            // Split the token into parts
            String[] parts = token.split("\\."); // Header, Payload, Signature
            if (parts.length < 2) {
                throw new IllegalArgumentException("Invalid JWT token format.");
            }

            // Decode the payload (middle part)
            String payload = new String(Base64.decode(parts[1], Base64.URL_SAFE));
            return new JSONObject(payload);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Return null if decoding fails
        }
    }
    public static boolean extractIsActive(String token) {
        JSONObject payload = decodeToken(token);
        if (payload != null) {
            Log.d("TokenHelper", "Decoded Token Payload: " + payload.toString()); // Log the decoded payload
            if (payload.has("isActive")) {
                return payload.optBoolean("isActive", false);
            }
        }
        Log.d("TokenHelper", "isActive field not found or decoding failed.");
        return false; // Default if payload is null
    }


}
