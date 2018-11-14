package com.delivery.arish.arishdelivery.ui.Main.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.delivery.arish.arishdelivery.R;

public class GetLastOrdersFragment extends Fragment {

    public GetLastOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.get_last_orders_fragment, container, false);
    }
}