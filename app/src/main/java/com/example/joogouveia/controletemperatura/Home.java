package com.example.joogouveia.controletemperatura;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


public class Home extends AppCompatActivity implements View.OnClickListener{

    //control variables
    boolean bleConnect = false;

    ImageButton getTemperatureButton;
    TextView lastTemperature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getTemperatureButton = (ImageButton) findViewById(R.id.bt_getTemp);
        lastTemperature = (TextView) findViewById(R.id.tv_lastMeasure);

        getTemperatureButton.setOnClickListener(this);
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
        lastTemperature.setTypeface(montSerrat);
    }
}
