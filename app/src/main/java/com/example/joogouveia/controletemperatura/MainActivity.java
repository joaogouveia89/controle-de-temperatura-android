package com.example.joogouveia.controletemperatura;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;

import com.example.joogouveia.controletemperatura.ble.BluetoothLowEnergy;

import static com.example.joogouveia.controletemperatura.NewData.ACTION_GET_TEMPERATURE;
import static com.example.joogouveia.controletemperatura.ble.BluetoothLowEnergy.ACTION_BLE_SCAN_HAS_BEEN_FINISHED;
import static com.example.joogouveia.controletemperatura.ble.BluetoothLowEnergy.ACTION_CHARACTERISTIC_WROTE;
import static com.example.joogouveia.controletemperatura.ble.BluetoothLowEnergy.ACTION_CONNECTED;
import static com.example.joogouveia.controletemperatura.ble.BluetoothLowEnergy.ACTION_CONNECT_REQUEST;
import static com.example.joogouveia.controletemperatura.ble.BluetoothLowEnergy.ACTION_DATA_RECEIVED;
import static com.example.joogouveia.controletemperatura.ble.BluetoothLowEnergy.ACTION_DISCONNECTED;
import static com.example.joogouveia.controletemperatura.ble.BluetoothLowEnergy.ACTION_DISCONNECTING;
import static com.example.joogouveia.controletemperatura.ble.BluetoothLowEnergy.ACTION_SERVICES_DISCOVERED;

public class MainActivity extends AppCompatActivity {

    ProgressDialog progressDialog;

    private BluetoothLowEnergy ble;
    private PagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.connecting) + "...");
        progressDialog.create();

        ble = new BluetoothLowEnergy(this);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Resumo"));
        tabLayout.addTab(tabLayout.newTab().setText("Nova Temperatura"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        final IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_CONNECTED);
        filter.addAction(BluetoothLowEnergy.ACTION_DISCONNECTED);
        filter.addAction(BluetoothLowEnergy.ACTION_SERVICES_DISCOVERED);
        filter.addAction(BluetoothLowEnergy.ACTION_DATA_RECEIVED);
        filter.addAction(BluetoothLowEnergy.ACTION_DISCONNECTING);
        filter.addAction(BluetoothLowEnergy.ACTION_BLE_SCAN_HAS_BEEN_FINISHED);
        filter.addAction(BluetoothLowEnergy.ACTION_CHARACTERISTIC_WROTE);
        filter.addAction(BluetoothLowEnergy.ACTION_CONNECT_REQUEST);
        filter.addAction(ACTION_GET_TEMPERATURE);
        registerReceiver(mBleUpdateReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mBleUpdateReceiver);
    }

    private final BroadcastReceiver mBleUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            switch (action) {
                case ACTION_CONNECT_REQUEST:
                    progressDialog.show();
                    ble.seekAndConnect();
                    break;
                case ACTION_CONNECTED:
                    ble.discoverServices();
                    break;
                case ACTION_SERVICES_DISCOVERED:
                    progressDialog.dismiss();
                    NewData nd = (NewData) adapter.getNewData();
                    nd.enableGetDataButton();
                    nd.setDisconnect();
                    ble.initializeServiceAndCharacteristic();
                    break;
                case ACTION_DATA_RECEIVED:
                    break;
                case ACTION_DISCONNECTING:
                    break;
                case ACTION_DISCONNECTED:
                    break;
                case ACTION_GET_TEMPERATURE:
                    ble.askForData();
                    break;
                case ACTION_BLE_SCAN_HAS_BEEN_FINISHED:
                    if(ble.getBleDevice() == null){
                        progressDialog.dismiss();
                        alertDeviceNotFound();
                    }
                    break;
                case ACTION_CHARACTERISTIC_WROTE:
                    break;
            }
        }
    };



    private void alertDeviceNotFound(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.device_not_found));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create();
        builder.show();
    }

}
