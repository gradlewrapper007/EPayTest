package com.singh.sudhanshu.epaytest.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;
import com.singh.sudhanshu.epaytest.R;
import com.singh.sudhanshu.epaytest.api.ApiHandler;
import com.singh.sudhanshu.epaytest.utils.Constants;
import com.singh.sudhanshu.epaytest.utils.PreferenceUtil;
import com.singh.sudhanshu.epaytest.utils.ToastUtil;
import com.singh.sudhanshu.epaytest.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sudhanshu on 05/06/17.
 */
public class LoginActivity extends AppCompatActivity {

    private final String TAG = LoginActivity.class.getSimpleName();
    @BindView(R.id.login_id_acet)
    AppCompatEditText mAcetLogin;
    @BindView(R.id.image_xformer)
    KenBurnsView mImview;
    @BindView(R.id.reveal_view)
    RelativeLayout mRevealView;
    @BindView(R.id.login_btn_fab)
    FloatingActionButton mBtnFab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!TextUtils.isEmpty(PreferenceUtil.getInstance().getStringValue(Constants.PREF_TOKEN, null))) {

            Log.i(TAG, "onCreate: token" + (PreferenceUtil.getInstance().getStringValue(Constants.PREF_TOKEN, null)));
            launchDash();

        } else {
            setContentView(R.layout.activity_login);
            ButterKnife.bind(this);
            getSupportActionBar().hide();

            Glide.with(this).load(R.drawable.login_one).diskCacheStrategy(DiskCacheStrategy.ALL).into(mImview);
            RandomTransitionGenerator generator = new RandomTransitionGenerator(3000, new AccelerateDecelerateInterpolator());
            mImview.setTransitionGenerator(generator);
        }

    }

    /**
     * launch/skip-to dashboard
     */
    void launchDash() {
        startActivity(new Intent(this, DashBoardActivity.class));
        finish();
    }

    @OnClick(R.id.login_btn_fab)
    void callAPI() {
        String email = mAcetLogin.getText().toString().trim();
        if (TextUtils.isEmpty(email)
                || !Utils.isValidEmail(email, this)) {
            ToastUtil.showLongToast(this, R.string.error_msg_invalidemail);
            return;
        }
        mBtnFab.setEnabled(false);
        ApiHandler.fetchTokenAndSaveIfNull(this, new AppCallback() {
            @Override
            public void onSuccess(Object data) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    revealSuccessUI();
                } else {
                    launchDash();
                }

            }

            @Override
            public void onFailure(Object data) {

            }
        });
    }

    @OnClick(R.id.signup_btn)
    void comingSoon() {
        ToastUtil.showLongToast(this, R.string.error_msg_coming_soon);
    }

    @OnClick(R.id.gplus_btn)
    void comingSoon2() {
        ToastUtil.showLongToast(this, R.string.error_msg_coming_soon);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     *
     */
    @SuppressLint("NewApi")
    void revealSuccessUI() {

        //Change the UI of FAB
        mBtnFab.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(getResources(), R.color.green_500, null)));
        mBtnFab.setImageResource(R.drawable.ic_done_white_24dp);

        //Set reveal clipping circle from the center of the target view
        int cx = mRevealView.getWidth() / 2;
        int cy = mRevealView.getHeight() / 2;

        float finalRadius = (float) Math.hypot(cx, cy);
        Animator anim = null;

        anim = ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, 0, finalRadius);

        mRevealView.setVisibility(View.VISIBLE);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                mRevealView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        launchDash();
                    }
                }, 2000);
            }
        });

        anim.start();

        ToastUtil.showSmallToast(this, R.string.msg_login_success);
    }
}
