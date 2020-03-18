package net.growdev.userojekonline.view.splashscreen;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

import net.growdev.userojekonline.MainActivity;
import net.growdev.userojekonline.R;
import net.growdev.userojekonline.helper.SessionManager;
import net.growdev.userojekonline.view.auth.AuthActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreenActivity extends AppCompatActivity implements SplashScreenContract.View {


    @BindView(R.id.splash_lottie)
    LottieAnimationView splashLottie;

    private SplashScreenPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);

        getSupportActionBar().hide();

        presenter = new SplashScreenPresenter(this);
        presenter.delaySplash(3000, splashLottie);
    }

    @Override
    public void pindahActivity() {

        SessionManager sessionManager = new SessionManager(this);
        if (sessionManager.isLogin()){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, AuthActivity.class));
            finish();
        }


    }

    // default MVP
    @Override
    public void onAttachView() {
        presenter.onAttach(this);
    }

    @Override
    public void onDetachView() {
        presenter.onDetach();
    }

    @Override
    protected void onStart() {
        super.onStart();
        onAttachView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onDetachView();
    }
}
