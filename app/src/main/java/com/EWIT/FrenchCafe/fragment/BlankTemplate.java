package com.EWIT.FrenchCafe.fragment;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TimePicker;
import com.EWIT.FrenchCafe.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankTemplate extends Fragment implements TimePickerDialog.OnTimeSetListener {

    public BlankTemplate() {
        // Required empty public constructor
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wake_tracker, container, false);
    }

    @Override public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Long[] test = new Long[]{ 500L, 600L };
    }
}
