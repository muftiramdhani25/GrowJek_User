package net.growdev.userojekonline.view.detailorder;

import net.growdev.userojekonline.base.BasePresenter;
import net.growdev.userojekonline.base.BaseView;

public interface DetailOrderContract {

    interface View extends BaseView<BasePresenter> {
        void showMsg(String msg);
        void showLoading(String msgLoading);
        void hideLoading();
        void showError(String localizedMessage);
        void getDataMap(String dataGaris);
        void pindahHalaman();
    }

    interface Presenter extends BasePresenter {
        void detailRute(String origin, String desti, String key);
        void completeBooking(String iduser, String idbooking, String token, String device);
    }


}
