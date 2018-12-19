package io.github.joaogouveia89.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.lang.reflect.Modifier;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by João Gouveia on 20/10/2017.
 */

public class RetrofitBase {
    private static final String API_BASE_URL = "https://controle-temperatura.herokuapp.com";
    public static final String API_TOKEN = "u^[Y]e^v^KeQ]TV";

    private static RetrofitService retrofit;

    public static RetrofitService getInterfaceRetrofit() {
        if (retrofit == null) new RetrofitBase();
        return retrofit;
    }

    private RetrofitBase() {
        initRetrofit();
    }

    private void initRetrofit() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(60, TimeUnit.SECONDS);
        builder.connectTimeout(60, TimeUnit.SECONDS);

        /* REMOVE FOR RELEASE */
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(logInterceptor);


        GsonBuilder gsonBuilder = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);

        Gson gson = gsonBuilder.create();
        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(gson);

        addAuthorizationHeaders(builder);

        OkHttpClient httpClient = builder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(httpClient)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(RetrofitService.class);
    }

    private void addAuthorizationHeaders(final OkHttpClient.Builder builder) {
        builder.addInterceptor(chain -> {
            Request original = chain.request();

            Request.Builder request = original
                    .newBuilder()
                    .method(original.method(), original.body());
            fillAuthHeaders(request);

            return chain.proceed(request.build());
        });
    }

    private static void fillAuthHeaders(Request.Builder builder) {
        builder.addHeader("auth", API_TOKEN);
    }
}
