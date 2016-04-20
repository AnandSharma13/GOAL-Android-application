package com.ph.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.ph.R;
import com.ph.Utils.DateOperations;
import com.ph.Utils.StepsCountClick;
import com.ph.model.DBOperations;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StepsDay.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StepsDay#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepsDay extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView stepsDay;
    @Bind(R.id.progress_steps_day_mine_text)
     TextView stepsDayText;

    @Bind(R.id.progress_steps_week_others)
     TextView stepsOthersAvg;

    @Bind(R.id.progress_steps_week_others_text)
     TextView stepsOthersAvgText;

    @Bind(R.id.steps_day_bar_chart)
    BarChart barChart;
    private RelativeLayout stepsLayout;
    private DBOperations dbOperations;
    private DateOperations dateOperations;

    private OnFragmentInteractionListener mListener;

    public StepsDay() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StepsDay.
     */
    // TODO: Rename and change types and number of parameters
    public static StepsDay newInstance(String param1, String param2) {
        StepsDay fragment = new StepsDay();
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
        dbOperations = new DBOperations(getContext());
        dateOperations = new DateOperations(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_steps_day, container, false);
        ButterKnife.bind(this,v);
        int stepsCount = dbOperations.getStepsCountForToday();

        stepsDay = (TextView) v.findViewById(R.id.progress_steps_day_mine);

        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Eurostile.ttf");
        Typeface custom_font2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/HelveticaNeue.ttf");
        stepsDay.setTypeface(custom_font);
        stepsDayText.setTypeface(custom_font2);
        stepsOthersAvg.setTypeface(custom_font);
        stepsOthersAvgText.setTypeface(custom_font2);
        stepsLayout = (RelativeLayout) v.findViewById(R.id.steps_day_relative_layout);

        stepsLayout.setOnClickListener(new StepsCountClick(getActivity(), new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
             stepsDay.setText(String.valueOf(dbOperations.getStepsCountForToday()));
                setUpBarChart();

            }
        }));


        stepsDay.setText(String.valueOf(stepsCount));

        setUpBarChart();






        return v;
    }


    private void setUpBarChart()
    {
        HashMap<String, Integer> data = dbOperations.getStepsForThisWeek();
        ArrayList<BarEntry> dataList = new ArrayList<>();
        int i=0;
        for(String date: data.keySet())
        {
            dataList.add(new BarEntry(data.get(date),i++));

        }






       /* dataList.add(new BarEntry(2500, 1));
        dataList.add(new BarEntry(3500, 2));
        dataList.add(new BarEntry(2500, 3));
        dataList.add(new BarEntry(4500, 4));
*/


        BarDataSet dataSet = new BarDataSet(dataList, "Steps");

        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        ArrayList<String> xVals = new ArrayList<>();
        xVals.add("Day 1");
        xVals.add("Day 2");
        xVals.add("Day 3");
        xVals.add("Day 4");
        xVals.add("Day 5");
        xVals.add("Day 6");
        xVals.add("Day 7");
        BarData barData = new BarData(xVals,dataSet);

        barChart.setData(barData);
        barChart.setDescription("");


        dataSet.setBarSpacePercent(2);

        dataSet.setColor(ContextCompat.getColor(getContext(), R.color.activity_entry_app_bar));



        barChart.getXAxis().setDrawLabels(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setAxisLineColor(ContextCompat.getColor(getContext(),R.color.progress_theme_color));
        barChart.getAxisLeft().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.setDrawGridBackground(true);

        barChart.invalidate();


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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
