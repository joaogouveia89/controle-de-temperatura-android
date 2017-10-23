package com.example.joogouveia.controletemperatura;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joogouveia.controletemperatura.api.RetrofitService;
import com.example.joogouveia.controletemperatura.api.ServiceGenerator;
import com.example.joogouveia.controletemperatura.api.model.Temperature;
import com.example.joogouveia.controletemperatura.ble.BluetoothLowEnergy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.joogouveia.controletemperatura.ble.BluetoothLowEnergy.ACTION_BLE_SCAN_HAS_BEEN_FINISHED;
import static com.example.joogouveia.controletemperatura.ble.BluetoothLowEnergy.ACTION_CHARACTERISTIC_WROTE;
import static com.example.joogouveia.controletemperatura.ble.BluetoothLowEnergy.ACTION_CONNECTED;
import static com.example.joogouveia.controletemperatura.ble.BluetoothLowEnergy.ACTION_DATA_RECEIVED;
import static com.example.joogouveia.controletemperatura.ble.BluetoothLowEnergy.ACTION_SERVICES_DISCOVERED;


public class Home extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "HomeActivity";
    private static final String API_TOKEN = "u^[Y]e^v^KeQ]TV";

    //control variables
    boolean bleConnect = false;

    private ImageButton getTemperatureButton, getSummaryButton, helpButton, saveButton, researchButton;
    private TextView lastTemperatureTitle, lastTemperatureTemperature, lastTemperatureTimestamp;
    private ProgressBar progressBar;

    private BluetoothLowEnergy ble;

    private String temperature = "";
    private String date;
    private String hour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initializeWidgets();
        initializeMontSerratFont();

        getLastTemperatureMeasured();

        ble = new BluetoothLowEnergy(this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        getLastTemperatureMeasured();
    }

    @Override
    protected void onResume(){
        super.onResume();

        final IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_CONNECTED);
        filter.addAction(ACTION_SERVICES_DISCOVERED);
        filter.addAction(ACTION_CHARACTERISTIC_WROTE);
        filter.addAction(ACTION_DATA_RECEIVED);
        filter.addAction(ACTION_BLE_SCAN_HAS_BEEN_FINISHED);

        registerReceiver(mBleUpdateReceiver, filter);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.bt_getTemp:
                if(!bleConnect){
                    connectAndGetTemp();
                }
                break;
            case R.id.bt_save:
                saveData();
                break;

            case R.id.bt_research:
                temperature = "";
                progressBar.setVisibility(View.VISIBLE);
                enabledisableAllButtons(false);
                ble.askForData();
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


        visibleSaveAndResearchButtons(View.INVISIBLE);
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

    private void connectAndGetTemp(){
        progressBar.setVisibility(View.VISIBLE);
        enabledisableAllButtons(false);
        ble.seekAndConnect();
    }

    private void visibleSaveAndResearchButtons(int status){
        saveButton.setVisibility(status);
        researchButton.setVisibility(status);
    }


    private void saveData(){
        RetrofitService service = ServiceGenerator.createService(RetrofitService.class);

        progressBar.setVisibility(View.VISIBLE);
        enabledisableAllButtons(false);
        Call<Temperature> call = service.sendTemperature(temperature, date, hour, API_TOKEN);

        call.enqueue(new Callback<Temperature>() {
            @Override
            public void onResponse(Call<Temperature> call, Response<Temperature> response) {
                Log.i("NEW DATA", "SUCCESSFULL");
                progressBar.setVisibility(View.INVISIBLE);
                enabledisableAllButtons(true);
                if(response.isSuccessful()){
                    if(response.raw().message().equals("OK")){
                        saveButton.setEnabled(false);
                        Toast.makeText(Home.this,getString(R.string.saved_success), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Temperature> call, Throwable t) {
                Toast.makeText(Home.this,getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        });
    }




    private final BroadcastReceiver mBleUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            switch (action){
                case ACTION_CONNECTED:
                    bleConnect = true;
                    ble.discoverServices();
                    break;
                case ACTION_SERVICES_DISCOVERED:
                    getTemperatureButton.setBackgroundResource(R.drawable.selector_disconnection_button);
                    ble.initializeServiceAndCharacteristic();
                    ble.askForData();
                    break;

                case ACTION_CHARACTERISTIC_WROTE:
                    ble.enableCharacteristicNotification(true);
                    break;
                case ACTION_BLE_SCAN_HAS_BEEN_FINISHED:
                    if(ble.getBleDevice() == null){
                        progressBar.setVisibility(View.INVISIBLE);
                        enabledisableAllButtons(true);
                        Toast t = Toast.makeText(Home.this, "Termometro nao encontrado!", Toast.LENGTH_SHORT);
                        t.show();
                    }
                    break;
                case ACTION_DATA_RECEIVED:
                    if((int)ble.getData() != 85){
                        if(ble.getPackageNumber() == 1){
                            temperature += ble.getData() + ".";
                        }else if(ble.getPackageNumber() == 2){
                            long milis = System.currentTimeMillis();
                            progressBar.setVisibility(View.INVISIBLE);
                            enabledisableAllButtons(true);
                            temperature += ble.getData();
                            ble.setPackageNumber(0);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
                            Date currentTimestamp = new Date(milis);

                            date = dateFormat.format(currentTimestamp);
                            hour = hourFormat.format(currentTimestamp);

                            lastTemperatureTemperature.setText(temperature + " ºC");
                            lastTemperatureTimestamp.setText(date + " - " + hour);
                            visibleSaveAndResearchButtons(View.VISIBLE);
                            ble.enableCharacteristicNotification(false);
                        }
                    }
                    break;
            }
        }
    };
}
