package net.growdev.userojekonline.view.goride;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.compat.AutocompleteFilter;
import com.google.android.libraries.places.compat.Place;
import com.google.android.libraries.places.compat.ui.PlaceAutocomplete;

import net.growdev.userojekonline.R;
import net.growdev.userojekonline.helper.DirectionMapsV2;
import net.growdev.userojekonline.helper.GPSTracker;
import net.growdev.userojekonline.helper.HeroHelper;
import net.growdev.userojekonline.helper.MyContants;
import net.growdev.userojekonline.helper.SessionManager;
import net.growdev.userojekonline.view.waitingdriver.WaitingDriverActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static net.growdev.userojekonline.helper.MyContants.IDBOOKING;
import static net.growdev.userojekonline.helper.MyContants.TARIF;

public class GoRideActivity extends FragmentActivity implements OnMapReadyCallback, GoRideContract.View {

    @BindView(R.id.imgpick)
    ImageView imgpick;
    @BindView(R.id.lokasiawal)
    TextView lokasiawal;
    @BindView(R.id.lokasitujuan)
    TextView lokasitujuan;
    @BindView(R.id.edtcatatan)
    EditText edtcatatan;
    @BindView(R.id.txtharga)
    TextView txtharga;
    @BindView(R.id.txtjarak)
    TextView txtjarak;
    @BindView(R.id.txtdurasi)
    TextView txtdurasi;
    @BindView(R.id.requestorder)
    Button requestorder;
    @BindView(R.id.rootlayout)
    RelativeLayout rootlayout;
    private GoogleMap mMap;

    private HeroHelper heroHelper;
    private GoogleApiClient googleApiClient;
    private GPSTracker gpsTracker;
    private GoRidePresenter presenter;
    private ProgressDialog loadingDialog;

    private double latAwal;
    private double lonAwal;
    private LatLng latLngAwal;
    private String namaLokasiAwal;

    private double latTujuan;
    private double lonTujuan;
    private LatLng latLngTujuan;
    private String namaLokasiTujuan;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_ride);
        ButterKnife.bind(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        heroHelper = new HeroHelper();
        heroHelper.checkGpsDevice(this, googleApiClient);

        presenter = new GoRidePresenter(this);
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

        gpsTracker = new GPSTracker(this);

        if (gpsTracker.canGetLocation()){

            latAwal = gpsTracker.getLatitude();
            lonAwal = gpsTracker.getLongitude();


            String lokasi = convertLocation(latAwal, lonAwal);
            lokasiawal.setText(lokasi);

            // Add a marker in Sydney and move the camera
            latLngAwal  = new LatLng(latAwal, lonAwal);
            mMap.addMarker(new MarkerOptions().position(latLngAwal).title(lokasi));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngAwal, 17));


        }

    }

    private String convertLocation(double myLat, double myLng) {

        String nameLocation = null;

        Geocoder geocoder = new Geocoder(GoRideActivity.this, Locale.getDefault());
        try {
            List<Address> list = geocoder.getFromLocation(myLat, myLng, 1);
            if (list != null && list.size() > 0) {

                nameLocation = list.get(0).getAddressLine(0) + " " + list.get(0).getCountryName();
                //fetch data from addresses
            } else {
                Toast.makeText(GoRideActivity.this, "kosong", Toast.LENGTH_SHORT).show();
                //display Toast message
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nameLocation;
    }





    @OnClick({R.id.imgpick, R.id.lokasiawal, R.id.lokasitujuan, R.id.requestorder})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgpick:
                startActivityForResult(new Intent(GoRideActivity.this, MapsActivity.class),0);
                break;
            case R.id.lokasiawal:
                setLokasi(1);
                break;
            case R.id.lokasitujuan:
                setLokasi(2);
                break;
            case R.id.requestorder:
                SessionManager sesi = new SessionManager(this);
                String idUser = sesi.getIdUser();
                String latAwalStr = String.valueOf(latAwal);
                String lonAwalStr = String.valueOf(lonAwal);
                String lokasiAwalStr = lokasiawal.getText().toString();
                String latTujuanStr = String.valueOf(latTujuan);
                String lonTujuanStr = String.valueOf(lonTujuan);
                String lokasiTujuanStr = lokasitujuan.getText().toString();
                String catatan = edtcatatan.getText().toString();
                String jarak = txtjarak.getText().toString();
                String token = sesi.getToken();
                String device = HeroHelper.getDeviceUUID(this);

                presenter.requestOrder(idUser, latAwalStr, lonAwalStr, lokasiAwalStr, latTujuanStr, lonTujuanStr, lokasiTujuanStr, catatan, jarak, token, device);
                break;
        }
    }

    private void setLokasi(int kodeAmbilLokasi){
        AutocompleteFilter filter = new AutocompleteFilter.Builder()
                .setCountry("ID")
                .build();

        Intent i;
        try {
            i = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .setFilter(filter)
                    .build(GoRideActivity.this);
            startActivityForResult(i, kodeAmbilLokasi);
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // ambil lokasi awal
        if (requestCode == 1){
            if (resultCode == RESULT_OK){

                Place place = PlaceAutocomplete.getPlace(this, data);

                latAwal = place.getLatLng().latitude;
                lonAwal = place.getLatLng().longitude;
                latLngAwal = new LatLng(latAwal, lonAwal);

                mMap.clear();

                namaLokasiAwal = place.getAddress().toString();
                mMap.addMarker(new MarkerOptions().position(latLngAwal).title(namaLokasiAwal));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngAwal, 17));

                lokasiawal.setText(namaLokasiAwal);

            } else if (resultCode == RESULT_CANCELED){
                Toast.makeText(this, "Batal ambil lokasi", Toast.LENGTH_SHORT).show();
            }
        }

        // ambil lokasi tujuan
        if (requestCode == 2){
            if (resultCode == RESULT_OK){
                Place place = PlaceAutocomplete.getPlace(this, data);

                latTujuan = place.getLatLng().latitude;
                lonTujuan = place.getLatLng().longitude;
                latLngTujuan = new LatLng(latTujuan, lonTujuan);

                namaLokasiTujuan = place.getAddress().toString();
                mMap.addMarker(new MarkerOptions().position(latLngTujuan).title(namaLokasiTujuan));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngTujuan, 17));

                lokasitujuan.setText(namaLokasiTujuan);

                setZoomTwoLocation();

                presenter.getDataMap(latAwal+","+lonAwal, latTujuan+","+lonTujuan, getString(R.string.google_maps_key));

            } else if (resultCode == RESULT_CANCELED){
                Toast.makeText(this, "batal ambil lokasi tujuan", Toast.LENGTH_SHORT).show();
            }
        }

        if (data == null) {
            Toast.makeText(this, "no data", Toast.LENGTH_SHORT).show();
            return;

        } else {


            switch (resultCode) {
                case MyContants.LOKASI:
                    Bundle resultData = data.getExtras();
                    namaLokasiAwal = resultData.getString("value");
                    latAwal = resultData.getDouble("lat");
                    lonAwal = resultData.getDouble("lon");
                    //Implicit intent to make a call
                    mMap.clear();
                    lokasiawal.setText(namaLokasiAwal);
//                    latawal = lati;
//                    lonawal = longi;
                    break;

            }
        }
    }

    private void setZoomTwoLocation(){

        LatLngBounds.Builder latLongBuilder = new LatLngBounds.Builder();

        latLongBuilder.include(new LatLng(latAwal, lonAwal));
        latLongBuilder.include(new LatLng(latTujuan, lonTujuan));

        // Dapatkan koordinat di tengah2
        LatLngBounds bounds = latLongBuilder.build();
        //
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int paddingMap = (int) (width * 0.3); // jarak dari sisi map 30 %

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, paddingMap);

        mMap.animateCamera(cu);
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
    public void showInfoOrder(String durasi, String jarak, double harga) {
        txtdurasi.setText(durasi);
        txtjarak.setText(jarak);
        txtharga.setText(String.valueOf(harga));
    }

    @Override
    public void showDataBooking(String idBooking, String tarif) {
        Intent kirim = new Intent(this, WaitingDriverActivity.class);
        kirim.putExtra(IDBOOKING, idBooking);
        kirim.putExtra(TARIF, tarif);
        startActivity(kirim);
    }

    @Override
    public void dataGaris(String dataGaris) {
        DirectionMapsV2 mapsV2 = new DirectionMapsV2(this);
        mapsV2.gambarRoute(mMap, dataGaris);
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
