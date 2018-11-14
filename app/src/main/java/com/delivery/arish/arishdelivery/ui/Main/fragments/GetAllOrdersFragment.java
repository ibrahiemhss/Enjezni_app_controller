package com.delivery.arish.arishdelivery.ui.Main.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.delivery.arish.arishdelivery.R;
import com.delivery.arish.arishdelivery.ui.Main.adapters.AllOrdersAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GetAllOrdersFragment extends Fragment {

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.rv_all_orders)
    protected RecyclerView mRvShowAll;

    // private ArrayList<DetailsModel> mDetailsModelArrayList;
    @SuppressWarnings("unused")
    private AllOrdersAdapter mAllOrdersAdapter;

    public GetAllOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.get_all_orders_fragment, container, false);
        ButterKnife.bind(this, rootView);

        //   mDetailsModelArrayList = MainPresenter.getRestaurantsModel();

        return rootView;
    }


}