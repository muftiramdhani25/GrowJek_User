package net.growdev.userojekonline.view.trackingdriver;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import net.growdev.userojekonline.R;
import net.growdev.userojekonline.model.modeltracking.DataItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static net.growdev.userojekonline.helper.MyContants.IDDRIVER;

public class TrackingDriverActivity extends FragmentActivity implements OnMapReadyCallback, TrackingDriverContract.View {

    @BindView(R.id.lokasiawal)
    TextView lokasiawal;
    @BindView(R.id.lokasitujuan)
    TextView lokasitujuan;
    @BindView(R.id.txtnamadriver)
    TextView txtnamadriver;
    @BindView(R.id.linear2)
    LinearLayout linear2;
    @BindView(R.id.txthpdriver)
    TextView txthpdriver;
    @BindView(R.id.linear1)
    LinearLayout linear1;
    private GoogleMap mMap;

    private String idDriver;
    private double latDriver;
    private double lonDriver;
    private LatLng latLngDriver;

    private TrackingDriverPresenter presenter;
    private ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_driver);
        ButterKnife.bind(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        idDriver = getIntent().getStringExtra(IDDRIVER);

        presenter = new TrackingDriverPresenter(this);

        loadingDialog = new ProgressDialog(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void showMsg(String msg) {
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
    public void showError(String localizedMessage) {
        Toast.makeText(this, localizedMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getDataDriver(List<DataItem> driver) {

        txthpdriver.setText(driver.get(0).getUserHp());
        txtnamadriver.setText(driver.get(0).getUserNama());
        latDriver = Double.parseDouble(driver.get(0).getTrackingLat());
        lonDriver = Double.parseDouble(driver.get(0).getTrackingLng());
        latLngDriver = new LatLng(latDriver, lonDriver);
        mMap.addMarker(new MarkerOptions().position(latLngDriver)).setIcon(
                BitmapDescriptorFactory.fromResource(R.mipmap.ic_car)
        );
        mMap.moveCamera(CameraUpdateFactory.
                newLatLngZoom(latLngDriver, 17));
        mMap.setPadding(40, 150, 50, 120);

        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);


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
    }
}
