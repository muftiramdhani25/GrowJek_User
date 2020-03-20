package net.growdev.userojekonline.view.rating;

import net.growdev.userojekonline.base.BasePresenter;
import net.growdev.userojekonline.base.BaseView;

public interface RatingContract {

    interface View extends BaseView<BasePresenter> {
        void showLoading(String pesanloading);
        void hideLoading();
        void showError(String localizedMessage);
        void showMsg(String msg);
        void back();
    }

    interface Presenter extends BasePresenter {
        void setRatingDriver(String iduser, String iddriver, String idbooking, String rating, String comment, String token, String device);
    }
}
