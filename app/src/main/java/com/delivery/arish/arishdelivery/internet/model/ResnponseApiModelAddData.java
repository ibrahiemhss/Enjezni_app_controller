package com.delivery.arish.arishdelivery.internet.model;

import com.delivery.arish.arishdelivery.data.Contract;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ResnponseApiModelAddData {

    @SerializedName(Contract.ERROR)
    private
    String error;

    @SerializedName(Contract.ERROR_MSG)
    private
    String error_msg;
    @SerializedName(Contract.SUCCESS_MSG)
    private
    String success_msg;

    @SerializedName(Contract.AR_NAME_COL)
    private
    String ar_name;

    @SerializedName(Contract.EN_NAME_COL)
    private

    String en_name;

    @SerializedName(Contract.IMAGE_COL)
    private
    String img_url;

    public String getError() {
        return error;
    }

    public String getError_msg() {
        return error_msg;
    }

    public String getSuccess_msg() {
        return success_msg;
    }

    public String getAr_name() {
        return ar_name;
    }

    public String getEn_name() {
        return en_name;
    }

    public String getImg_url() {
        return img_url;
    }


}
