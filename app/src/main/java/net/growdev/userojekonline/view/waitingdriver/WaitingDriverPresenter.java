package net.growdev.userojekonline.view.waitingdriver;

import net.growdev.userojekonline.base.BaseView;
import net.growdev.userojekonline.model.modelwaiting.ResponseWaiting;
import net.growdev.userojekonline.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WaitingDriverPresenter implements WaitingDriverContract.Presenter{

    private WaitingDriverContract.View waitingView;
    BaseView baseView;

    public WaitingDriverPresenter(WaitingDriverContract.View waitingView) {
        this.waitingView = waitingView;
    }

    @Override
    public void cancelBooking(String idBooking, String token, String device) {
        waitingView.showLoading("proses cancel booking");

        ApiClient.getApiService().cancelBooking(idBooking, token, device).enqueue(new Callback<ResponseWaiting>() {
            @Override
            public void onResponse(Call<ResponseWaiting> call, Response<ResponseWaiting> response) {
                waitingView.hideLoading();
                if (response.isSuccessful() || response.code() == 200){
                    boolean result = response.body().isResult();
                    String msg = response.body().getMsg();

                    if (result){
                        waitingView.showMessage(msg);
                        waitingView.back();
                    } else {
                        waitingView.showMessage(msg);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseWaiting> call, Throwable t) {
                waitingView.hideLoading();
                waitingView.showError(t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void cekStatusBooking(String idBooking) {

        ApiClient.getApiService().cekStatusBooking(idBooking).enqueue(new Callback<ResponseWaiting>() {
            @Override
            public void onResponse(Call<ResponseWaiting> call, Response<ResponseWaiting> response) {
                if (response.isSuccessful() || response.code() == 200){
                    boolean result = response.body().isResult();
                    String msg = response.body().getMsg();

                    if (result){
                        waitingView.showMessage(msg);
                        String idDriver = response.body().getDriver();
                        waitingView.getDataDriver(idDriver);
                    } else {
                        waitingView.showMessage(msg);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseWaiting> call, Throwable t) {
                waitingView.showError(t.getLocalizedMessage());
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
