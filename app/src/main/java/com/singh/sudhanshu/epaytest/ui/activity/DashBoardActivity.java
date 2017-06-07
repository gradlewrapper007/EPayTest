package com.singh.sudhanshu.epaytest.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.singh.sudhanshu.epaytest.R;
import com.singh.sudhanshu.epaytest.api.ApiHandler;
import com.singh.sudhanshu.epaytest.model.Balance;
import com.singh.sudhanshu.epaytest.ui.widget.AddSpendDialog;

import java.util.Currency;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sudhanshu on 07/06/17.
 */

public class DashBoardActivity extends AppCompatActivity {

    @BindView(R.id.dash_tv_blance)
    TextView mTvBalance;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        ApiHandler.fetchBalance(this, new AppCallback() {
            @Override
            public void onSuccess(Object data) {

                Balance balance = ((Balance) data);
                Currency currency = Currency.getInstance(balance.getCurrency());
                mTvBalance.setText(currency.getSymbol() + " " + balance.getBalance());

            }

            @Override
            public void onFailure(Object data) {

            }
        });
    }

    @OnClick(R.id.dash_btn_fab)
    void fabClick() {

        AddSpendDialog dialog = new AddSpendDialog();
        dialog.registerCallback(new AppCallback() {
            @Override
            public void onSuccess(Object data) {
                    //refreshAPI()
            }

            @Override
            public void onFailure(Object data) {

            }
        });
        dialog.show(getSupportFragmentManager(), "spend_diag");
    }

}
