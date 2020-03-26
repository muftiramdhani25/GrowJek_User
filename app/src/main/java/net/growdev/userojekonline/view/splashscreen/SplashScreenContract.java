package net.growdev.userojekonline.view.splashscreen;

import com.airbnb.lottie.LottieAnimationView;

import net.growdev.userojekonline.base.BasePresenter;
import net.growdev.userojekonline.base.BaseView;

public interface SplashScreenContract {

    interface View extends BaseView<BasePresenter>{
        void pindahActivity();
    }

    interface Presenter extends BasePresenter{
        void delaySplash(long i, LottieAnimationView splashLottie);
    }

}
