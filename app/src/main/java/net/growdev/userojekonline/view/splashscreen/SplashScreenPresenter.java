package net.growdev.userojekonline.view.splashscreen;

import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;

import net.growdev.userojekonline.base.BaseView;



public class SplashScreenPresenter implements SplashScreenContract.Presenter {

    private SplashScreenContract.View splashView;
    private BaseView baseView;

    public SplashScreenPresenter(SplashScreenContract.View splashView) {
        this.splashView = splashView;
    }

    @Override
    public void delaySplash(long delay, LottieAnimationView splashLottie) {
        splashLottie.setAnimation("motorcycle.json");
        splashLottie.loop(true);
        splashLottie.playAnimation();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                splashView.pindahActivity();
            }
        }, delay);

    }

    @Override
    public void onAttach(BaseView view) {
        baseView = view;
    }

    @Override
    public void onDetach() {
        baseView = null;
    }
}
