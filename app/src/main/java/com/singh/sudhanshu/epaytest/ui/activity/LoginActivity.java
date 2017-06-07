package com.singh.sudhanshu.epaytest.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;

import com.singh.sudhanshu.epaytest.R;
import com.singh.sudhanshu.epaytest.api.ApiHandler;
import com.singh.sudhanshu.epaytest.utils.Constants;
import com.singh.sudhanshu.epaytest.utils.PreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sudhanshu on 6/7/2017.
 */

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_id_acet)
    AppCompatEditText mAcetLogin;
//    @BindView(R.id.login_pass_acet)
//    AppCompatEditText mAcetPassw;
//    @BindView(R.id.login_btn_fab)
//    FloatingActionButton mBtnFab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!TextUtils.isEmpty(PreferenceUtil.getInstance().getStringValue(Constants.PREF_TOKEN, null))) {

            startActivity(new Intent(this, DashBoardActivity.class));
            finish();

        } else {
            setContentView(R.layout.activity_login);
            ButterKnife.bind(this);
            getSupportActionBar().hide();
        }

    }

    @OnClick(R.id.login_btn_fab)
    void callAPI() {
        ApiHandler.fetchTokenAndSaveIfNull(this, new AppCallback() {
            @Override
            public void onSuccess(Object data) {
                //Move forward
            }

            @Override
            public void onFailure(Object data) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
