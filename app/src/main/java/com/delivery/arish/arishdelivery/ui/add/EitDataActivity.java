package com.delivery.arish.arishdelivery.ui.add;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.delivery.arish.arishdelivery.R;
import com.delivery.arish.arishdelivery.base.BaseActivity;
import com.delivery.arish.arishdelivery.data.Contract;
import com.delivery.arish.arishdelivery.mvp.presenter.AddEditPresenter;
import com.delivery.arish.arishdelivery.mvp.presenter.UpdateEditPresenter;
import com.delivery.arish.arishdelivery.ui.Main.fragments.AddNewCategoryFragment;
import com.delivery.arish.arishdelivery.util.EditTextUtil;
import com.delivery.arish.arishdelivery.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;

public class EitDataActivity extends BaseActivity implements View.OnClickListener {

    private static final int PICK_IMAGE_REQUEST = 5;
    private static final String TAG = AddNewCategoryFragment.class.getSimpleName();

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.lin_add)
    protected LinearLayout mLinearLayout;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.img_add)
    protected ImageView mImgAddValue;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.etx_ar_name)
    protected EditText mEtxtAddAr;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.etx_en_name)
    protected EditText mEtxtAddEn;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.btn_add_new)
    protected Button mBtnAddNew;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.txt_case_edit)
    protected TextView mTxtCase;

    private String mPart_image;
    private File mActualImageFile;
    private int mFragmentPosition;
    private String mCategoryId, mRestaurantId;

    private UpdateEditPresenter mUpdateEditPresenter;

    private AddEditPresenter mAddPresenter;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);

        Bundle extras = getIntent().getExtras();
        mAddPresenter = new AddEditPresenter(this);
        mUpdateEditPresenter = new UpdateEditPresenter(this);

        assert extras != null;
        if (extras.containsKey(Contract.EXTRA_FRAGMENT_POSITION)) {
            if (extras.containsKey(Contract.EXTRA_ID_RESTAURANT) || extras.containsKey(Contract.EXTRA_ID_CATEGORY)) {
                mCategoryId = extras.getString(Contract.EXTRA_ID_CATEGORY);
                mRestaurantId = extras.getString(Contract.EXTRA_ID_RESTAURANT);

            }

            mFragmentPosition = extras.getInt(Contract.EXTRA_FRAGMENT_POSITION, 0);
            Toast.makeText(this, String.valueOf(mFragmentPosition), Toast.LENGTH_LONG).show();
            switch (mFragmentPosition) {
                case 1:

                    mTxtCase.setText(R.string.click_add_image);
                    break;
                case 2:
                    mTxtCase.setText(R.string.click_add_image);

                    break;
                case 3:
                    mTxtCase.setText(R.string.click_edit_image);

                    break;
                case 4:
                    mTxtCase.setText(R.string.click_edit_image);

                    break;
            }


        }

    }

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_edit;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void setListener() {
        mImgAddValue.setOnClickListener(this);
        mBtnAddNew.setOnClickListener(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.img_add:
                chooseImage();
                break;

            case R.id.btn_add_new:

                if (EditTextUtil.isNameCase(mEtxtAddAr.getText().toString()) == 2) {
                    mEtxtAddAr.setError(getResources().getString(R.string.name_error_char));
                    return;

                }
                if (EditTextUtil.isNameCase(mEtxtAddEn.getText().toString()) == 2) {
                    mEtxtAddEn.setError(getResources().getString(R.string.name_error_char));
                    return;

                }
                if (mFragmentPosition == 1) {
                    mAddPresenter.requestAddNewRestaurant(
                            mPart_image,
                            mActualImageFile,
                            mEtxtAddAr.getText().toString(),
                            mEtxtAddEn.getText().toString()
                    );
                }
                if (mFragmentPosition == 2) {
                    mAddPresenter.requestAddNewCategory(
                            mPart_image,
                            mActualImageFile,
                            mEtxtAddAr.getText().toString(),
                            mEtxtAddEn.getText().toString()
                    );
                }
                if (mFragmentPosition == 3) {
                    mUpdateEditPresenter.requestUpdateRestaurant(
                            mRestaurantId,
                            mPart_image,
                            mActualImageFile,
                            mEtxtAddAr.getText().toString(),
                            mEtxtAddEn.getText().toString()
                    );
                }
                if (mFragmentPosition == 4) {
                    mUpdateEditPresenter.requestUpdateCategory(
                            mCategoryId,
                            mPart_image,
                            mActualImageFile,
                            mEtxtAddAr.getText().toString(),
                            mEtxtAddEn.getText().toString()
                    );
                }
                break;
        }
    }


    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
        mTxtCase.setText(R.string.sellected_image);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data == null) {
                return;
            }
            try {
                Uri imageData = data.getData();
                String[] imageProjection = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(Objects.requireNonNull(imageData), imageProjection, null, null, null);

                if (cursor != null) {
                    cursor.moveToFirst();
                    int indexImage = cursor.getColumnIndex(imageProjection[0]);
                    mPart_image = cursor.getString(indexImage);

                    if (mPart_image != null) {
                        mActualImageFile = FileUtil.from(this, data.getData());
                        // Glide.with(this).load(mActualImageFile).into(mImgAddValueOfRest);
                        mImgAddValue.setImageBitmap(BitmapFactory.decodeFile(mActualImageFile.getAbsolutePath()));
                        Log.e(TAG, "mPartImageCategoryFragment = " + mPart_image);

                        cursor.close();

                        // mCircleImageViewHolder.setImageBitmap(BitmapFactory.decodeFile(mActualImageFile.getAbsolutePath()));
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
