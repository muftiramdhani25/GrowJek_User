package net.growdev.userojekonline.base;

public interface BaseView<T extends BasePresenter> {

    void onAttachView();
    void onDetachView();
}
