package com.example.joogouveia.joaogouveia89.api;

import com.example.joogouveia.joaogouveia89.api.model.Temperature;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by João Gouveia on 20/10/2017.
 */

public interface RetrofitService {
    @Headers("X-Mashape-Key: AuuyclCPjcmshv2iOPq190OpzLrMp1FJWwejsnJrdfwOUr4h44")
    @FormUrlEncoded
    @POST("temperatures")
    Call<Temperature> sendTemperature(@Field("temperature") String temperature,
                                       @Field("date") String date,
                                       @Field("hour") String hour,
                                       @Field("token") String token);

    @GET
    Call<List<Temperature>> getLastTemperature(@Url String url);

    @GET
    Call<List<Temperature>> getTemperatures(@Url String url);

    @GET
    Call<List<Temperature>> getMonthTemperatures(@Url String url);
}
