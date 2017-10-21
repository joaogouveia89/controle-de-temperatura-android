package com.example.joogouveia.controletemperatura;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.joogouveia.controletemperatura.api.RetrofitService;
import com.example.joogouveia.controletemperatura.api.ServiceGenerator;
import com.example.joogouveia.controletemperatura.api.model.Temperature;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Home extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "HomeActivity";
    private static final String API_TOKEN = "u^[Y]e^v^KeQ]TV";

    //control variables
    boolean bleConnect = false;

    private ImageButton getTemperatureButton, getSummaryButton, helpButton, saveButton, researchButton;
    private TextView lastTemperatureTitle, lastTemperatureTemperature, lastTemperatureTimestamp;
    private ProgressBar progressBar;
    private float temperature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initializeWidgets();
        initializeMontSerratFont();

        getLastTemperatureMeasured();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.bt_getTemp:
                break;
        }
    }

    private void initializeMontSerratFont(){
        Typeface montSerrat = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Medium.ttf");
        lastTemperatureTitle.setTypeface(montSerrat);
        lastTemperatureTemperature.setTypeface(montSerrat);
        lastTemperatureTimestamp.setTypeface(montSerrat);
    }

    private void initializeWidgets(){
        getTemperatureButton            = (ImageButton) findViewById(R.id.bt_getTemp);
        getSummaryButton                = (ImageButton) findViewById(R.id.bt_getSummary);
        helpButton                      = (ImageButton) findViewById(R.id.bt_help);
        saveButton                      = (ImageButton) findViewById(R.id.bt_save);
        researchButton                  = (ImageButton) findViewById(R.id.bt_research);


        lastTemperatureTitle = (TextView) findViewById(R.id.tv_lastMeasureTitle);
        lastTemperatureTemperature = (TextView) findViewById(R.id.tv_lastMeasureTemp);
        lastTemperatureTimestamp = (TextView) findViewById(R.id.tv_lastMeasureTimestamp);


        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);


        getTemperatureButton.setOnClickListener(this);
        getSummaryButton.setOnClickListener(this);
        helpButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        researchButton.setOnClickListener(this);


        saveButton.setVisibility(View.INVISIBLE);
        researchButton.setVisibility(View.INVISIBLE);
    }


    private void getLastTemperatureMeasured(){

        RetrofitService service = ServiceGenerator.createService(RetrofitService.class);
        Call<List<Temperature>> call = service.getLastTemperature("https://controle-temperatura.herokuapp.com/api/lasttemperature/" + API_TOKEN);

        progressBar.setVisibility(View.VISIBLE);
        enabledisableAllButtons(false);
        call.enqueue(new Callback<List<Temperature>>() {
            @Override
            public void onResponse(Call<List<Temperature>> call, Response<List<Temperature>> response) {
                lastTemperatureTemperature.setText(response.body().get(0).getTemperature() + "ºC");
                lastTemperatureTimestamp.setText(response.body().get(0).getDate() + " - " + response.body().get(0).getHour());
                progressBar.setVisibility(View.INVISIBLE);
                enabledisableAllButtons(true);
            }

            @Override
            public void onFailure(Call<List<Temperature>> call, Throwable t) {
                Log.i(TAG, "FAILURE!");
            }
        });
    }

    private void enabledisableAllButtons(boolean enable){
        getTemperatureButton.setEnabled(enable);
        getSummaryButton.setEnabled(enable);
        helpButton.setEnabled(enable);
        saveButton.setEnabled(enable);
        researchButton.setEnabled(enable);
    }
}
