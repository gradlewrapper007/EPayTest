package com.singh.sudhanshu.epaytest.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.github.jorgecastilloprz.listeners.FABProgressListener;
import com.singh.sudhanshu.epaytest.R;
import com.singh.sudhanshu.epaytest.api.ApiHandler;
import com.singh.sudhanshu.epaytest.ui.widget.RoundImageView;
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
    @BindView(R.id.login_logo)
    RoundImageView mImvLogo;
    @BindView(R.id.fabProgressCircle)
    FABProgressCircle mFabCircle;

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

            //Load images
            Glide.with(this).load(R.drawable.login_one).diskCacheStrategy(DiskCacheStrategy.ALL).into(mImview);

            //Init KenburnsImageview used as BG
            RandomTransitionGenerator generator = new RandomTransitionGenerator(3000, new AccelerateDecelerateInterpolator());
            mImview.setTransitionGenerator(generator);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        Animation expandIn = AnimationUtils.loadAnimation(this, R.anim.pop_in);
        expandIn.setStartOffset(500);
        mImvLogo.startAnimation(expandIn);
    }

    /**
     * launch/skip-to dashboard
     */
    void launchDash() {
        startActivity(new Intent(this, DashBoardActivity.class));
        finish();
    }

    //Login BUtton action
    @OnClick(R.id.login_btn_fab)
    void callAPI() {
        String email = mAcetLogin.getText().toString().trim();
        Utils.hideKB(this, mAcetLogin);

        if (TextUtils.isEmpty(email)
                || !Utils.isValidEmail(email, this)) {
            ToastUtil.showLongToast(this, R.string.error_msg_invalidemail);


            return;
        }

//        mBtnFab.setEnabled(false);
        mFabCircle.show();
        ApiHandler.fetchTokenAndSaveIfNull(this, new AppCallback() {
            @Override
            public void onSuccess(Object data) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                    //Animate the circle around FAB
                    mFabCircle.beginFinalAnimation();
                    mFabCircle.attachListener(new FABProgressListener() {
                        @Override
                        public void onFABProgressAnimationEnd() {
                            revealSuccessUI();
                        }
                    });


                } else {
                    launchDash();
                }

            }

            @Override
            public void onFailure(Object data) {
                mBtnFab.setEnabled(true);
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
                }, 1000);
            }
        });

        anim.start();

        ToastUtil.showSmallToast(this, R.string.msg_login_success);
    }


}
