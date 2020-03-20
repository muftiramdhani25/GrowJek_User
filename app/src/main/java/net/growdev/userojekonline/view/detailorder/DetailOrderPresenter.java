package net.growdev.userojekonline.view.detailorder;

import net.growdev.userojekonline.base.BaseView;
import net.growdev.userojekonline.model.modelhistory.ResponseHistory;
import net.growdev.userojekonline.model.modelmap.ResponseMap;
import net.growdev.userojekonline.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailOrderPresenter implements DetailOrderContract.Presenter {

    DetailOrderContract.View detailOrderView;
    BaseView view;


    public DetailOrderPresenter(DetailOrderContract.View detailOrderView) {
        this.detailOrderView = detailOrderView;
    }


    @Override
    public void detailRute(String origin, String desti, String key) {

        ApiClient.getApiServiceMap().getDataMap(origin, desti, key).enqueue(new Callback<ResponseMap>() {
            @Override
            public void onResponse(Call<ResponseMap> call, Response<ResponseMap> response) {
                if (response.isSuccessful()){
                    String status = response.body().getStatus();
                    if (status.equals("OK")){
                        String dataGaris = response.body().getRoutes().get(0)
                                .getOverviewPolyline().getPoints();
                        detailOrderView.getDataMap(dataGaris);
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseMap> call, Throwable t) {
                detailOrderView.showError(t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void completeBooking(String iduser, String idbooking, String token, String device) {

        detailOrderView.showLoading("proses complete booking");

        ApiClient.getApiService().completeBooking(iduser, idbooking, token, device)
                .enqueue(new Callback<ResponseHistory>() {
                    @Override
                    public void onResponse(Call<ResponseHistory> call, Response<ResponseHistory> response) {
                        detailOrderView.hideLoading();
                        if (response.isSuccessful()){
                            boolean result = response.body().getResult();
                            String msg = response.body().getMsg();
                            if (result){
                                detailOrderView.showMsg(msg);
                                detailOrderView.pindahHalaman();
                            }else {
                                detailOrderView.showMsg(msg);
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseHistory> call, Throwable t) {
                        detailOrderView.showError(t.getLocalizedMessage());
                        detailOrderView.hideLoading();

                    }
                });
    }

    @Override
    public void onAttach(BaseView view) {
        this.view = view;
    }

    @Override
    public void onDetach() {
        view = null;
    }
}



