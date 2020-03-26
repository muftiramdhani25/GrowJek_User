package net.growdev.userojekonline.base;

public interface BasePresenter<T extends BaseView> {

    void onAttach(T view);
    void onDetach();

}
