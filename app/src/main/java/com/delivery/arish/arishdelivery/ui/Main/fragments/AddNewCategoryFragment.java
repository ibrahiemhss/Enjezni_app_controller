package com.delivery.arish.arishdelivery.ui.Main.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.delivery.arish.arishdelivery.R;
import com.delivery.arish.arishdelivery.data.Contract;
import com.delivery.arish.arishdelivery.mvp.presenter.MainPresenter;
import com.delivery.arish.arishdelivery.ui.add.EitDataActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddNewCategoryFragment extends Fragment implements View.OnClickListener {

    // --Commented out by Inspection (14/11/18 01:04 م):private static final String TAG = AddNewCategoryFragment.class.getSimpleName();


    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.img_show_add_ctg)
    protected ImageView mImgSowAddNew;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.rv_show_ctg)
    protected RecyclerView mRvShowAll;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.add_new_category_fragment, container, false);
        ButterKnife.bind(this, rootView);
        MainPresenter mMainPresenter = new MainPresenter(getActivity());
        mMainPresenter.getCategoriesArrayList(mRvShowAll, getLayoutInflater());

        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mImgSowAddNew.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {

            case R.id.img_show_add_ctg:
                Intent intent = new Intent(getActivity(), EitDataActivity.class);
                Bundle extras = new Bundle();
                extras.putInt(Contract.EXTRA_FRAGMENT_POSITION, 2);
                extras.putInt(Contract.EXTRA_ID_CATEGORY, 1);

                // extras.putParcelableArrayList(Contract.EXTRA_DETAILS_LIST, mDetailsModelArrayList);
                // SharedPrefManager.getInstance(this).setPrefBakePosition(position);
                // String name = mMainModelArrayList.get(position).getName();
                // extras.putString(Contract.EXTRA_BAKE_NAME, name);
                // SharedPrefManager.getInstance(this).setPrefDetailsPosition(position);
                // SharedPrefManager.getInstance(this).setPrefBakeName(name);
                intent.putExtras(extras);
                startActivity(intent);
                break;
        }

    }


}

