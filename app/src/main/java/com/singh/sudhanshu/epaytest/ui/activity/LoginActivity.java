package com.singh.sudhanshu.epaytest.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;

import com.singh.sudhanshu.epaytest.R;
import com.singh.sudhanshu.epaytest.api.ApiHandler;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sudhanshu on 6/7/2017.
 */

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_id_acet)
    AppCompatEditText mAcetLogin;
    @BindView(R.id.login_pass_acet)
    AppCompatEditText mAcetPassw;
    @BindView(R.id.login_btn_fab)
    FloatingActionButton mBtnFab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mBtnFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAPI();
            }
        });
    }

    private void callAPI() {
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
