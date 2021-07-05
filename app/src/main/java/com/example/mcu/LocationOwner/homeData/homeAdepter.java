package com.example.mcu.LocationOwner.homeData;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mcu.Ip.And.Ordernum.model.ipandordermodel;
import com.example.mcu.R;

import java.util.List;

public class homeAdepter extends RecyclerView.Adapter<homeAdepter.ViewHolder> {

    private final List<ipandordermodel> list;
    private final Context context;

    public homeAdepter(List<ipandordermodel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ips_and_orders, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.set_ips(list.get(position).getIp());
        holder.set_order_number(list.get(position).getOrder());
        holder.ic_setting.setOnClickListener(v -> showAlert(list.get(position).getIp(), String.valueOf(list.get(position).getOrder()), list.get(position).getId()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView ipnum;
        TextView ord_num;
        ImageView ic_setting;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ic_setting = itemView.findViewById(R.id.icon_ip_setting);
            ipnum = itemView.findViewById(R.id.user_ip_home);
            ord_num = itemView.findViewById(R.id.order_num_home);
        }

        void set_ips(String ip) {
            ipnum.setText(String.valueOf(ip));
        }
        void set_order_number(int order) {
            ord_num.setText(String.valueOf(order));
        }
    }

    void showAlert(String ip, String order, String id) {
        new AlertDialog.Builder(this.context)
                .setTitle(ip)
                .setMessage(R.string.are_you_sure_you_want_to_control_this_ip)
                .setIcon(R.drawable.ic_attention)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    Intent intent = new Intent(context, retailer_ip_settingActivity.class);
                    intent.putExtra("ip", ip);
                    intent.putExtra("order", order);
                    intent.putExtra("id", id);
//                        Toast.makeText(context, id, Toast.LENGTH_LONG).show();
                    context.startActivity(intent);

                })
                .setNegativeButton(R.string.no, null)
                .create().show();
    }
}
