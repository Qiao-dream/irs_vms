package com.iray.irs_vms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iray.irs_vms.R;

import java.util.List;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.ViewHolder> {
    private final LayoutInflater mLayoutInflater;


    public DeviceListAdapter(Context context) {
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.device_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivDeviceItem;
        TextView tvDeviceOrg;
        TextView tvDeviceName;
        TextView tvDeviceType;
        TextView tvDeviceTransport;
        TextView tvIconOnline;
        TextView tvOnline;

        ViewHolder(View view) {
            super(view);
            ivDeviceItem = view.findViewById(R.id.iv_device_item);
            tvDeviceOrg = view.findViewById(R.id.tv_device_org);
            tvDeviceName = view.findViewById(R.id.tv_device_name);
            tvDeviceType = view.findViewById(R.id.tv_device_type);
            tvDeviceTransport = view.findViewById(R.id.tv_device_transport);
            tvIconOnline = view.findViewById(R.id.tv_icon_online);
            tvOnline = view.findViewById(R.id.tv_online);
        }

    }
}
