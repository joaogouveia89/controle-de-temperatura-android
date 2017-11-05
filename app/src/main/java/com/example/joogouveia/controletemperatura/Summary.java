package com.example.joogouveia.controletemperatura;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.joogouveia.controletemperatura.api.RetrofitService;
import com.example.joogouveia.controletemperatura.api.ServiceGenerator;
import com.example.joogouveia.controletemperatura.api.model.Temperature;
import com.example.joogouveia.controletemperatura.custom.calendar.CustomCalendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.joogouveia.controletemperatura.activities.Home.API_TOKEN;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Summary.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Summary#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Summary extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final int PREVIOUS = 0;
    public static final int CURRENT = 1;
    public static final int NEXT = 2;
    private int changeFrame = CURRENT;

    private TextView fragmentTitle;
    private View view;
    private ColumnChartView columnChartView;

    private ColumnChartData data;
    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLabels = false;
    private boolean hasLabelForSelected = false;


    private Calendar cal = Calendar.getInstance();
    private CustomCalendar calendar = new CustomCalendar(cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)); //current Date;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Summary() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Summary.
     */
    // TODO: Rename and change types and number of parameters
    public static Summary newInstance(String param1, String param2) {
        Summary fragment = new Summary();
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
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_summary, container, false);
        fragmentTitle = (TextView) view.findViewById(R.id.titleLabel);
        fragmentTitle.setText(calendar.getMonthStringPtBr() + " " + calendar.getCurrentYear());
        columnChartView = (ColumnChartView) view.findViewById(R.id.chart);
        fetchMonthData(calendar.getCurrentMonth(), calendar.getCurrentYear());
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public int getChangeFrame() {
        return changeFrame;
    }

    public void setChangeFrame(int changeFrame) {
        this.changeFrame = changeFrame;
        if(changeFrame == NEXT){
            calendar.advanceOneMonth();
        }else if(changeFrame == PREVIOUS){
            calendar.regressOneMonth();
        }
        fragmentTitle.setText(calendar.getMonthStringPtBr() + " " + calendar.getCurrentYear());
        fetchMonthData(calendar.getCurrentMonth(), calendar.getCurrentYear());
        Log.i("SUMMA", calendar.getMonthStringPtBr() + " de " + calendar.getCurrentYear());
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

    private void fetchMonthData(int month, int year){
        RetrofitService service = ServiceGenerator.createService(RetrofitService.class);
        Call<List<Temperature>> call = service.getMonthTemperatures("https://controle-temperatura.herokuapp.com/api/temperatures/" +  month +"/" + year + "/" + API_TOKEN);

        call.enqueue(new Callback<List<Temperature>>() {
            @Override
            public void onResponse(Call<List<Temperature>> call, Response<List<Temperature>> response) {
                setChartData(response.body());
            }

            @Override
            public void onFailure(Call<List<Temperature>> call, Throwable t) {

            }
        });
    }

    private void setChartData(List<Temperature> temperatures){
        int subColumns = 1;
        int numColums = temperatures.size();

        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        List<AxisValue> axisXValues = new ArrayList<AxisValue>();

        for(int i = 0; i < numColums; ++i){

            values = new ArrayList<SubcolumnValue>();
            for(int j = 0; j < subColumns; ++j){
                SubcolumnValue val = new SubcolumnValue(Float.parseFloat(temperatures.get(i).getTemperature()), ChartUtils.pickColor());
                values.add(val);
                String[] dateTemp = temperatures.get(i).getDate().split("/");
                AxisValue axval = new AxisValue(Float.parseFloat(dateTemp[0]));
                Log.i("SUMMARY", "VAL = " + dateTemp[0]);
                axval.setLabel(dateTemp[0]);
                axisXValues.add(axval);
            }
            Column column = new Column(values);
            column.setHasLabels(hasLabels);
            column.setHasLabelsOnlyForSelected(hasLabelForSelected);
            columns.add(column);
        }

        data = new ColumnChartData(columns);

        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName(getString(R.string.day));
                axisY.setName(getString(R.string.temperature));
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
            //axisX.setValues(axisXValues);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }
        columnChartView.setColumnChartData(data);

    }

    private void generateDefaultData() {
        int numSubcolumns = 1;
        int numColumns = 30;
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {

            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {
                values.add(new SubcolumnValue((float) Math.random() * 50f + 5, ChartUtils.pickColor()));
            }

            Column column = new Column(values);
            column.setHasLabels(hasLabels);
            column.setHasLabelsOnlyForSelected(hasLabelForSelected);
            columns.add(column);
        }

        data = new ColumnChartData(columns);

        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName(getString(R.string.day));
                axisY.setName(getString(R.string.temperature));
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        columnChartView.setColumnChartData(data);

    }
}
