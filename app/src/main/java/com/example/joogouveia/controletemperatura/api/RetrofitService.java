package com.example.joogouveia.controletemperatura.api;

import com.example.joogouveia.controletemperatura.api.model.Temperature;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

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
}
