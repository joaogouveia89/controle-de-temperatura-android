package com.example.joogouveia.controletemperatura;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joogouveia.controletemperatura.api.RetrofitService;
import com.example.joogouveia.controletemperatura.api.ServiceGenerator;
import com.example.joogouveia.controletemperatura.api.model.Temperature;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.joogouveia.controletemperatura.ble.BluetoothLowEnergy.ACTION_CONNECT_REQUEST;
import static com.example.joogouveia.controletemperatura.ble.BluetoothLowEnergy.ACTION_DISCONNECTED;
import static com.example.joogouveia.controletemperatura.ble.BluetoothLowEnergy.ACTION_DISCONNECTING;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewData.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewData#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewData extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String ACTION_GET_TEMPERATURE =
            "com.example.joogouveia.controletemperatura.newdata.ACTION_GET_TEMPERATURE";
    public static final String ACTION_SAVING_TEMPERATURE =
            "com.example.joogouveia.controletemperatura.newdata.ACTION_SAVING_TEMPERATURE";
    public static final String ACTION_RETROFIT_REQUEST_FINISHED =
            "com.example.joogouveia.controletemperatura.newdata.ACTION_RETROFIT_REQUEST_FINISHED";



    private static final String API_TOKEN = "u^[Y]e^v^KeQ]TV";

    private Context appCtx;

    Button connectButton;
    Button requestButton;
    TextView temperatureTextView;
    Button sendButton;

    int integerPart;
    int decimalPart;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public NewData() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewData.
     */
    // TODO: Rename and change types and number of parameters
    public static NewData newInstance(String param1, String param2) {
        NewData fragment = new NewData();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_data, container, false);

        connectButton = (Button) view.findViewById(R.id.bt_connect);
        requestButton = (Button) view.findViewById(R.id.bt_getTemp);
        temperatureTextView = (TextView) view.findViewById(R.id.tv_temperature);
        sendButton = (Button) view.findViewById(R.id.bt_send);

        appCtx = view.getContext();

        connectButton.setOnClickListener(this);
        requestButton.setOnClickListener(this);
        sendButton.setOnClickListener(this);
        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.bt_connect:
                if(connectButton.getText().equals(getString(R.string.connect))){
                    broadcastUpdate(ACTION_CONNECT_REQUEST);
                }else{
                    broadcastUpdate(ACTION_DISCONNECTING);
                }
                break;
            case R.id.bt_getTemp:
                broadcastUpdate(ACTION_GET_TEMPERATURE);
                break;

            case R.id.bt_send:
                retrofitConverter();
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        appCtx.sendBroadcast(intent);
    }

    public void enableGetDataButton(){
        requestButton.setEnabled(true);
    }

    public void setDisconnect(){
        connectButton.setText(getString(R.string.disconnect));
    }
    public void setConnect(){
        connectButton.setText(getString(R.string.connect));
    }

    public void setIntegerPart(int number){ integerPart = number; }
    public void setDecimalPart(int number){ decimalPart = number; }

    public void showSendTemperatureOptions(){
        temperatureTextView.setVisibility(View.VISIBLE);
        sendButton.setVisibility(View.VISIBLE);
        temperatureTextView.setText("Temperatura: " + integerPart + "," + decimalPart + "ºC");
    }

    public void resetWidgets(){
        temperatureTextView.setVisibility(View.INVISIBLE);
        sendButton.setVisibility(View.INVISIBLE);
        requestButton.setEnabled(false);
    }

    public void retrofitConverter(){
        String temperature = integerPart + "." + decimalPart;
        long milis = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
        Date currentTimestamp = new Date(milis);
        RetrofitService service = ServiceGenerator.createService(RetrofitService.class);

        broadcastUpdate(ACTION_SAVING_TEMPERATURE);
        Call<Temperature> call = service.sendTemperature(temperature, dateFormat.format(currentTimestamp), hourFormat.format(currentTimestamp), API_TOKEN);

        call.enqueue(new Callback<Temperature>() {
            @Override
            public void onResponse(Call<Temperature> call, Response<Temperature> response) {
                Log.i("NEW DATA", "SUCCESSFULL");
                broadcastUpdate(ACTION_RETROFIT_REQUEST_FINISHED);
                if(response.isSuccessful()){
                    if(response.raw().message().equals("OK")){
                        Toast.makeText(appCtx,getString(R.string.saved_success), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Temperature> call, Throwable t) {
                Toast.makeText(appCtx,getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
