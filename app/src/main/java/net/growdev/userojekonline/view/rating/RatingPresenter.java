package net.growdev.userojekonline.view.rating;


import net.growdev.userojekonline.base.BaseView;
import net.growdev.userojekonline.model.modelhistory.ResponseHistory;
import net.growdev.userojekonline.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingPresenter implements RatingContract.Presenter {

    RatingContract.View ratingView;
    BaseView view;

    public RatingPresenter(RatingContract.View view) {
        ratingView = view;
    }

    @Override
    public void setRatingDriver(String iduser, String iddriver, String idbooking, String rating, String comment, String token, String device) {
        ratingView.showLoading("proses gift rating driver");
        ApiClient.getApiService().ratingDriver(iduser, iddriver, idbooking, rating, comment, token, device)
                .enqueue(new Callback<ResponseHistory>() {
                    @Override
                    public void onResponse(Call<ResponseHistory> call, Response<ResponseHistory> response) {
                        ratingView.hideLoading();
                        if (response.isSuccessful()) {
                            boolean result = response.body().getResult();
                            String msg = response.body().getMsg();
                            if (result) {
                                ratingView.showMsg(msg);
                                ratingView.back();

                            }
                            else {
                                ratingView.showMsg(msg);
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseHistory> call, Throwable t) {
                        ratingView.showError(t.getLocalizedMessage());
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



