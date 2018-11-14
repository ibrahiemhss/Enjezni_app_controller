package com.delivery.arish.arishdelivery.mvp.presenter;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.delivery.arish.arishdelivery.R;
import com.delivery.arish.arishdelivery.data.Contract;
import com.delivery.arish.arishdelivery.data.SharedPrefManager;
import com.delivery.arish.arishdelivery.internet.BaseApiService;
import com.delivery.arish.arishdelivery.internet.UtilsApi;
import com.delivery.arish.arishdelivery.mvp.View.OnItemListClickListener;
import com.delivery.arish.arishdelivery.mvp.model.CategoryModel;
import com.delivery.arish.arishdelivery.mvp.model.RestaurantModel;
import com.delivery.arish.arishdelivery.ui.Main.adapters.CategoryAdapter;
import com.delivery.arish.arishdelivery.ui.Main.adapters.RestaurantsAdapter;
import com.delivery.arish.arishdelivery.ui.add.EitDataActivity;
import com.delivery.arish.arishdelivery.ui.log_in.LogInActivity;
import com.delivery.arish.arishdelivery.ui.log_in.ProfileActivity;
import com.delivery.arish.arishdelivery.util.JsonUtils;
import com.delivery.arish.arishdelivery.util.LangUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class MainPresenter {
    @SuppressWarnings("unused")
    private final static String TAG = MainPresenter.class.getSimpleName();
    private final Context mCtx;
    private final BaseApiService mApiService;
    private ProgressDialog mLoading;
    private RecyclerView mRCategory, mRvRestaurant;

    private LayoutInflater mLayoutInflater;
    private String mArraySort;

    private ArrayList<RestaurantModel> mRestaurantArrayList;
    private ArrayList<CategoryModel> mCategoryArrayList;

    public MainPresenter(Context context) {
        mCtx = context;
        mApiService = UtilsApi.getAPIService();
        if (mRestaurantArrayList != null) {
            mRestaurantArrayList.clear();

        }
        if (mCategoryArrayList != null) {
            mCategoryArrayList.clear();

        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressWarnings("unused")
    public void initNavigationDrawer(final Context context, final NavigationView navigationView) {

        Configuration configuration = context.getResources().getConfiguration();
        final int screenWidthDp = configuration.screenWidthDp; //The current width of the available screen space, in dp units, corresponding to screen width resource qualifier.
      /*  navigationView.post(new Runnable() {
            @Override
            public void run() {
                Resources resources = context.getResources();
                float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) (screenWidthDp*0.83), resources.getDisplayMetrics());
                DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) navigationView.getLayoutParams();
                params.width = (int) (width);
                navigationView.setLayoutParams(params);
            }
        });
*/
        Log.d(TAG, "allWidthOfScreen =" + String.valueOf(screenWidthDp) + "\n with after div =" + String.valueOf((int) (screenWidthDp * 0.83)));

        CircleImageView circleImageView = navigationView.findViewById(R.id.nav_image);
        TextView txtName = navigationView.findViewById(R.id.nav_txtname);
        TextView txtEmail = navigationView.findViewById(R.id.nav_txtemail);
        ImageView imgProfile = navigationView.findViewById(R.id.nav_profile);
        ImageView imgShare = navigationView.findViewById(R.id.nav_share);
        ImageView imgLogOut = navigationView.findViewById(R.id.nav_signout);
        getUserImage(circleImageView, txtName, txtEmail);
        imgProfile.setOnClickListener(view -> {
            // Launching the login activity
            Intent intent = new Intent(context, ProfileActivity.class);
            context.startActivity(intent);
        });

        imgShare.setOnClickListener(view -> {

        });
        imgLogOut.setOnClickListener(view -> logoutUser(context));


    }

    @SuppressWarnings("unused")
    private static void logoutUser(Context context) {

        SharedPrefManager.getInstance(context).setLoginUser(false);

        // Launching the login activity
        Intent intent = new Intent(context, LogInActivity.class);
        context.startActivity(intent);
    }


    @SuppressWarnings("unused")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getUserImage(final CircleImageView circleImageView, final TextView tvName, final TextView tvEmail) {

        mLoading = ProgressDialog.show(mCtx, null, mCtx.getResources().getString(R.string.loading_user_data), true, false);

        String emailVal
                = SharedPrefManager.
                getInstance(mCtx)
                .getEmailOfUsers();


        mApiService.getUserInfo(emailVal, LangUtil.getCurrentLanguage(mCtx))

                .enqueue(new Callback<ResponseBody>() {
                    @SuppressLint("SetTextI18n")
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {

                            try {
                                String remoteResponse = Objects.requireNonNull(response.body()).string();

                                Log.d("JSONStringNav", remoteResponse);

                                final JSONObject jsonRESULTS = new JSONObject(remoteResponse);


                                if (jsonRESULTS.optString(Contract.ERROR).equals(Contract.FALSE_VAL)) {
                                    String name = jsonRESULTS.getJSONObject(Contract.USER_COL).optString(Contract.NAME_COL);
                                    String email = jsonRESULTS.getJSONObject(Contract.USER_COL).optString(Contract.EMAIL_COL);
                                    String imgUrl = jsonRESULTS.getJSONObject(Contract.USER_COL).optString(Contract.IMAGE_COL);
                                    tvEmail.setText(mCtx.getResources().getString(R.string.email) + " :" + email);
                                    tvName.setText(mCtx.getResources().getString(R.string.name) + " :" + name);

                                    if (imgUrl != null) {

                                        Toast.makeText(mCtx,
                                                imgUrl,
                                                Toast.LENGTH_LONG).show();

                                        Glide.with(mCtx).load(imgUrl).into(circleImageView);
                                        Log.d(TAG, "JSONStringPrfImageUrl =" + imgUrl);
                                        mLoading.dismiss();


                                    } else {
                                        circleImageView.setImageResource(R.drawable.blank_profile_picture);
                                    }
                                    Toast.makeText(mCtx, jsonRESULTS.optString(Contract.ERROR_MSG), Toast.LENGTH_SHORT).show();

                                    mLoading.dismiss();
                                } else {
                                    circleImageView.setImageResource(R.drawable.blank_profile_picture);

                                    Toast.makeText(mCtx, jsonRESULTS.optString(Contract.ERROR_MSG), Toast.LENGTH_SHORT).show();
                                    mLoading.dismiss();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        mLoading.dismiss();

                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        Log.e("debugJSONS", "onFailure: ERROR > " + t.toString());
                        mLoading.dismiss();

                    }
                });
    }


    ////////////////////////////////////////////////getRestaurant  values ArrayList from server////////////////////////////////////////////////////////////////////////
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getRestaurantArrayList(final RecyclerView rvShow, final LayoutInflater layoutInflater) {


        mApiService.getRestaurant(LangUtil.getCurrentLanguage(mCtx))

                // ,SharedPrefManager.getInstance( this ).getDeviceToken())
                .enqueue(new Callback<ResponseBody>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String remoteResponse = Objects.requireNonNull(response.body()).string();

                                Log.d("JSONStringGetRestaurant", remoteResponse);

                                initialiseListRestaurant(remoteResponse, rvShow, layoutInflater);


                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        Log.e("debugJSONS", "onFailure: ERROR > " + t.toString());

                    }
                });

    }

    private void initialiseListRestaurant(String remoteResponse, RecyclerView rvShowAll, LayoutInflater layoutInflater) {
        mRvRestaurant = rvShowAll;
        mLayoutInflater = layoutInflater;
        if (mRestaurantArrayList != null) {
            mRestaurantArrayList.clear();

        }
        mArraySort = Contract.RESTAURANT_COL;

        mRestaurantArrayList = JsonUtils.parseRestaurantModel(remoteResponse, Contract.RESTAURANT_COL);
        rvShowAll.setHasFixedSize(true);
        rvShowAll.setLayoutManager(new GridLayoutManager(mCtx, 2,
                GridLayoutManager.VERTICAL, false));
        RestaurantsAdapter mRestaurantsAdapter = new RestaurantsAdapter(
                mRestaurantArrayList
                , layoutInflater);
        mRestaurantsAdapter.setOnItemListClickListener(onItemListClickListener);
        rvShowAll.setAdapter(mRestaurantsAdapter);
        mRestaurantsAdapter.notifyDataSetChanged();


    }


////////////////////////////////////////////////getCategories  value ArrayList from server////////////////////////////////////////////////////////////////////////

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getCategoriesArrayList(final RecyclerView rvShow, final LayoutInflater layoutInflater) {


        mApiService.getCategory(LangUtil.getCurrentLanguage(mCtx))

                // ,SharedPrefManager.getInstance( this ).getDeviceToken())
                .enqueue(new Callback<ResponseBody>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String remoteResponse = Objects.requireNonNull(response.body()).string();

                                Log.d("JSONStringGetCategories", remoteResponse);

                                initialiseListCategory(remoteResponse, rvShow, layoutInflater);


                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        Log.e("debugJSONS", "onFailure: ERROR > " + t.toString());

                    }
                });

    }

    private void initialiseListCategory(String remoteResponse, RecyclerView rvShowAll, LayoutInflater layoutInflater) {
        mRCategory = rvShowAll;
        mLayoutInflater = layoutInflater;
        if (mCategoryArrayList != null) {
            mCategoryArrayList.clear();

        }
        mArraySort = Contract.CATEGORY_COL;

        mCategoryArrayList =
                JsonUtils.parseCategoryModels(remoteResponse, Contract.CATEGORY_COL);


        rvShowAll.setHasFixedSize(true);
        rvShowAll.setLayoutManager(new GridLayoutManager(mCtx, 1,
                GridLayoutManager.VERTICAL, false));
        CategoryAdapter mCategoryAdapter = new CategoryAdapter(
                mCategoryArrayList
                , layoutInflater);
        mCategoryAdapter.setOnItemListClickListener(onItemListClickListener);
        rvShowAll.setAdapter(mCategoryAdapter);
        mCategoryAdapter.notifyDataSetChanged();


    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private final OnItemListClickListener onItemListClickListener = this::launchPopUpMenu;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void launchPopUpMenu(View view, final int position, String id) {

        Context wrapper = new ContextThemeWrapper(mCtx, R.style.MyPopupMenu);
        PopupMenu popup = new PopupMenu(wrapper, view);
        popup.getMenuInflater().inflate(R.menu.clipboard_popup, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(item -> {

            switch (item.getItemId()) {
                case R.id.edit:
                    Intent intent = new Intent(mCtx, EitDataActivity.class);
                    Bundle extras = new Bundle();
                    if (Objects.equals(mArraySort, Contract.RESTAURANT_COL)) {
                        extras.putInt(Contract.EXTRA_FRAGMENT_POSITION, 3);

                        extras.putString(Contract.EXTRA_ID_RESTAURANT, id);

                    } else if (Objects.equals(mArraySort, Contract.CATEGORY_COL)) {

                        extras.putInt(Contract.EXTRA_FRAGMENT_POSITION, 4);
                        extras.putString(Contract.EXTRA_ID_CATEGORY, id);


                    }


                    // extras.putParcelableArrayList(Contract.EXTRA_DETAILS_LIST, mDetailsModelArrayList);
                    // SharedPrefManager.getInstance(this).setPrefBakePosition(position);
                    // String name = mMainModelArrayList.get(position).getName();
                    // extras.putString(Contract.EXTRA_BAKE_NAME, name);
                    // SharedPrefManager.getInstance(this).setPrefDetailsPosition(position);
                    // SharedPrefManager.getInstance(this).setPrefBakeName(name);
                    intent.putExtras(extras);
                    mCtx.startActivity(intent);
                    break;
                case R.id.delete:

                    launchDeleteDialog(position, id);
                    break;


                default:
                    break;
            }

            return true;
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void launchDeleteDialog(final int position, String idVal) {


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                Objects.requireNonNull(mCtx));
        alertDialogBuilder.setTitle(mCtx.getResources().getString(R.string.delete));

        // set dialog message
        alertDialogBuilder
                .setMessage(mCtx.getResources().getString(R.string.click_delete))
                .setCancelable(false)
                .setPositiveButton(mCtx.getResources().getString(R.string.yes), (dialog, id) -> deleteSelectedItem(position, idVal))
                .setNegativeButton(mCtx.getResources().getString(R.string.no), (dialog, id) -> dialog.cancel());

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void deleteSelectedItem(int position, String id) {
        Log.d("mArraySort=", mArraySort);

        if (Objects.equals(mArraySort, Contract.RESTAURANT_COL)) {
            if (mRestaurantArrayList.size() > 0) {

                Log.d("idValueList Restaurant=", id);


                deleteRestaurant(id,
                        mRestaurantArrayList.get(position).getAr_name(),
                        mRestaurantArrayList.get(position).getEn_name());

                getRestaurantArrayList(mRvRestaurant, mLayoutInflater);

            }


        } else if (Objects.equals(mArraySort, Contract.CATEGORY_COL)) {
            if (mCategoryArrayList.size() > 0) {

                Log.d("idValueList Category=", id);
                deleteCategory(id,
                        mCategoryArrayList.get(position).getAr_name(),
                        mCategoryArrayList.get(position).getEn_name());

                getCategoriesArrayList(mRCategory, mLayoutInflater);

            }
        }
    }

    ////////////////////////////////////////////////////delete row from restaurant///////////////////////////////////////////////////////////////


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void deleteRestaurant(String id, String ar_name, String en_name) {

        mLoading = ProgressDialog.show(mCtx, null, mCtx.getResources().getString(R.string.loading_user_data), true, false);


        mApiService.deleteRestaurant(id, ar_name, en_name, LangUtil.getCurrentLanguage(mCtx))

                .enqueue(new Callback<ResponseBody>() {
                    @SuppressLint("SetTextI18n")
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {

                            try {
                                String remoteResponse = Objects.requireNonNull(response.body()).string();

                                Log.d("JSONSdeleteRestaurant", remoteResponse);

                                final JSONObject jsonRESULTS = new JSONObject(remoteResponse);


                                if (jsonRESULTS.optString(Contract.ERROR).equals(Contract.FALSE_VAL)) {


                                    Toast.makeText(mCtx, jsonRESULTS.optString(Contract.ERROR_MSG), Toast.LENGTH_SHORT).show();

                                    mLoading.dismiss();
                                } else {

                                    Toast.makeText(mCtx, jsonRESULTS.optString(Contract.ERROR_MSG), Toast.LENGTH_SHORT).show();
                                    mLoading.dismiss();

                                }
                                mLoading.dismiss();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        mLoading.dismiss();

                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        Log.e("debugJSONS", "onFailure: ERROR > " + t.toString());
                        mLoading.dismiss();

                    }
                });
    }


    ////////////////////////////////////////////////////delete row from Category///////////////////////////////////////////////////////////////


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void deleteCategory(String id, String ar_name, String en_name) {

        mLoading = ProgressDialog.show(mCtx, null, mCtx.getResources().getString(R.string.loading_user_data), true, false);


        mApiService.deleteCategory(id, ar_name, en_name, LangUtil.getCurrentLanguage(mCtx))

                .enqueue(new Callback<ResponseBody>() {
                    @SuppressLint("SetTextI18n")
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {

                            try {
                                String remoteResponse = Objects.requireNonNull(response.body()).string();

                                Log.d("JSONSdeleteCategory", remoteResponse);

                                final JSONObject jsonRESULTS = new JSONObject(remoteResponse);


                                if (jsonRESULTS.optString(Contract.ERROR).equals(Contract.FALSE_VAL)) {


                                    Toast.makeText(mCtx, jsonRESULTS.optString(Contract.ERROR_MSG), Toast.LENGTH_SHORT).show();

                                    mLoading.dismiss();
                                } else {

                                    Toast.makeText(mCtx, jsonRESULTS.optString(Contract.ERROR_MSG), Toast.LENGTH_SHORT).show();
                                    mLoading.dismiss();

                                }
                                mLoading.dismiss();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        mLoading.dismiss();

                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        Log.e("debugJSONS", "onFailure: ERROR > " + t.toString());
                        mLoading.dismiss();

                    }
                });
    }


}
