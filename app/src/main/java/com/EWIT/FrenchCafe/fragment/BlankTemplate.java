package com.EWIT.FrenchCafe.fragment;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;
import android.widget.Toast;
import com.EWIT.FrenchCafe.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankTemplate extends Fragment implements TimePickerDialog.OnTimeSetListener {

    public BlankTemplate() {
        // Required empty public constructor
    }

    public static BlankTemplate newInstance() {
        Bundle args = new Bundle();
        BlankTemplate fragment = new BlankTemplate();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wake_tracker, container, false);
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getContext(), "Hello 6", Toast.LENGTH_SHORT).show();
    }

    @Override public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Long[] test = new Long[] { 500L, 600L };
    }
}
