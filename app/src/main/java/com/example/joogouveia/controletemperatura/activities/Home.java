package com.example.joogouveia.controletemperatura.activities;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.FragmentManager;

import com.example.joogouveia.controletemperatura.R;
import com.example.joogouveia.controletemperatura.SumTransition;
import com.example.joogouveia.controletemperatura.fragments.Summary;
import com.example.joogouveia.controletemperatura.adapters.FragmentPager;
import com.example.joogouveia.controletemperatura.adapters.TemperatureAdapter;
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

import static com.example.joogouveia.controletemperatura.ble.BluetoothLowEnergy.ACTION_DISCONNECTED;
import static com.example.joogouveia.controletemperatura.fragments.Summary.NEXT;
import static com.example.joogouveia.controletemperatura.fragments.Summary.PREVIOUS;
import static com.example.joogouveia.controletemperatura.ble.BluetoothLowEnergy.ACTION_BLE_SCAN_HAS_BEEN_FINISHED;
import static com.example.joogouveia.controletemperatura.ble.BluetoothLowEnergy.ACTION_CHARACTERISTIC_WROTE;
import static com.example.joogouveia.controletemperatura.ble.BluetoothLowEnergy.ACTION_CONNECTED;
import static com.example.joogouveia.controletemperatura.ble.BluetoothLowEnergy.ACTION_DATA_RECEIVED;
import static com.example.joogouveia.controletemperatura.ble.BluetoothLowEnergy.ACTION_SERVICES_DISCOVERED;


public class Home extends AppCompatActivity implements View.OnClickListener,
        Summary.OnFragmentInteractionListener,
        ViewPager.OnPageChangeListener,
        SumTransition.OnFragmentInteractionListener{

    private static final String TAG = "HomeActivity";
    public static final String API_TOKEN = "u^[Y]e^v^KeQ]TV";

    //control variables
    boolean bleConnect = false;

    private ImageButton getTemperatureButton, getSummaryButton, helpButton, saveButton, researchButton;
    private TextView lastTemperatureTitle, lastTemperatureTemperature, lastTemperatureTimestamp;
    private ProgressBar progressBar;
    private RecyclerView mRecyclerView;
    private ViewPager pager;
    private FragmentPager fpager;
    private Summary summaryFragment;

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

        FragmentManager fm = getSupportFragmentManager();
        fpager = new FragmentPager(fm);

        pager.setAdapter(fpager);
        pager.setCurrentItem(1);

        pager.addOnPageChangeListener(this);
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
                }else{
                    disconnect();
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

            case R.id.bt_getSummary:
                getTemperaturesList();
               break;

            case R.id.bt_help:
                openInformationModal();
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
        pager                           = (ViewPager) findViewById(R.id.vp_summary);


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

    private void getTemperaturesList(){
        progressBar.setVisibility(View.VISIBLE);
        enabledisableAllButtons(false);
        RetrofitService service = ServiceGenerator.createService(RetrofitService.class);
        Call<List<Temperature>> call = service.getTemperatures("https://controle-temperatura.herokuapp.com/api/temperatures/" + API_TOKEN);

        call.enqueue(new Callback<List<Temperature>>() {
            @Override
            public void onResponse(Call<List<Temperature>> call, final Response<List<Temperature>> response) {
                //dialog creation
                LayoutInflater inflater = getLayoutInflater();
                AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                builder.setTitle(getString(R.string.measures));
                View inflated = inflater.inflate(R.layout.temperatures_list, null);
                mRecyclerView = (RecyclerView) inflated.findViewById(R.id.rv_temperatures_list);
                mRecyclerView.setHasFixedSize(true);

                LinearLayoutManager llm = new LinearLayoutManager(Home.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                mRecyclerView.setLayoutManager(llm);

                TemperatureAdapter mTemperatureAdapter = new TemperatureAdapter(Home.this, response.body());
                mRecyclerView.setAdapter(mTemperatureAdapter);
                builder.setView(inflated);
                //tvList = (TextView) inflated.findViewById(R.id.tvList);
                builder.setNeutralButton(getString(R.string.dismiss), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create();
                builder.show();
                progressBar.setVisibility(View.INVISIBLE);
                enabledisableAllButtons(true);
            }

            @Override
            public void onFailure(Call<List<Temperature>> call, Throwable t) {

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

    private void disconnect(){
        progressBar.setVisibility(View.VISIBLE);
        enabledisableAllButtons(false);
        ble.disconnect();
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
                case ACTION_DISCONNECTED:
                    bleConnect = false;
                    enabledisableAllButtons(true);
                    getTemperatureButton.setBackgroundResource(R.drawable.selector_connection_button);
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        summaryFragment = fpager.getSummaryFragment();
        if(position == 2){
            summaryFragment.setChangeFrame(NEXT);
            pager.setCurrentItem(1);
        }else if(position == 0){
            summaryFragment.setChangeFrame(PREVIOUS);
            pager.setCurrentItem(1);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void openInformationModal(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.help_layout, null);
        builder.setView(v);
        builder.setTitle(getString(R.string.help_title));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.create();
        builder.show();
    }
}
