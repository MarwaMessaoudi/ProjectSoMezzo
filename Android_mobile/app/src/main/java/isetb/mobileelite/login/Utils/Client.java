package isetb.mobileelite.login.Utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import isetb.mobileelite.login.Adapter.AuthInterceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {

    // Create a Gson object without the LocalDateTime adapter
    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd") // Optionally set a default date format for Date fields
            .create();

    public static Retrofit getRetrofit(String url, Context context) {
        // Create a logging interceptor to log network requests and responses
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // Log the full request/response including the body

        // Create OkHttpClient with both AuthInterceptor and HttpLoggingInterceptor
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(context))  // Add AuthInterceptor for Authorization header
                .addInterceptor(loggingInterceptor)            // Add HttpLoggingInterceptor for logging network traffic
                .connectTimeout(30, TimeUnit.SECONDS)          // Set connection timeout
                .readTimeout(30, TimeUnit.SECONDS)             // Set read timeout
                .build();

        // Build and return Retrofit instance
        return new Retrofit.Builder()
                .baseUrl(url)                          // Set the base URL
                .client(okHttpClient)                  // Set OkHttpClient with interceptors
                .addConverterFactory(GsonConverterFactory.create(gson))  // Use Gson for JSON serialization
                .build();
    }
}
