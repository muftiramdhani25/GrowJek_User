package net.growdev.userojekonline.view.goride;

import net.growdev.userojekonline.base.BaseView;
import net.growdev.userojekonline.model.modelmap.Distance;
import net.growdev.userojekonline.model.modelmap.Duration;
import net.growdev.userojekonline.model.modelmap.LegsItem;
import net.growdev.userojekonline.model.modelmap.OverviewPolyline;
import net.growdev.userojekonline.model.modelmap.ResponseMap;
import net.growdev.userojekonline.model.modelmap.RoutesItem;
import net.growdev.userojekonline.model.modelreqorder.ResponseBooking;
import net.growdev.userojekonline.network.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoRidePresenter implements GoRideContract.Presenter {

    private GoRideContract.View goRideView;
    private BaseView baseView;

    public GoRidePresenter(GoRideContract.View goRideView) {
        this.goRideView = goRideView;
    }

    @Override
    public void getDataMap(String lokasiAwal, String lokasiTujuan, String key) {
        goRideView.showLoading("loading");

        ApiClient.getApiServiceMap().getDataMap(lokasiAwal, lokasiTujuan, key).enqueue(new Callback<ResponseMap>() {
            @Override
            public void onResponse(Call<ResponseMap> call, Response<ResponseMap> response) {
                goRideView.hideLoading();

                if (response.isSuccessful() || response.code() == 200){
                    ResponseMap responseMap = response.body();
                    List<RoutesItem> route = responseMap.getRoutes();
                    RoutesItem object = route.get(0);
                    List<LegsItem> legs = object.getLegs();
                    LegsItem objectLegs = legs.get(0);
                    Distance distance = objectLegs.getDistance();
                    Duration duration = objectLegs.getDuration();

                    int jarak = distance.getValue();
                    String waktu = duration.getText();

                    double harga = Math.ceil((jarak / 1000) * 5000);
                    String convJarak = String.valueOf(jarak / 1000);
                    goRideView.showInfoOrder(waktu, convJarak, harga);

                    OverviewPolyline overviewPolilyne = object.getOverviewPolyline();
                    String point = overviewPolilyne.getPoints();
                    goRideView.dataGaris(point);

                }
            }

            @Override
            public void onFailure(Call<ResponseMap> call, Throwable t) {
                goRideView.hideLoading();
                goRideView.showError(t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void requestOrder(String idUser,  String latAwal, String lonAwal, String lokasiAwal,
                             String latTujuan, String lonTujuan, String lokasiTujuan,
                             String catatan, String jarak, String token, String device) {

        goRideView.showLoading("Sedang booking");

        ApiClient.getApiService().insertBooking(idUser, latAwal, lonAwal, lokasiAwal, latTujuan, lonTujuan, lokasiTujuan, catatan, jarak, token, device).enqueue(new Callback<ResponseBooking>() {
            @Override
            public void onResponse(Call<ResponseBooking> call, Response<ResponseBooking> response) {
                goRideView.hideLoading();

                if (response.isSuccessful() || response.code() == 200){
                    boolean result = response.body().isResult();
                    String msg = response.body().getMsg();

                    if (result){
                        goRideView.showMessage(msg);

                        int idBooking = response.body().getIdBooking();
                        int tarif = response.body().getTarif();

                        goRideView.showDataBooking(String.valueOf(idBooking), String.valueOf(tarif));
                    } else {
                        goRideView.showMessage(msg);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBooking> call, Throwable t) {
                goRideView.hideLoading();
                goRideView.showError(t.getLocalizedMessage());
            }
        });

    }

    @Override
    public void onAttach(BaseView view) {

    }

    @Override
    public void onDetach() {

    }
}
