package com.iray.irs_vms.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iray.irs_vms.R;
import com.iray.irs_vms.activity.DeviceListActivity;
import com.iray.irs_vms.info.DeviceInfo;

import java.lang.ref.WeakReference;
import java.util.List;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.ViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private List<DeviceInfo> mDeviceInfoList;
    private WeakReference<Context> reference;
    private DeviceListActivity.DeviceListHandler handler;


    public DeviceListAdapter(Context context, List<DeviceInfo> mDeviceInfoList, DeviceListActivity.DeviceListHandler deviceListHandler) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mDeviceInfoList = mDeviceInfoList;
        this.reference = new WeakReference<Context>(context);
        this.handler = deviceListHandler;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.device_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvDeviceOrg.setText(mDeviceInfoList.get(position).getDeviceOrg().equals("null")?"未知区域":mDeviceInfoList.get(position).getDeviceOrg());
        holder.tvDeviceName.setText(mDeviceInfoList.get(position).getDeviceName().equals("null")?"未命名设备":mDeviceInfoList.get(position).getDeviceName());
        holder.tvDeviceTransport.setText(String.format("%s%s", "通道", mDeviceInfoList.get(position).getDeviceTransport()));
        switch (mDeviceInfoList.get(position).getDeviceType()) {
            case 1:
                holder.tvDeviceType.setText(reference.get().getString(R.string.device_type_1));
                break;
            case 2:
                holder.tvDeviceType.setText(reference.get().getString(R.string.device_type_2));
                break;
            case 3:
                holder.tvDeviceType.setText(reference.get().getString(R.string.device_type_3));
                break;
            default:
                holder.tvDeviceType.setText("未知类型");
                break;
        }

        if(mDeviceInfoList.get(position).isDeviceOnline()){
            holder.tvIconOnline.setBackground(reference.get().getDrawable(R.drawable.icon_online));
            holder.tvOnline.setText(reference.get().getString(R.string.device_online));
        } else {
            holder.tvIconOnline.setBackground(reference.get().getDrawable(R.drawable.icon_offline));
            holder.tvOnline.setText(reference.get().getString(R.string.device_offline));
        }

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deviceId = mDeviceInfoList.get(position).getDeviceId();
                Message msg = handler.obtainMessage(DeviceListActivity.HANDLER_GET_DEVICE_ID, deviceId);
                handler.sendMessage(msg);
            }
        });
    }



    @Override
    public int getItemCount() {
        return mDeviceInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View item;
        ImageView ivDeviceItem;
        TextView tvDeviceOrg;
        TextView tvDeviceName;
        TextView tvDeviceType;
        TextView tvDeviceTransport;
        TextView tvIconOnline;
        TextView tvOnline;

        ViewHolder(View view) {
            super(view);
            item = view;
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
