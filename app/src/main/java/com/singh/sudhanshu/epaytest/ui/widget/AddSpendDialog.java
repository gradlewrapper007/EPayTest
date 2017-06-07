package com.singh.sudhanshu.epaytest.ui.widget;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.singh.sudhanshu.epaytest.R;
import com.singh.sudhanshu.epaytest.ui.activity.AppCallback;

/**
 * Created by Sudhanshu on 08/05/17.
 */

public class AddSpendDialog extends android.support.v4.app.DialogFragment {
    private Context mContext;
    private AppCallback mCallback;
    private AppCompatButton mBtnOk, mBtnCancel;
    private TextView mTvTitle;
    private TextInputLayout mTilDescription, mTilAmt;
    private AppCompatEditText mAcetDescription, mAcetAmount;

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

        initViews(view);
        return view;
    }


    private void initViews(View view) {

        mBtnOk = (AppCompatButton) view.findViewById(R.id.dialog_spend_btn_submit);
        mBtnCancel = (AppCompatButton) view.findViewById(R.id.dialog_spend_btn_cancel);
        mTvTitle = (TextView) view.findViewById(R.id.dialog_spend_title);
        mAcetDescription = (AppCompatEditText) view.findViewById(R.id.dialog_spend_desc_acet);
        mTilDescription = (TextInputLayout) view.findViewById(R.id.dialog_spend_desc_til);
        mAcetAmount = (AppCompatEditText) view.findViewById(R.id.dialog_spend_amt_acet);
        mTilAmt = (TextInputLayout) view.findViewById(R.id.dialog_spend_amt_til);

        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!validate()) {
                    return;
                }

                Bundle bn = new Bundle();
                bn.putString("amount", mAcetAmount.getText().toString().trim());
                bn.putString("message", mAcetDescription.getText().toString().trim());
                bn.putString("currency", "");// TODO: 06/06/17 dynamic

                if (mCallback != null) {
                    //send bundle here
                    mCallback.onSuccess(bn);
                    mCallback = null;
                }

            }
        });

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback != null) {
                    mCallback.onFailure(null);
                    mCallback = null;
                }
                AddSpendDialog.this.dismiss();
            }
        });

//        setTheme(view);
    }

//    private void setTheme(View view) {
//        mBtnOk.setTextColor(ContextCompat.getColor(mContext, R.color.white));
//        mBtnCancel.setTextColor(ContextCompat.getColor(mContext, R.color.white));
//
//        ColorStateList list = ContextCompat.getColorStateList(mContext, R.color.green);
//        ColorStateList listRed = ContextCompat.getColorStateList(mContext, R.color.red);
//
//        ViewCompat.setBackgroundTintList(mBtnOk, list);
//        ViewCompat.setBackgroundTintList(mBtnCancel, listRed);
//    }

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
        float amount = Integer.parseInt(mAcetDescription.getText().toString());

        // TODO: 06/06/17 Validation for currency
        if (!TextUtils.isEmpty(description.trim()) && description.length() < 10) {
            mTilDescription.setError("Please provide a description");
            return false;
        } else {
            mTilDescription.setErrorEnabled(false);
        }

        if (!TextUtils.isEmpty(description.trim()) && amount < 0) {
            mTilAmt.setError("Invalid amount.");
            return false;
        } else {
            mTilAmt.setErrorEnabled(false);
        }

        return true;
    }
}

