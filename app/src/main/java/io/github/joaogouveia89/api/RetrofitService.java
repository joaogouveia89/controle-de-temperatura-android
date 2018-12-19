package io.github.joaogouveia89.api;


import java.util.List;

import io.github.joaogouveia89.api.model.Temperature;
import io.github.joaogouveia89.app.Constants;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by João Gouveia on 20/10/2017.
 */

public interface RetrofitService {

    @POST(Constants.Service.POST_TEMPERATURE)
    Observable<Temperature> sendTemperature(@Body Temperature temperature);

    @GET(Constants.Service.GET_LAST_TEMPERATURE)
    Observable<Temperature> getLastTemperature();

    @GET(Constants.Service.GET_TEMPERATURE_LIST)
    Observable<List<Temperature>> getTemperatures();

    @GET(Constants.Service.GET_MONTH_TEMPERATURES)
    Observable<List<Temperature>> getMonthTemperatures(
            @Path("month") int month,
            @Path("year") int year);
}
