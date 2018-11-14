package com.delivery.arish.arishdelivery.mvp.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.delivery.arish.arishdelivery.R;
import com.delivery.arish.arishdelivery.data.Contract;
import com.delivery.arish.arishdelivery.internet.BaseApiService;
import com.delivery.arish.arishdelivery.internet.UtilsApi;
import com.delivery.arish.arishdelivery.internet.model.ResnponseApiModelAddData;
import com.delivery.arish.arishdelivery.ui.Main.MainActivity;
import com.delivery.arish.arishdelivery.util.LangUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateEditPresenter {

    private final Context mCtx;
    private final BaseApiService mApiService;
    private ProgressDialog mLoading;
    private File mImagefile;
    private MultipartBody.Part mPart;
    private static final String TAG = RegisterPresenter.class.getSimpleName();

    public UpdateEditPresenter(Context context) {
        mCtx = context;
        mApiService = UtilsApi.getAPIService();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
////////////////////////////////////////////////add Restaurant  value to database////////////////////////////////////////////////////////////////////////
    public void requestUpdateRestaurant(
            String id_val,
            String old_part_img,//pass path the file of image as string from AddNewRestaurantFragment
            File myfile,//pass file  from AddNewRestaurantFragment
            String ar_nameval,//pass arabic name come from editText from AddNewRestaurantFragment
            String en_nameval//pass english name come from editText from AddNewRestaurantFragment

    ) {
        //initialize ProgressDialog status message
        mLoading = ProgressDialog.show(mCtx, null, mCtx.getResources().getString(R.string.creating_new), true, false);

        if (old_part_img != null) {
            mImagefile = new File(old_part_img);//initialize new file as file that come from string old_part_img this string come from parameter


            if (myfile != null) {/*all codes below inside this if statement
                              to use file come from parameters to compress the size of
                              the file of our image before uploading to server*/
                try {
                    try {
                        mImagefile = new Compressor(mCtx)
                                .setMaxWidth(640)
                                .setMaxHeight(480)
                                .setQuality(75)
                                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_PICTURES).getAbsolutePath())
                                .compressToFile(myfile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

        }


        if (mImagefile != null) {
            /*initialize new request created fom device files */
            RequestBody reqBody = RequestBody.create(MediaType.parse(Contract.MULTIPART_FILE_PATH), mImagefile);
            /*make multipart request with our image file and our initialized requestBody*/
            mPart = MultipartBody.Part.createFormData(Contract.PIC_TO_LOAD, mImagefile.getName(), reqBody);

        } else {
            /*initialize new request created fom device files */
            RequestBody reqBody = RequestBody.create(MediaType.parse(Contract.MULTIPART_FILE_PATH), "");
            /*make multipart request with our image file and our initialized requestBody*/
            mPart = MultipartBody.Part.createFormData("", "", reqBody);

        }

        RequestBody id = createPartFromString(id_val);/*initialize new request created fom string value of id */
        RequestBody ar_name = createPartFromString(ar_nameval);/*initialize new request created fom string value of arabic name */
        RequestBody en_name = createPartFromString(en_nameval);/*initialize new request created fom string value of english name*/
        RequestBody lang = createPartFromString(LangUtil.getCurrentLanguage(mCtx));/*initialize new request created fom string value of language device*/

        //create new HashMap with string and RequestBody
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put(Contract.ID_COL, id);//add RequestBody id to initialized HashMap
        map.put(Contract.AR_NAME_COL, ar_name);//add RequestBody arabic name to initialized HashMap
        map.put(Contract.EN_NAME_COL, en_name);//add RequestBody english name to initialized HashMap
        map.put(Contract.LANG_COL, lang);//add RequestBody lang to initialized HashMap

       /*call ApiService class to make Multipart request with uploadImage method
        pass two params first is our  HashMap that carry all
         requests body with strings and second params is MultipartBody that carry image file value * */
        Call<ResnponseApiModelAddData> upload = mApiService.updateRestaurant(map, mPart);
        /*call ApiService class to make request with registerRequest method to register new user with no image*/

        upload.enqueue(new Callback<ResnponseApiModelAddData>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NonNull Call<ResnponseApiModelAddData> call, @NonNull final Response<ResnponseApiModelAddData> response) {

                Log.d(TAG, "myjson = : " +
                        Objects.requireNonNull(response.body()).toString());
                //get value from json value if value is false all things good and successful request no error
                if (Objects.requireNonNull(response.body()).getError().equals(Contract.FALSE_VAL)) {
                    //   mLoading.setMessage(response.body().getError_msg());


                    Log.d(TAG, "server_message : " + Objects.requireNonNull(response.body()).getError_msg()
                            + "\n" + Objects.requireNonNull(response.body()).getSuccess_msg());

                    /*if JsonObject get Success message will do this code*/
                    if (Objects.requireNonNull(response.body()).getSuccess_msg().equals(Contract.SUCCESS_MSG_VALUE)) {
                        Toast.makeText(mCtx,
                                Objects.requireNonNull(response.body()).getError_msg()
                                , Toast.LENGTH_SHORT).show();
                        mCtx.startActivity(new Intent(mCtx, MainActivity.class));
                    }
                    mLoading.dismiss();

                    //get value from json value if value is true there are error that come inside error_message JsonObject

                } else if (Objects.requireNonNull(response.body()).getError().equals("true")) {
                    Toast.makeText(mCtx,
                            Objects.requireNonNull(response.body()).getError_msg()
                            , Toast.LENGTH_SHORT).show();
                    mLoading.dismiss();


                }
            }

            @Override
            public void onFailure(@NonNull Call<ResnponseApiModelAddData> call, @NonNull Throwable t) {
                Log.d("RETRO", "ON FAILURE : " + t.getMessage());
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
////////////////////////////////////////////////add Category  value to database////////////////////////////////////////////////////////////////////////
    public void requestUpdateCategory(
            String id_val,
            String old_part_img,//pass path the file of image as string from AddNewCategoryFragment
            File myfile,//pass file  from AddNewRestaurantFragment
            String ar_nameval,//pass arabic name come from editText from AddNewCategoryFragment
            String en_nameval//pass english name come from editText from AddNewCategoryFragment

    ) {
        //initialize ProgressDialog status message
        mLoading = ProgressDialog.show(mCtx, null, mCtx.getResources().getString(R.string.creating_new), true, false);

        if (old_part_img != null) {
            mImagefile = new File(old_part_img);//initialize new file as file that come from string old_part_img this string come from parameter


            if (myfile != null) {/*all codes below inside this if statement
                              to use file come from parameters to compress the size of
                              the file of our image before uploading to server*/
                try {
                    try {
                        mImagefile = new Compressor(mCtx)
                                .setMaxWidth(640)
                                .setMaxHeight(480)
                                .setQuality(75)
                                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_PICTURES).getAbsolutePath())
                                .compressToFile(myfile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

        }


        if (mImagefile != null) {
            /*initialize new request created fom device files */
            RequestBody reqBody = RequestBody.create(MediaType.parse(Contract.MULTIPART_FILE_PATH), mImagefile);
            /*make multipart request with our image file and our initialized requestBody*/
            mPart = MultipartBody.Part.createFormData(Contract.PIC_TO_LOAD, mImagefile.getName(), reqBody);

        } else {
            /*initialize new request created fom device files */
            RequestBody reqBody = RequestBody.create(MediaType.parse(Contract.MULTIPART_FILE_PATH), "");
            /*make multipart request with our image file and our initialized requestBody*/
            mPart = MultipartBody.Part.createFormData("", "", reqBody);

        }


///////////////////////////////////////////////////////////////////////////
        RequestBody id = createPartFromString(id_val);/*initialize new request created fom string value of id */
        RequestBody ar_name = createPartFromString(ar_nameval);/*initialize new request created fom string value of arabic name */
        RequestBody en_name = createPartFromString(en_nameval);/*initialize new request created fom string value of english name*/
        RequestBody lang = createPartFromString(LangUtil.getCurrentLanguage(mCtx));/*initialize new request created fom string value of language device*/

        //create new HashMap with string and RequestBody
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put(Contract.ID_COL, id);//add RequestBody arabic name to initialized HashMap
        map.put(Contract.AR_NAME_COL, ar_name);//add RequestBody arabic name to initialized HashMap
        map.put(Contract.EN_NAME_COL, en_name);//add RequestBody english name to initialized HashMap
        map.put(Contract.LANG_COL, lang);//add RequestBody lang to initialized HashMap

       /*call ApiService class to make Multipart request with uploadImage method
        pass two params first is our  HashMap that carry all
         requests body with strings and second params is MultipartBody that carry image file value * */
        Call<ResnponseApiModelAddData> upload = mApiService.updateCategory(map, mPart);
        /*call ApiService class to make request with registerRequest method to register new user with no image*/

        upload.enqueue(new Callback<ResnponseApiModelAddData>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NonNull Call<ResnponseApiModelAddData> call, @NonNull final Response<ResnponseApiModelAddData> response) {

                Log.d(TAG, "myjson = : " +
                        Objects.requireNonNull(response.body()).toString());
                //get value from json value if value is false all things good and successful request no error
                if (Objects.requireNonNull(response.body()).getError().equals(Contract.FALSE_VAL)) {
                    //   mLoading.setMessage(response.body().getError_msg());


                    Log.d(TAG, "server_message : " + Objects.requireNonNull(response.body()).getError_msg()
                            + "\n" + Objects.requireNonNull(response.body()).getSuccess_msg());

                    /*if JsonObject get Success message will do this code*/
                    if (Objects.requireNonNull(response.body()).getSuccess_msg().equals(Contract.SUCCESS_MSG_VALUE)) {
                        Toast.makeText(mCtx,
                                Objects.requireNonNull(response.body()).getError_msg()
                                , Toast.LENGTH_SHORT).show();
                        mCtx.startActivity(new Intent(mCtx, MainActivity.class));
                    }
                    mLoading.dismiss();

                    //get value from json value if value is true there are error that come inside error_message JsonObject

                } else if (Objects.requireNonNull(response.body()).getError().equals("true")) {
                    Toast.makeText(mCtx,
                            Objects.requireNonNull(response.body()).getError_msg()
                            , Toast.LENGTH_SHORT).show();
                    mLoading.dismiss();


                }
            }

            @Override
            public void onFailure(@NonNull Call<ResnponseApiModelAddData> call, @NonNull Throwable t) {
                Log.d("RETRO", "ON FAILURE : " + t.getMessage());
            }
        });

    }

    private RequestBody createPartFromString(String partString) {
        return RequestBody.create(MultipartBody.FORM, partString);
    }
}
