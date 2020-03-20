package net.growdev.userojekonline.view.waitingdriver;

import net.growdev.userojekonline.base.BasePresenter;
import net.growdev.userojekonline.base.BaseView;

public interface WaitingDriverContract {

    interface View extends BaseView<BasePresenter> {
        void showMessage(String msg);
        void showLoading(String msgLoading);
        void hideLoading();
        void showError(String error);
        void getDataDriver(String idDriver);
        void back();
    }

    interface Presenter extends BasePresenter{
        void cancelBooking(String booking, String token, String idBooking);
        void cekStatusBooking(String idBooking);
    }
}
