package az.amorphist.poster.model.server;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static az.amorphist.poster.Constants.SYSTEM.API_KEY;

public class HttpInterceptor implements Interceptor {
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        final Request request = chain.request();
        final String sign;
        if (TextUtils.isEmpty(request.url().encodedQuery())) {
            sign = "?";
        } else {
            sign = "&";
        }
        final Request.Builder newRequest = request.newBuilder()
                .url(request.url().toString() + sign + "api_key=" + API_KEY);
        return chain.proceed(newRequest.build());
    }
}
