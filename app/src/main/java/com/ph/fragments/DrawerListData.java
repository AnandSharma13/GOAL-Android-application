package com.ph.fragments;

import android.support.v4.app.Fragment;


public class DrawerListData {

    private int listId;
    private String title;

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * A simple {@link Fragment} subclass.
     * Activities that contain this fragment must implement the
     * {@link OnFragmentInteractionListener} interface
     * to handle interaction events.
     * Use the {@link NutritionHistoryFragment#newInstance} factory method to
     * create an instance of this fragment.
     */
}
