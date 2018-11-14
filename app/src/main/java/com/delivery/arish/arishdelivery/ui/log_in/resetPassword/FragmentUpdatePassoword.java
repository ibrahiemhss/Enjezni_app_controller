package com.delivery.arish.arishdelivery.ui.log_in.resetPassword;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.delivery.arish.arishdelivery.R;
import com.delivery.arish.arishdelivery.data.SharedPrefManager;
import com.delivery.arish.arishdelivery.mvp.presenter.ResetPasswordPresenter;
import com.delivery.arish.arishdelivery.util.EditTextUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("ALL")
public class FragmentUpdatePassoword extends Fragment implements View.OnClickListener {

    @BindView(R.id.et_code)
    protected EditText mEtxtGetCode;
    @BindView(R.id.et_update_password)
    protected EditText mEtxtGetNewPass;
    @BindView(R.id.et_confirm_password)
    protected EditText mEtxtConfirmNewPass;
    @BindView(R.id.btn_update_password)
    protected Button mBtnUpdateNewPass;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ubdate_password, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBtnUpdateNewPass.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_update_password:
                ResetPasswordPresenter resetPasswordPresenter = new ResetPasswordPresenter(getActivity());
                if (EditTextUtil.passCases(mEtxtGetNewPass.getText().toString()) == 6) {
                    mEtxtGetNewPass.setError(getResources().getString(R.string.password_error));
                    return;

                }
                if (!mEtxtGetNewPass.getText().toString().equals(mEtxtConfirmNewPass.getText().toString())) {
                    mEtxtGetNewPass.setError(getResources().getString(R.string.confirm_password_error));
                    mEtxtConfirmNewPass.setError(getResources().getString(R.string.confirm_password_error));

                    return;

                }
                String email = SharedPrefManager.getInstance(getActivity()).getEmailOfUsers();
                resetPasswordPresenter.requestUpdateWithCode(
                        email,
                        mEtxtGetNewPass.getText().toString(),
                        mEtxtGetCode.getText().toString());
        }

    }
}
