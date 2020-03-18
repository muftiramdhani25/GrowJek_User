package net.growdev.userojekonline.view.trackingdriver;

import net.growdev.userojekonline.base.BasePresenter;
import net.growdev.userojekonline.base.BaseView;
import net.growdev.userojekonline.model.modeltracking.DataItem;

import java.util.List;

public interface TrackingDriverContract {

    interface View extends BaseView<BasePresenter> {
        void showMsg(String msg);
        void showLoading(String msgLoading);
        void hideLoading();
        void showError(String localizedMessage);
        void getDataDriver(List<DataItem> dataDriver);
    }

    interface Presenter extends BasePresenter {
        void getDetailDriver(String idDriver);
    }

}
