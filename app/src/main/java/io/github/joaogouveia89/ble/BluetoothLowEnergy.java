package com.example.joogouveia.joaogouveia89.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by João Gouveia on 18/10/2017.
 */

public class BluetoothLowEnergy {
    private static final String TAG = "BluetoothLowEnergy";

    /**
     * 5 seconds scanning
     */
    private static final int SCAN_TIME_MS = 5000;

    private static final String BLE_NAME = "BT05";

    //Bluetooth Objects
    private static BluetoothManager bleManager;
    private static BluetoothAdapter bleAdapter;
    private static BluetoothLeScanner bleScanner;
    private static BluetoothDevice bleDevice;
    private static BluetoothGatt bleGatt;

    private int packageNumber = 0;


    private byte data;
    private Handler mHandler;
    private Context appContext;

    /**
     * ===========================================================================================
     * ******************************************CONSTRUCTOR**************************************
     * ===========================================================================================
     */

    public BluetoothLowEnergy(Context ctx) {
        this.appContext = ctx;

        mHandler = new Handler();

        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (bleManager == null) {
            bleManager = (BluetoothManager) appContext.getSystemService(Context.BLUETOOTH_SERVICE);
            if (bleManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
            }
        }

        bleAdapter = bleManager.getAdapter();
        if (bleAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
        }

        if(!bleAdapter.isEnabled()){
            bleAdapter.enable();
        }
    }

    /**
     * ===========================================================================================
     * *****************************************UUIDS*********************************************
     * ===========================================================================================
     */

    /**
     * TODO: Change this UUIDs values by the ones you going to use
     */
    UUID serviceUUID = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    UUID characteristicUUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");

    /**
     * ===========================================================================================
     * ****************************SERVICES AND CHARACTERISTICS***********************************
     * ===========================================================================================
     */
    BluetoothGattService service;
    BluetoothGattCharacteristic characteristic;

    /**
     * ===========================================================================================
     * ******************************************ACTIONS******************************************
     * ===========================================================================================
     */

    // Actions used during broadcasts to the main activity
    public final static String ACTION_BLE_SCAN_HAS_BEEN_FINISHED =
            "com.example.joogouveia.controletemperatura.ble.ACTION_BLE_SCAN_HAS_BEEN_FINISHED";
    public final static String ACTION_CONNECTED =
            "com.example.joogouveia.controletemperatura.ble.ACTION_CONNECTED";
    public final static String ACTION_DISCONNECTING =
            "com.example.joogouveia.controletemperatura.ble.ACTION_DISCONNECTING";
    public final static String ACTION_DISCONNECTED =
            "com.example.joogouveia.controletemperatura.ble.ACTION_DISCONNECTED";
    public final static String ACTION_SERVICES_DISCOVERED =
            "com.example.joogouveia.controletemperatura.ble.ACTION_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_RECEIVED =
            "com.example.joogouveia.controletemperatura.ble.ACTION_DATA_RECEIVED";
    public static final String ACTION_CHARACTERISTIC_WROTE =
            "com.example.joogouveia.controletemperatura.ble.ACTION_CHARACTERISTIC_WROTE";
    public static final String ACTION_CONNECT_REQUEST =
            "com.example.joogouveia.controletemperatura.ble.ACTION_CONNECT_REQUEST";

    /**
     * ===========================================================================================
     * ****************************************BLE METHODS****************************************
     * ===========================================================================================
     */

    public void seekAndConnect() {
        ScanSettings settings;
        List<ScanFilter> filters;
        bleScanner = bleAdapter.getBluetoothLeScanner();
        settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build();
        filters = new ArrayList<>();
        Log.i(TAG, "Starting scan");
        bleScanner.startScan(filters, settings, mScanCallback);
        mHandler.postDelayed(finishedScanRunnable, SCAN_TIME_MS);
    }

    public void discoverServices(){
        bleGatt.discoverServices();
    }

    public void disconnect(){
        bleGatt.disconnect();
    }

    public void initializeServiceAndCharacteristic(){
        service = bleGatt.getService(serviceUUID);
        characteristic = service.getCharacteristic(characteristicUUID);
    }
    public void enableCharacteristicNotification(boolean status){
        bleGatt.setCharacteristicNotification(characteristic, status);
    }

    public void askForData(){
        Log.i(TAG, "Asking for data");
        packageNumber = 0;
        characteristic.setValue(new byte[] {0x64});
        //value chosen because there will be no temperature in 100ºC 0x54 = 100
        bleGatt.writeCharacteristic(characteristic);
    }


    /**
     * ===========================================================================================
     * ***************************************BLE CALLBACKS***************************************
     * ===========================================================================================
     */

    private final ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            if(result.getDevice().getName() != null){
                Log.i(TAG, result.getDevice().getName());
            }
            if(result.getDevice().getName() != null && result.getDevice().getName().equals(BLE_NAME)){
                bleDevice = result.getDevice();
                bleScanner.stopScan(mScanCallback);
                broadcastUpdate(ACTION_BLE_SCAN_HAS_BEEN_FINISHED);
                bleGatt = bleDevice.connectGatt(appContext, false, connCallback);
            }
        }
    };

    private final BluetoothGattCallback connCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if(newState == BluetoothGatt.STATE_CONNECTED) {
                broadcastUpdate(ACTION_CONNECTED);
                gatt.discoverServices();
            }else if(newState == BluetoothGatt.STATE_DISCONNECTED){
                broadcastUpdate(ACTION_DISCONNECTED);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status){
            broadcastUpdate(ACTION_SERVICES_DISCOVERED);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status){
            Log.i(TAG, "WROOOTE");
            broadcastUpdate(ACTION_CHARACTERISTIC_WROTE);
        }

        @Override
        public void onCharacteristicChanged (BluetoothGatt gatt,
                                      BluetoothGattCharacteristic characteristic){
            broadcastUpdate(ACTION_DATA_RECEIVED);
            packageNumber++;
            data = characteristic.getValue()[0];
            Log.i(TAG, "Val = " + characteristic.getValue()[0]);
        }
    };


    /**
     * ===========================================================================================
     * ************************************GETTERS AND SETTERS************************************
     * ===========================================================================================
     */

    public byte getData() {
        return data;
    }

    public BluetoothDevice getBleDevice(){
        return bleDevice;
    }

    public int getPackageNumber(){ return packageNumber; }

    public void setPackageNumber(int packageNumber) { this.packageNumber = packageNumber; }


    /**
     * ===========================================================================================
     * *****************************************THREADS*******************************************
     * ===========================================================================================
     */

    Runnable finishedScanRunnable = new Runnable() {
        @Override
        public void run() {
            bleScanner.stopScan(mScanCallback);
            broadcastUpdate(ACTION_BLE_SCAN_HAS_BEEN_FINISHED);
        }
    };



    /**
     * ===========================================================================================
     * ****************************************FOR DEBUG******************************************
     * ===========================================================================================
     */

    public void logServicesCharacteristicsAndDescriptors(){
        List<BluetoothGattService> services = bleGatt.getServices();
        List<BluetoothGattCharacteristic> characteristics;
        List<BluetoothGattDescriptor> descriptors;
        for(BluetoothGattService s : services){
            System.out.println("==============================================");
            Log.i(TAG, "Service " + s.getUuid().toString());
            characteristics = s.getCharacteristics();
            for(BluetoothGattCharacteristic c : characteristics){
                Log.i(TAG, "Characteristic " + c.getUuid().toString());
                descriptors = c.getDescriptors();
                for(BluetoothGattDescriptor d : descriptors){
                    Log.i(TAG, "Descriptor " + d.getUuid().toString());
                }
            }
        }
    }

    /**
     * ===========================================================================================
     * **************************************OTHER METHODS****************************************
     * ===========================================================================================
     */
    public void close() {
        if (bleGatt == null) {
            return;
        }
        bleGatt.close();
        bleGatt = null;
    }

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        appContext.sendBroadcast(intent);
    }
}
