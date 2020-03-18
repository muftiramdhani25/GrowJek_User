package net.growdev.userojekonline.view.goride;

import net.growdev.userojekonline.base.BasePresenter;
import net.growdev.userojekonline.base.BaseView;

public interface GoRideContract {

    interface View extends BaseView<BasePresenter>{
        void showMessage(String msg);
        void showLoading(String msgLoading);
        void hideLoading();
        void showError(String error);
        void showInfoOrder(String durasi, String jarak, double harga);
        void showDataBooking(String idBooking, String tarif);
        void dataGaris(String dataGaris);
    }

    interface Presenter extends BasePresenter{
        void getDataMap(String lokasiAwal, String lokasiTujuan, String key);
        void requestOrder(String idUser,  String latAwal, String lonAwal, String lokasiAwal,
                          String latTujuan, String lonTujuan, String lokasiTujuan,
                          String catatan, String jarak, String token, String device);
    }
}
