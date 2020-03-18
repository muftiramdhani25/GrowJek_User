package net.growdev.userojekonline.view.auth;

import net.growdev.userojekonline.base.BasePresenter;
import net.growdev.userojekonline.base.BaseView;
import net.growdev.userojekonline.model.modelauth.ResponseAuth;

public interface AuthContract {

    interface View extends BaseView<BasePresenter>{
        void showMessage(String msg);
        void showLoading();
        void hideLoading();
        void showError(String localizedMessage);
        void pindahActivity();
        void dataUser(ResponseAuth dataUser);
    }

    interface Presenter extends BasePresenter{
        void actionLogin(String uuid, String email, String password);
        void actionRegister(String nama, String email, String password, String phone);
    }
}
