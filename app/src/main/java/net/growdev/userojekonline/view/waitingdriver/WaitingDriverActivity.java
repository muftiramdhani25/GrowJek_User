package net.growdev.userojekonline.view.waitingdriver;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import net.growdev.userojekonline.R;
import net.growdev.userojekonline.helper.HeroHelper;
import net.growdev.userojekonline.helper.SessionManager;
import net.growdev.userojekonline.view.trackingdriver.TrackingDriverActivity;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;

import static net.growdev.userojekonline.helper.MyContants.IDBOOKING;
import static net.growdev.userojekonline.helper.MyContants.IDDRIVER;

public class WaitingDriverActivity extends AppCompatActivity implements WaitingDriverContract.View {

    @BindView(R.id.pulsator)
    PulsatorLayout pulsator;
    @BindView(R.id.buttoncancel)
    Button buttoncancel;

    private WaitingDriverPresenter presenter;
    private SessionManager session;
    private ProgressDialog loadingDialog;
    private Timer timer;

    private String idBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_driver);
        ButterKnife.bind(this);

        idBooking = getIntent().getStringExtra(IDBOOKING);

        pulsator.start();

        timer = new Timer();

        presenter = new WaitingDriverPresenter(this);
        presenter.cekStatusBooking(idBooking);

        loadingDialog = new ProgressDialog(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        presenter.cekStatusBooking(idBooking);
                    }
                }, 0, 5000);
            }
        });
    }

    @OnClick(R.id.buttoncancel)
    public void onViewClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("cancel order");
        builder.setMessage("apakah anda yakin cancel orderan ini ?");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                session = new SessionManager(WaitingDriverActivity.this);
                String token = session.getToken();
                String device = HeroHelper.getDeviceUUID(WaitingDriverActivity.this);
                presenter.cancelBooking(idBooking, token, device);
            }
        });
        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading(String msgLoading) {
        loadingDialog.setMessage(msgLoading);
        loadingDialog.show();
    }

    @Override
    public void hideLoading() {
        loadingDialog.dismiss();
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getDataDriver(String idDriver) {
        // intent ke tracking
        Intent intent = new Intent(this, TrackingDriverActivity.class);
        intent.putExtra(IDDRIVER, idDriver);
        startActivity(intent);
        finish();

    }

    @Override
    public void back() {
        finish();
    }

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
        timer.cancel();
    }
}
