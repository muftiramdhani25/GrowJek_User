package net.growdev.userojekonline.view.history;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.growdev.userojekonline.R;
import net.growdev.userojekonline.helper.HeroHelper;
import net.growdev.userojekonline.helper.SessionManager;
import net.growdev.userojekonline.model.modelhistory.DataHistory;
import net.growdev.userojekonline.view.history.adapter.CustomHistoryAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment implements HistoryContract.View {

    int status;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    HistoryPresenter presenter;
    private SessionManager session;
    private ProgressDialog loading;

    public HistoryFragment(int i) {
        status = i;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, v);
        presenter = new HistoryPresenter(this);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        session = new SessionManager(getActivity());
        loading = new ProgressDialog(getActivity());

        String token = session.getToken();
        String iduser = session.getIdUser();
        String device = HeroHelper.getDeviceUUID(getActivity());

        presenter.getDataHistory(status, token, device, iduser);
    }

    @Override
    public void showMsg(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading(String msgLoading) {
        loading.setTitle("proses " + msgLoading);
        loading.setMessage(msgLoading);
        loading.show();
    }

    @Override
    public void hideLoading() {
        loading.dismiss();
    }

    @Override
    public void showError(String localizedMessage) {
        Toast.makeText(getActivity(), localizedMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void dataHistory(List<DataHistory> dataHistory) {

        CustomHistoryAdapter adapter = new CustomHistoryAdapter(getActivity(), dataHistory, status);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.setAdapter(adapter);
    }

    @Override
    public void onAttachView() {
        presenter.onAttach(this);
    }

    @Override
    public void onDetachView() {
        presenter.onDetach();
    }

    @Override
    public void onStart() {
        super.onStart();
        onAttachView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onDetachView();
    }
}


