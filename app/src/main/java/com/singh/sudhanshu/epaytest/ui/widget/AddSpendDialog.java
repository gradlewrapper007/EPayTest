package com.singh.sudhanshu.epaytest.ui.widget;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.singh.sudhanshu.epaytest.R;
import com.singh.sudhanshu.epaytest.api.APIs;
import com.singh.sudhanshu.epaytest.api.ApiHandler;
import com.singh.sudhanshu.epaytest.ui.activity.AppCallback;
import com.singh.sudhanshu.epaytest.utils.Constants;
import com.singh.sudhanshu.epaytest.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sudhanshu on 08/05/17.
 */

public class AddSpendDialog extends android.support.v4.app.DialogFragment {
    private Context mContext;
    private AppCallback mCallback;

    @BindView(R.id.login_sp_team)
    AppCompatSpinner mSpinCurrency;
//
//    @BindView(R.id.dialog_spend_btn_submit)
//    AppCompatButton mBtnOk;
//
//    @BindView(R.id.dialog_spend_btn_cancel)
//    AppCompatButton mBtnCancel;

    @BindView(R.id.dialog_spend_title)
    TextView mTvTitle;

    @BindView(R.id.dialog_spend_desc_acet)
    AppCompatEditText mAcetDescription;

    @BindView(R.id.dialog_spend_amt_acet)
    AppCompatEditText mAcetAmount;

    @BindView(R.id.dialog_spend_desc_til)
    TextInputLayout mTilDescription;

    @BindView(R.id.dialog_spend_amt_til)
    TextInputLayout mTilAmt;

    String currencyCode = "";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimationZoom;
        getDialog().getWindow().setDimAmount(0.85f);
    }

    /**
     */
    public AddSpendDialog() {
    }

    /**
     * @param callback onClick calback for actions.
     */
    public void registerCallback(AppCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void onAttach(Context context) {
        this.mContext = context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_dialog_add, null);

        ButterKnife.bind(this, view);

        initViews(view);
        return view;
    }


    private void initViews(View view) {

        final String[] codes = getResources().getStringArray(R.array.codesArray);

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(((Activity) mContext),
                R.layout.row_spinner, getResources().getStringArray(R.array.currencyArray));
        mSpinCurrency.setAdapter(adapter);
        mSpinCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                currencyCode = codes[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @OnClick(R.id.dialog_spend_btn_submit)
    void success() {

        if (!validate()) {
            return;
        }

//        Bundle bn = new Bundle();
//        bn.putString("amount", mAcetAmount.getText().toString().trim());
//        bn.putString("message", mAcetDescription.getText().toString().trim());
//        bn.putString("currency", currencyCode);// TODO: 06/06/17 dynamic

        JSONObject params = new JSONObject();

        try {
            params.put(Constants.PARAM_AMOUNT, mAcetAmount.getText().toString().trim());
            params.put(Constants.PARAM_CURRENCY, currencyCode);
            params.put(Constants.PARAM_DATE, System.currentTimeMillis());
            params.put(Constants.PARAM_DESCRIPTION, mAcetDescription.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiHandler.postRequest(mContext, APIs.SPEND_URL, new AppCallback() {
            @Override
            public void onSuccess(Object data) {

                if (mCallback != null) {
                    //send bundle here
                    mCallback.onSuccess(null);
                    mCallback = null;
                }
                ToastUtil.showSmallToast(mContext, "Success!");
                AddSpendDialog.this.dismiss();
            }

            @Override
            public void onFailure(Object data) {
                ToastUtil.showSmallToast(mContext, "failed!");
            }
        }, false, params);


    }

    @OnClick(R.id.dialog_spend_btn_cancel)
    void failure() {

        if (mCallback != null) {
            mCallback.onFailure(null);
            mCallback = null;
        }
        AddSpendDialog.this.dismiss();

    }

    /**
     * inits the dialog as per the type declared.
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme()) {
            @Override
            public void onBackPressed() {
                if (mCallback != null) {
                    mCallback = null;
                }
                AddSpendDialog.this.dismiss();
            }
        };
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private boolean validate() {

        String description = mAcetDescription.getText().toString();
        String amount = mAcetAmount.getText().toString();

        if (TextUtils.isEmpty(currencyCode) || currencyCode.equalsIgnoreCase("currency")) {
            ToastUtil.showSmallToast(mContext, "Please choose a currency");
//            mTilDescription.setError("Please choose a currency");
            return false;
        }

        if (TextUtils.isEmpty(amount.trim()) || Integer.parseInt(amount) < 0) {
            mTilAmt.setError("Invalid amount.");
            return false;
        } else {
            mTilAmt.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(description.trim())) {
            mTilDescription.setError("Please provide a description");
            return false;
        } else {
            mTilDescription.setErrorEnabled(false);
        }

        return true;
    }
}

