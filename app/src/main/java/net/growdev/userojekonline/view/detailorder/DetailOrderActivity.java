package net.growdev.userojekonline.view.detailorder;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import net.growdev.userojekonline.R;
import net.growdev.userojekonline.helper.DirectionMapsV2;
import net.growdev.userojekonline.helper.HeroHelper;
import net.growdev.userojekonline.helper.SessionManager;
import net.growdev.userojekonline.model.modelhistory.DataHistory;
import net.growdev.userojekonline.view.history.HistoryActivity;
import net.growdev.userojekonline.view.history.HistoryPresenter;
import net.growdev.userojekonline.view.rating.RatingActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.LatLngBounds;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static net.growdev.userojekonline.helper.MyContants.IDBOOKING;
import static net.growdev.userojekonline.helper.MyContants.IDDRIVER;
import static net.growdev.userojekonline.helper.MyContants.INDEX;
import static net.growdev.userojekonline.helper.MyContants.STATUS;


public class DetailOrderActivity extends FragmentActivity implements OnMapReadyCallback, DetailOrderContract.View {

    @BindView(R.id.textView7)
    TextView textView7;
    @BindView(R.id.textView8)
    TextView textView8;
    @BindView(R.id.txtidbooking)
    TextView txtidbooking;
    @BindView(R.id.requestFrom)
    TextView requestFrom;
    @BindView(R.id.requestTo)
    TextView requestTo;
    @BindView(R.id.textView9)
    TextView textView9;
    @BindView(R.id.requestWaktu)
    TextView requestWaktu;
    @BindView(R.id.requestTarif)
    TextView requestTarif;
    @BindView(R.id.textView18)
    TextView textView18;
    @BindView(R.id.requestNama)
    TextView requestNama;
    @BindView(R.id.requestEmail)
    TextView requestEmail;
    @BindView(R.id.requestID)
    TextView requestID;
    @BindView(R.id.CompleteBooking)
    Button CompleteBooking;
    @BindView(R.id.reviewbooking)
    Button reviewbooking;
    private GoogleMap mMap;

    DetailOrderPresenter presenter;
    private int index;
    private int status;
    private DataHistory dataHistory;
    private ProgressDialog loading;
    private SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        ButterKnife.bind(this);

        loading = new ProgressDialog(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapDetail);
        mapFragment.getMapAsync(this);

        presenter = new DetailOrderPresenter(this);

        index = getIntent().getIntExtra(INDEX, 0);
        status = getIntent().getIntExtra(STATUS, 0);

        if (status == 2){
            dataHistory = HistoryPresenter.dataHistory.get(index);
        }
        else{
            dataHistory = HistoryPresenter.dataHistoryComplete.get(index);
            CompleteBooking.setVisibility(View.GONE);
            reviewbooking.setVisibility(View.VISIBLE);
        }

        detailRequest();
    }

    private void detailRequest() {

        requestFrom.setText("dari :" + dataHistory.getBookingFrom());
        requestTo.setText("tujuan :" + dataHistory.getBookingTujuan());
        requestTarif.setText("tarif :" + dataHistory.getBookingBiayaUser());
        requestWaktu.setText("jarak :" + dataHistory.getBookingJarak());
        txtidbooking.setText("idbooking:" + dataHistory.getIdBooking());
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

        detailMap();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(
                        "http://maps.google.com/maps?daddr=" + dataHistory.getBookingTujuanLat() + ","
                                + dataHistory.getBookingTujuanLng()));
                startActivity(i);
            }
        });
    }

    private void detailMap() {

        //get koordinat
        String origin = dataHistory.getBookingFromLat() + "," + dataHistory.getBookingFromLng();
        String desti = dataHistory.getBookingTujuanLat() + "," + dataHistory.getBookingTujuanLng();

        LatLngBounds.Builder bound = LatLngBounds.builder();
        bound.include(new LatLng(Double.parseDouble(dataHistory.getBookingFromLat()), Double.parseDouble(dataHistory.getBookingFromLng())));
        bound.include(new LatLng(Double.parseDouble(dataHistory.getBookingTujuanLat()), Double.parseDouble(dataHistory.getBookingTujuanLng())));
        // mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bound.build(), 16));
        LatLngBounds bounds = bound.build();
        // begin new code:
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.12); // offset from edges of the map 12% of screen

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        // end of new code

        mMap.animateCamera(cu);
        String key = getString(R.string.google_maps_key);
        presenter.detailRute(origin, desti, key);
    }

    @OnClick({R.id.CompleteBooking, R.id.reviewbooking})
    public void onViewClicked(View view) {

        session = new SessionManager(this);
        String iduser = session.getIdUser();
        String token = session.getToken();
        String device = HeroHelper.getDeviceUUID(this);
        String iddriver = dataHistory.getBookingDriver();
        String idbooking = dataHistory.getIdBooking();

        switch (view.getId()) {

            case R.id.CompleteBooking:

                presenter.completeBooking(iduser, idbooking, token, device);

                break;

            case R.id.reviewbooking:

                Intent i = new Intent(this, RatingActivity.class);
                i.putExtra(IDDRIVER, iddriver);
                i.putExtra(IDBOOKING, idbooking);
                startActivity(i);

                break;
        }
    }

    @Override
    public void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading(String msgLoading) {
        loading.setTitle("proses " + msgLoading);
        loading.setMessage(msgLoading);
        loading.show();
    }

    @Override
    public void hideLoading() {
        loading.dismiss();
    }

    @Override
    public void showError(String localizedMessage) {
        Toast.makeText(this, localizedMessage, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void getDataMap(String dataGaris) {
        DirectionMapsV2 mapsV2 = new DirectionMapsV2(this);
        mapsV2.gambarRoute(mMap, dataGaris);
    }

    @Override
    public void pindahHalaman() {
        startActivity(new Intent(this, HistoryActivity.class));
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
    }
}


