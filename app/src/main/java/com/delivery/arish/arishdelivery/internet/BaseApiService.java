package com.delivery.arish.arishdelivery.internet;


import com.delivery.arish.arishdelivery.data.Contract;
import com.delivery.arish.arishdelivery.internet.model.ResnponseApiModelAddData;
import com.delivery.arish.arishdelivery.internet.model.ResponseApiModelLogIn;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/**
 * Class uses Queries  the first part of the Internet address
 * *named BASE_URL that found in {@linkplain com.delivery.arish.arishdelivery.data.Contract}
 * *to go to {@linkplain com.delivery.arish.arishdelivery.internet.RetrofitClient}
 * *here tare these Urls here to build final second part of urls here for every request of url wanted
 */
public interface BaseApiService {

    @FormUrlEncoded
    @POST(Contract.LOGIN_URL)
    Call<ResponseBody> loginRequest(
            @Field(Contract.EMAIL_COL) String email,
            @Field(Contract.PASSWORD_COL) String password,
            @Field(Contract.TOKEN_COL) String token,
            @Field(Contract.LANG_COL) String lang);


    @FormUrlEncoded
    @POST(Contract.REGISTER_URL)
    Call<ResponseBody> registerRequest(
            @Field(Contract.NAME_COL) String name,
            @Field(Contract.EMAIL_COL) String email,
            @Field(Contract.PASSWORD_COL) String password,
            @Field(Contract.PHONE_COL) String phone,
            @Field(Contract.LANG_COL) String lang);


    @Multipart
    @POST(Contract.REGISTER_WITH_IMAGE_URL)
    Call<ResponseApiModelLogIn> registerRequestWithImage(
            @PartMap() Map<String, RequestBody> partMap,
            @Part MultipartBody.Part image);


    @FormUrlEncoded
    @POST(Contract.FORGET_PASSWORD_URL)
    Call<ResponseBody> sendCodeToMail(
            @Field(Contract.EMAIL_COL) String email,
            @Field(Contract.LANG_COL) String lang);

    @FormUrlEncoded
    @POST(Contract.UPDATE_PASSWORD_URL)
    Call<ResponseBody> updatePass(
            @Field(Contract.EMAIL_COL) String email,
            @Field(Contract.PASSWORD_COL) String password,
            @Field(Contract.CODE_COL) String phone,
            @Field(Contract.LANG_COL) String lang);

    @FormUrlEncoded
    @POST(Contract.PROFILE_INFO_URL)
    Call<ResponseBody> getUserInfo(
            @Field(Contract.EMAIL_COL) String email,
            @Field(Contract.LANG_COL) String lang);

    @FormUrlEncoded
    @POST(Contract.UPDATE_INFO_URL)
    Call<ResponseBody> updateUserInfo(
            @Field(Contract.EMAIL_COL) String email,
            @Field(Contract.PASSWORD_COL) String password,
            @Field(Contract.NEW_NAME_COL) String new_name,
            @Field(Contract.NEW_EMAIL_COL) String new_mail,
            @Field(Contract.NEW_PHONE_COL) String new_phone,
            @Field(Contract.LANG_COL) String lang);

    @Multipart
    @POST(Contract.UPDATE_INFO_WITH_IMAGE_URL)
    Call<ResponseApiModelLogIn> updateUserInfoWithImg(
            @PartMap() Map<String, RequestBody> partMap,
            @Part MultipartBody.Part image);


    @Multipart
    @POST(Contract.ADD_RESTAURANT_URL)
    Call<ResnponseApiModelAddData> addNewRest(
            @PartMap() Map<String, RequestBody> partMap,
            @Part MultipartBody.Part image);

    @Multipart
    @POST(Contract.ADD_CATEGORY_URL)
    Call<ResnponseApiModelAddData> addNewCTG(
            @PartMap() Map<String, RequestBody> partMap,
            @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST(Contract.GET_RESTAURANTS_URL)
    Call<ResponseBody> getRestaurant(
            @Field(Contract.LANG_COL) String lang);

    @FormUrlEncoded
    @POST(Contract.GET_CATEGORIES_URL)
    Call<ResponseBody> getCategory(
            @Field(Contract.LANG_COL) String lang);


    @Multipart
    @POST(Contract.UPDATE_CATEGORY_URL)
    Call<ResnponseApiModelAddData> updateCategory(
            @PartMap() Map<String, RequestBody> partMap,
            @Part MultipartBody.Part image);

    @Multipart
    @POST(Contract.UPDATE_RESTAURANT_URL)
    Call<ResnponseApiModelAddData> updateRestaurant(
            @PartMap() Map<String, RequestBody> partMap,
            @Part MultipartBody.Part image);


    @FormUrlEncoded
    @POST(Contract.DELETE_CATEGORY_URL)
    Call<ResponseBody> deleteCategory(
            @Field(Contract.ID_COL) String id,
            @Field(Contract.AR_NAME_COL) String ar_name,
            @Field(Contract.EN_NAME_COL) String en_name,
            @Field(Contract.LANG_COL) String lang);

    @FormUrlEncoded
    @POST(Contract.DELETE_RESTAURANT_URL)
    Call<ResponseBody> deleteRestaurant(
            @Field(Contract.ID_COL) String id,
            @Field(Contract.AR_NAME_COL) String ar_name,
            @Field(Contract.EN_NAME_COL) String en_name,
            @Field(Contract.LANG_COL) String lang);

}
