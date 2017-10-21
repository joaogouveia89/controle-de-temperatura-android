package com.example.joogouveia.controletemperatura;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;


public class Home extends AppCompatActivity implements View.OnClickListener{

    //control variables
    boolean bleConnect = false;

    ImageButton getTemperatureButton, getSummaryButton, helpButton, saveButton, researchButton;
    TextView lastTemperatureTitle, lastTemperatureTemperature, lastTemperatureTimestamp;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initializeWidgets();
        initializeMontSerratFont();
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

    private void enabledisableAllButtons(boolean enable){
        getTemperatureButton.setEnabled(enable);
        getSummaryButton.setEnabled(enable);
        helpButton.setEnabled(enable);
        saveButton.setEnabled(enable);
        researchButton.setEnabled(enable);
    }
}
