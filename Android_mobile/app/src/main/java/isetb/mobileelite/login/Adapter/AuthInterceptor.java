package isetb.mobileelite.login.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import isetb.mobileelite.login.Utils.TokenHelper;

public class AuthInterceptor implements Interceptor {

    private Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        String accessToken = TokenHelper.getAccessToken(context);  // Retrieve the access token

        Request originalRequest = chain.request();

        // Add Authorization header only if the token is available
        if (accessToken != null && !accessToken.isEmpty()) {
            Request modifiedRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer " + accessToken)
                    .build();
            return chain.proceed(modifiedRequest);
        }

        return chain.proceed(originalRequest);
    }
}
