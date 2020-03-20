package net.growdev.userojekonline.view.history;

import net.growdev.userojekonline.base.BasePresenter;
import net.growdev.userojekonline.base.BaseView;
import net.growdev.userojekonline.model.modelhistory.DataHistory;

import java.util.List;

public interface HistoryContract {

    interface View extends BaseView<BasePresenter> {
        void showMsg(String msg);
        void showLoading(String msgLoading);
        void hideLoading();
        void showError(String localizedMessage);
        void dataHistory(List<DataHistory> dataHistory);
    }

    interface Presenter extends BasePresenter {
        void getDataHistory(int status, String token, String device, String iduser);
    }


}
