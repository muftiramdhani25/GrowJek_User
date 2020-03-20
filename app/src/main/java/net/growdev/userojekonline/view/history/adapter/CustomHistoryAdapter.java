package net.growdev.userojekonline.view.history.adapter;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import net.growdev.userojekonline.R;
import net.growdev.userojekonline.model.modelhistory.DataHistory;
import net.growdev.userojekonline.view.detailorder.DetailOrderActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static net.growdev.userojekonline.helper.MyContants.INDEX;
import static net.growdev.userojekonline.helper.MyContants.STATUS;

public class CustomHistoryAdapter extends RecyclerView.Adapter<CustomHistoryAdapter.ViewHolder> {

    FragmentActivity activity;
    List<DataHistory> dataHistory;
    int status;

    public CustomHistoryAdapter(FragmentActivity activity, List<DataHistory> dataHistory, int status) {
        this.activity = activity;
        this.dataHistory = dataHistory;
        this.status = status;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.custom_history, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.texttgl.setText(dataHistory.get(position).getBookingTanggal());
        holder.txtawal.setText(dataHistory.get(position).getBookingFrom());
        holder.txtakhir.setText(dataHistory.get(position).getBookingTujuan());
        holder.txtharga.setText(dataHistory.get(position).getBookingBiayaUser());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, DetailOrderActivity.class);
                i.putExtra(INDEX,position);
                i.putExtra(STATUS,status);
                activity.startActivity(i);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataHistory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.texttgl)
        TextView texttgl;
        @BindView(R.id.txtawal)
        TextView txtawal;
        @BindView(R.id.txtakhir)
        TextView txtakhir;
        @BindView(R.id.txtharga)
        TextView txtharga;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}


