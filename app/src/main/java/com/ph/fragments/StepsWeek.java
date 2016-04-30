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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.ph.R;
import com.ph.Utils.DateOperations;
import com.ph.Utils.StepsCountClick;
import com.ph.model.DBOperations;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StepsWeek.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StepsWeek#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepsWeek extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView stepsWeek;


    @Bind(R.id.progress_steps_week_mine_text)
     TextView stepsWeekText;

    @Bind(R.id.progress_steps_week_others)
     TextView stepsOthersAvg;

    @Bind(R.id.progress_steps_week_others_text)
     TextView stepsOthersAvgText;

    @Bind(R.id.steps_week_line_chart)
    LineChart lineChart;
    private DBOperations dbOperations;
    private DateOperations dateOperations;


    private OnFragmentInteractionListener mListener;
    private RelativeLayout stepsLayout;

    public StepsWeek() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StepsWeek.
     */
    // TODO: Rename and change types and number of parameters
    public static StepsWeek newInstance(String param1, String param2) {
        StepsWeek fragment = new StepsWeek();
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
        View v = inflater.inflate(R.layout.fragment_steps_week, container, false);
        ButterKnife.bind(this,v);


        int stepsCount = dbOperations.getStepsCountForThisWeek();
        stepsWeek = (TextView) v.findViewById(R.id.progress_steps_week_mine);

        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Eurostile.ttf");
        Typeface custom_font2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/HelveticaNeue.ttf");
        stepsWeek.setTypeface(custom_font);

        
        stepsWeekText.setTypeface(custom_font2);


        stepsOthersAvg.setTypeface(custom_font);
        stepsOthersAvgText.setTypeface(custom_font2);


        stepsLayout = (RelativeLayout) v.findViewById(R.id.steps_week_relative_layout);

        stepsLayout.setOnClickListener(new StepsCountClick(getActivity(), new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                stepsWeek.setText(String.valueOf(dbOperations.getStepsCountForToday()));
            }
        }));

        stepsWeek.setText(String.valueOf(stepsCount));
        ArrayList<Integer> data = dbOperations.getStepsCountByWeek();


        ArrayList<Entry> dataList = new ArrayList<>();

        for(int k=0;k<data.size();k++)
        {
            dataList.add(new Entry(data.get(k), k));
        }
        /*dataList.add(new Entry(1500, 0));
        dataList.add(new Entry(2500, 1));
        dataList.add(new Entry(3500, 2));
        dataList.add(new Entry(4500, 3));
        dataList.add(new Entry(3500,4));
        dataList.add(new Entry(2500, 5));
        dataList.add(new Entry(1500, 6));
*/

        LineDataSet dataSet = new LineDataSet(dataList,"Steps");
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        //TODO: Remove hard coding here.
        ArrayList<String> xVals = new ArrayList<>();
        xVals.add("Week 1");
        xVals.add("Week 2");
        xVals.add("Week 3");
        xVals.add("Week 4");
        xVals.add("Week 5");
        xVals.add("Week 6");
        xVals.add("Week 7");
        xVals.add("Week 8");
        xVals.add("Week 9");
        xVals.add("Week 10");
        xVals.add("Week 11");
        xVals.add("Week 12");
        LineData lineData = new LineData(xVals,dataSet);

        lineChart.setData(lineData);
        lineChart.setDescription("");


        dataSet.setColor(ContextCompat.getColor(getContext(), R.color.activity_entry_app_bar));



        dataSet.setCircleColor(ContextCompat.getColor(getContext(), R.color.progress_theme_color));

        dataSet.setFillColor(ContextCompat.getColor(getContext(), R.color.activity_entry_app_bar));

        dataSet.setDrawFilled(true);


        lineChart.getXAxis().setDrawLabels(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getXAxis().setAxisLineColor(ContextCompat.getColor(getContext(), R.color.progress_theme_color));
        lineChart.getAxisLeft().setEnabled(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.setDrawGridBackground(true);
        
        lineChart.invalidate();










        return v;
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
