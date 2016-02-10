package com.ph.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.ph.R;
import com.ph.Utils.DateOperations;
import com.ph.model.DBOperations;

import java.util.HashMap;

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
    private DBOperations dbOperations;
    private DateOperations dateOperations;
    private GraphView barGraph;

    private OnFragmentInteractionListener mListener;

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
        int stepsCount = dbOperations.getStepsCountForThisWeek();
        stepsWeek = (TextView) v.findViewById(R.id.progress_steps_week_mine);
        stepsWeek.setText(String.valueOf(stepsCount));
        HashMap<String, Integer> data = dbOperations.getStepsForThisWeek();

        DataPoint[] dataPointArray = new DataPoint[data.size()];
        int i=0;
        String[] labels = new String[data.size()];
        for(String date: data.keySet())
        {


                dataPointArray[i] = new DataPoint(i,data.get(date));
                labels[i] = date;
                i++;

        }


        barGraph = (GraphView) v.findViewById(R.id.progress_steps_week_bar);
        if(dataPointArray.length>0) {
            BarGraphSeries<DataPoint> series = new BarGraphSeries<>(dataPointArray);

            series.setDrawValuesOnTop(true);
            series.setSpacing(5);


            barGraph.addSeries(series);
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(barGraph);
            staticLabelsFormatter.setHorizontalLabels(labels);

            barGraph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            //barGraph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getContext()));
           // barGraph.getGridLabelRenderer().setNumHorizontalLabels(data.size());
        }




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
