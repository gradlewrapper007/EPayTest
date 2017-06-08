package com.singh.sudhanshu.epaytest.ui.activity;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.singh.sudhanshu.epaytest.R;
import com.singh.sudhanshu.epaytest.api.ApiHandler;
import com.singh.sudhanshu.epaytest.model.Balance;
import com.singh.sudhanshu.epaytest.model.Product;
import com.singh.sudhanshu.epaytest.ui.widget.AddSpendDialog;
import com.singh.sudhanshu.epaytest.utils.PreferenceUtil;
import com.singh.sudhanshu.epaytest.utils.ToastUtil;
import com.singh.sudhanshu.epaytest.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sudhanshu on 05/06/17.
 */

public class DashBoardActivity extends AppCompatActivity {

    @BindView(R.id.dash_tv_blance)
    TextView mTvBalance;
    @BindView(R.id.dash_recycler)
    RecyclerView mRecyclerView;

    private TransactionAdapter mAdapter;
    private LinearLayoutManager layoutManager;
    private List<Product> transactionList = new ArrayList<>();
    double mBalance = 0.0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        setupRecycler();

        if (!Utils.isConnected(this)) {
            ToastUtil.showLongToast(this, R.string.error_msg_no_network);
            findViewById(R.id.dash_bo_net).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.dash_bo_net).setVisibility(View.GONE);
            //fetch balance
            fetchBalance();
            //fetch the list
            fetchTransactions();
        }


    }

    /**
     * fetches/refreshes transactions
     */
    void fetchTransactions() {
        ApiHandler.fetchTransactions(this, new AppCallback() {
            @Override
            public void onSuccess(Object data) {

                transactionList.clear();
                transactionList = ((List<Product>) data);
                mAdapter.addALL(transactionList);

            }

            @Override
            public void onFailure(Object data) {

            }
        });
    }

    void fetchBalance() {
        ApiHandler.fetchBalance(this, new AppCallback() {
            @Override
            public void onSuccess(Object data) {

                Balance balance = ((Balance) data);
                mBalance = Double.parseDouble(balance.getBalance());
//                mTvBalance.setText(Utils.getCurrencySymbol(balance.getCurrency()) + " " + balance.getBalance());
//                startCountAnimation((int) mBalance);
                startCountAnimation((float) mBalance);
            }

            @Override
            public void onFailure(Object data) {
                LogoutUser();
            }
        });
    }

    /**
     * Logs user out, clears tokens
     */
    private void LogoutUser() {
        ToastUtil.showSmallToast(this, R.string.error_msg_logout);
        PreferenceUtil.getInstance().clear();
        startActivity(new Intent(DashBoardActivity.this, LoginActivity.class));
        finishAffinity();
    }

    /**
     * Setup recycler view(the List)
     */
    void setupRecycler() {
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(layoutManager);
        //For same size items
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new TransactionAdapter(transactionList, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * Opens the Spend dialog
     */
    @OnClick(R.id.dash_btn_fab)
    void fabClick() {

        if (!Utils.isConnected(this)) {
            ToastUtil.showLongToast(this, R.string.error_msg_no_network);
            return;
        }

        AddSpendDialog dialog = new AddSpendDialog();
        dialog.addData(new AppCallback() {
            @Override
            public void onSuccess(Object data) {
                //reload the data
                mAdapter.clear(new ArrayList<Product>());
                fetchTransactions();
                fetchBalance();
            }

            @Override
            public void onFailure(Object data) {

            }
        }, mBalance);
        dialog.show(getSupportFragmentManager(), "spend_diag");
    }

    /**
     * Adapter class for the list items
     */
    private static class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionRowItemholder> {

        private List<Product> mList;

        public AppCallback getCallback() {
            return this.callback;
        }

        public void setCallback(AppCallback callback) {
            this.callback = callback;
        }

        private Context mActivity;
        private AppCallback callback;

        public TransactionAdapter(List<Product> list, Activity activity) {
            mList = new ArrayList<>();
            this.mActivity = activity;
            if (list != null)
                mList.addAll(list);
        }


        void addALL(List<Product> list) {
            if (list != null) {
                mList.addAll(list);
                notifyDataSetChanged();
            }
        }

        void clear(List<Product> list) {
            if (list != null) {
                mList.clear();
                notifyDataSetChanged();
            }
        }

        public static class TransactionRowItemholder extends RecyclerView.ViewHolder {
            private TextView mTvTitle, mTvTime, mTvAmt;

            public TransactionRowItemholder(final View itemView) {
                super(itemView);
                mTvTitle = (TextView) itemView.findViewById(R.id.row_spend_title);
                mTvTime = (TextView) itemView.findViewById(R.id.row_spend_time);
                mTvAmt = (TextView) itemView.findViewById(R.id.row_spend_amt);

            }
        }

        @Override
        public TransactionRowItemholder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_row_spend, parent, false);
            return new TransactionRowItemholder(v);
        }

        @Override
        public void onBindViewHolder(final TransactionRowItemholder holder, final int position) {

            Product model = mList.get(position);
            holder.mTvTitle.setText(model.getDescription());
            holder.mTvTime.setText(Utils.fromISO8601UTC(model.getDate()));
            holder.mTvAmt.setText(Utils.getCurrencySymbol(model.getCurrency()) + " " + model.getAmount());

        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        void clear() {
            mList.clear();
        }

    }

    /**
     * Shows a meter like animation on TextView
     *
     * @param balance
     */
    private void startCountAnimation(float balance) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, balance);
        animator.setDuration(600);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                mTvBalance.setText(String.format("%.2f", (float) animation.getAnimatedValue()));
            }
        });
        animator.start();
    }
}
