package com.ph.fragments;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.RippleDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.ph.Activities.NewGoal;
import com.ph.R;
import com.ph.view.PieProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NextGoalFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NextGoalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

@Deprecated
public class NextGoalFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @Bind(R.id.fragment_next_goal_progress_bar_nutrition)
    ImageView iv;
    @Bind(R.id.fragment_next_goal_progress_bar_activity)
    ProgressBar customProgressBar;
    private Button nextGoalButton;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public NextGoalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NextGoalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NextGoalFragment newInstance(String param1, String param2) {
        NextGoalFragment fragment = new NextGoalFragment();
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
        View v = inflater.inflate(R.layout.fragment_next_goal, container, false);
        ButterKnife.bind(this, v);


        PieProgressBar p = new PieProgressBar(ContextCompat.getColor(getContext(), R.color.home_nutrition_progress_bar_background),ContextCompat.getColor(getContext(), R.color.home_nutrition_progress_outer_ring_color) );
        RippleDrawable rippledImage = new
                RippleDrawable(ColorStateList.valueOf(Color.RED), p, null);
        p.onLevelChange(10);
        iv.setImageDrawable(p);
        iv.setImageLevel(80);

        ObjectAnimator animation1 = ObjectAnimator.ofInt(iv, "progress",0, 80);
        animation1.setDuration(5000); //in milliseconds
        animation1.setInterpolator(new DecelerateInterpolator());
        animation1.start();




        //  iv.setBackground(new PieProgressBar());

        p.setLevel(80);
        customProgressBar.setProgressDrawable(p);

        nextGoalButton = (Button) v.findViewById(R.id.fragment_next_goal_btn_create_Goal);
        nextGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent newGoalIntent = new Intent(getActivity(), NewGoal.class);
                startActivity(newGoalIntent);
            }
        });
        return v;
    }


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
