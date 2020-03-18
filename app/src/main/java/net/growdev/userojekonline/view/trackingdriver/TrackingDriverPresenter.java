package net.growdev.userojekonline.view.trackingdriver;

import net.growdev.userojekonline.base.BaseView;
import net.growdev.userojekonline.model.modeltracking.DataItem;
import net.growdev.userojekonline.model.modeltracking.ResponseTracking;
import net.growdev.userojekonline.network.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackingDriverPresenter implements TrackingDriverContract.Presenter{

    private TrackingDriverContract.View trackingView;
    private BaseView baseView;

    public TrackingDriverPresenter(TrackingDriverContract.View trackingView) {
        this.trackingView = trackingView;
    }

    @Override
    public void getDetailDriver(String idDriver) {
        trackingView.showLoading("loading tracking");

        ApiClient.getApiService().getTracking(idDriver).enqueue(new Callback<ResponseTracking>() {
            @Override
            public void onResponse(Call<ResponseTracking> call, Response<ResponseTracking> response) {
                trackingView.hideLoading();

                if (response.isSuccessful() || response.code() == 200){
                    boolean result = response.body().isResult();
                    String msg = response.body().getMsg();

                    if (result){
                        trackingView.showMsg(msg);

                        List<DataItem> dataDriver = response.body().getData();
                        trackingView.getDataDriver(dataDriver);
                    } else {
                        trackingView.showMsg(msg);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseTracking> call, Throwable t) {
                trackingView.hideLoading();
                trackingView.showError(t.getLocalizedMessage());
            }
        });
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
