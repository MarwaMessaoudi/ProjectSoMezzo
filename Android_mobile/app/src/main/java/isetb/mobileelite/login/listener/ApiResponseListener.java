package isetb.mobileelite.login.listener;

public interface ApiResponseListener<T> {
        void onSuccess(T response);        // Callback for successful API responses
        void onError(String errorMessage); // Callback for API errors
        void onIsnotActive(String Activitystate);

}
