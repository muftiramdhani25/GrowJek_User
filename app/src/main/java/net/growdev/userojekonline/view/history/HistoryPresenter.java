package net.growdev.userojekonline.view.history;

import net.growdev.userojekonline.base.BaseView;
import net.growdev.userojekonline.model.modelhistory.DataHistory;
import net.growdev.userojekonline.model.modelhistory.ResponseHistory;
import net.growdev.userojekonline.network.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryPresenter implements HistoryContract.Presenter {

    HistoryContract.View historyView;
    BaseView view;
    public static List<DataHistory> dataHistory;
    public static List<DataHistory> dataHistoryComplete;

    public HistoryPresenter(HistoryContract.View historyView) {
        this.historyView = historyView;
    }

    @Override
    public void getDataHistory(int status, String token, String device, String iduser) {
        historyView.showLoading("get data history");

        if (status == 2){

            ApiClient.getApiService().getDataHistory(String.valueOf(status), iduser, token, device).enqueue(new Callback<ResponseHistory>() {
                @Override
                public void onResponse(Call<ResponseHistory> call, Response<ResponseHistory> response) {
                    historyView.hideLoading();

                    if (response.isSuccessful()){

                        boolean result = response.body().getResult();
                        String msg = response.body().getMsg();

                        if (result){
                            historyView.showMsg(msg);
                            dataHistory = response.body().getData();
                            historyView.dataHistory(dataHistory);
                        }
                        else{
                            historyView.showMsg(msg);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseHistory> call, Throwable t) {
                    historyView.showError(t.getLocalizedMessage());
                    historyView.hideLoading();
                }
            });
        }

        else if (status == 4){

            ApiClient.getApiService().getDataHistory(String.valueOf(status), iduser, token, device).enqueue(new Callback<ResponseHistory>() {
                @Override
                public void onResponse(Call<ResponseHistory> call, Response<ResponseHistory> response) {
                    historyView.hideLoading();

                    if (response.isSuccessful()){

                        boolean result = response.body().getResult();
                        String msg = response.body().getMsg();

                        if (result){
                            historyView.showMsg(msg);
                            dataHistoryComplete = response.body().getData();
                            historyView.dataHistory(dataHistoryComplete);
                        }
                        else{
                            historyView.showMsg(msg);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseHistory> call, Throwable t) {
                    historyView.showError(t.getLocalizedMessage());
                    historyView.hideLoading();
                }
            });
        }
    }

    @Override
    public void onAttach(BaseView view) {
        this.view = view;
    }

    @Override
    public void onDetach() {
        this.view = null;
    }
}


