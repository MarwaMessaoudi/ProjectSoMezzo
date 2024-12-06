package isetb.mobileelite.login.Utils;

import android.content.Context;

public class Apis {
    public static final String URL="http://192.168.1.16:8084";
    public static UserService getService(Context context) {
        return Client.getRetrofit(URL, context).create(UserService.class);
    }
    public static AppelService getAppelService(Context context) {
        return Client.getRetrofit(URL, context).create(AppelService.class);
    }
}
