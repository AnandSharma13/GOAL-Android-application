package com.ph.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ph.Adapters.NutritionHistoryViewAdapter;
import com.ph.R;
import com.ph.model.DBOperations;
import com.ph.model.NutritionEntry;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NutritionHistoryDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NutritionHistoryDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NutritionHistoryDetails extends Fragment {

    private static final String ARG_PARAM1 = "week";
    @Bind(R.id.nutrition_goal_history_recycler_view)
    RecyclerView recyclerView;
    public int weekNumber;
    private OnFragmentInteractionListener mListener;
    private List<NutritionEntry> dataList;
    private DBOperations dbOperations;
    private NutritionHistoryViewAdapter nutritionHistoryViewAdapter;

    public NutritionHistoryDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NutritionHistoryDetails.
     */
    public static NutritionHistoryDetails newInstance(int week) {
        NutritionHistoryDetails fragment = new NutritionHistoryDetails();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, week);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            weekNumber = getArguments().getInt(ARG_PARAM1);
        }
        dbOperations = new DBOperations(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_nutrition_history_details, container, false);
        ButterKnife.bind(this, v);
        recyclerView.setHasFixedSize(true);




        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext()){
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
        recyclerView.requestDisallowInterceptTouchEvent(false);
        recyclerView.setLayoutManager(layoutManager);
        dataList = (List<NutritionEntry>)(List<?>) dbOperations.getGoalProgressForAWeek(weekNumber,"Nutrition");
        nutritionHistoryViewAdapter = new NutritionHistoryViewAdapter(getContext(),dataList);
        recyclerView.setAdapter(nutritionHistoryViewAdapter);
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
