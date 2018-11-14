package com.delivery.arish.arishdelivery.ui.Main.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.delivery.arish.arishdelivery.R;
import com.delivery.arish.arishdelivery.mvp.View.OnItemListClickListener;
import com.delivery.arish.arishdelivery.mvp.model.DetailsModel;

import java.util.ArrayList;

import butterknife.ButterKnife;

@SuppressWarnings("unused")
public class AllOrdersAdapter extends RecyclerView.Adapter<AllOrdersAdapter.Holder> {

    // --Commented out by Inspection (03/11/18 02:01 م):private static final String TAG = DetailsAdapter.class.toString();
    private final ArrayList<DetailsModel> mDetailsModels;
    private OnItemListClickListener onItemListClickListener;
    private final LayoutInflater mLayoutInflater;


    public AllOrdersAdapter(ArrayList<DetailsModel> detailsModelArrayList, LayoutInflater inflater) {
        this.mDetailsModels = detailsModelArrayList;
        mLayoutInflater = inflater;
    }

    @SuppressWarnings("unused")
    @NonNull
    @Override
    public AllOrdersAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, @SuppressWarnings("unused") int viewType) {
        View view = mLayoutInflater.inflate(R.layout.rest_list_item, parent, false);

        return new AllOrdersAdapter.Holder(view);
    }

    @SuppressWarnings("unused")
    @Override
    public void onBindViewHolder(@NonNull AllOrdersAdapter.Holder holder, int position) {

        holder.bind(mDetailsModels.get(position), position);
    }

    @SuppressWarnings("unused")
    @Override
    public int getItemCount() {
        return mDetailsModels.size();
    }

    //create interface to goo another activity
    public void setOnItemListClickListener(OnItemListClickListener listener) {
        onItemListClickListener = listener;
    }


    @SuppressWarnings("unused")
    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final Context mContext;

        /*    @BindView(R.id.name_details_item)
            protected TextView titleView;
            @BindView(R.id.image_details_item)
            protected ImageView thumbnail;
            @BindView(R.id.LinearListContainer)
            protected LinearLayout LinearListContainer;
    */
        public Holder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @SuppressWarnings({"deprecation", "EmptyMethod"})
        public void bind(DetailsModel detailsModel, int position) {

        }


        @Override
        public void onClick(View view) {
            if (onItemListClickListener != null) {
                onItemListClickListener.onlItemClick(view, getAdapterPosition(), mDetailsModels.get(getLayoutPosition()).getName());
            }
        }
    }
}


