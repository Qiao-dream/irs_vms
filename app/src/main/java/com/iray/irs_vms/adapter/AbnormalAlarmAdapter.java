package com.iray.irs_vms.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iray.irs_vms.R;
import com.iray.irs_vms.activity.AbnormalAlarmActivity;
import com.iray.irs_vms.activity.AlarmInfoActivity;
import com.iray.irs_vms.info.AbnormalAlarmInfo;

import java.lang.ref.WeakReference;
import java.util.List;

public class AbnormalAlarmAdapter extends RecyclerView.Adapter<AbnormalAlarmAdapter.ViewHolder> {
    private LayoutInflater mLayoutInflater = null;
    private List<AbnormalAlarmInfo> mAlarmInfoList;
    private WeakReference<Context> reference;
    private AbnormalAlarmActivity.AlarmListHandler handler;
    private Context context;
    String rtspUrl;
    String eventType;
    String id;
    private static String TAG="AbnormalAlarmAdapter";

    public AbnormalAlarmAdapter(Context context, List<AbnormalAlarmInfo> mAlarmInfoList, AbnormalAlarmActivity.AlarmListHandler alarmListHandler) {
        try {
            this.mLayoutInflater = LayoutInflater.from(context);
            this.mAlarmInfoList = mAlarmInfoList;
            this.reference = new WeakReference<Context>(context);
            this.handler = alarmListHandler;
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(mLayoutInflater.inflate(R.layout.alarm_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.tv_fire.setText(mAlarmInfoList.get(position).getEventName());
        holder.tv_position.setText(mAlarmInfoList.get(position).getDetail());
        holder.tv_time.setText(mAlarmInfoList.get(position).getEventTimeStamp());
        holder.tv_level.setText(mAlarmInfoList.get(position).getEventType());

        if((mAlarmInfoList.get(position).getEventType().equals("10001"))){
            holder.iv_fire.setImageResource(R.drawable.btn_alarm_fire);
        }
        if((mAlarmInfoList.get(position).getEventType().equals("10002"))){
            holder.iv_fire.setImageResource(R.drawable.btn_alarm_banxian);
        }
        if((mAlarmInfoList.get(position).getEventType().equals("10003"))){
            holder.iv_fire.setImageResource(R.drawable.btn_alarm_ruqin);
        }
        if((mAlarmInfoList.get(position).getEventType().equals("10004"))){
            holder.iv_fire.setImageResource(R.drawable.btn_alarm_temp);
        }
        if((mAlarmInfoList.get(position).getEventType().equals("10005"))){
            holder.iv_fire.setImageResource(R.drawable.btn_alarm_stranger);
        }
        if((mAlarmInfoList.get(position).getEventType().equals("10006"))){
            holder.iv_fire.setImageResource(R.drawable.btn_alarm_humantemp);
        }
        if((mAlarmInfoList.get(position).getEventType().equals("10007"))){
            holder.iv_fire.setImageResource(R.drawable.btn_alarm_mask);
        }
        if((mAlarmInfoList.get(position).getEventType().equals("10008"))){
            holder.iv_fire.setImageResource(R.drawable.btn_alarm_other);
        }


        holder.tv_descrip.setText(mAlarmInfoList.get(position).getDetail());
        holder.iv_detail.setOnClickListener(mOnClickListener);
        rtspUrl = mAlarmInfoList.get(position).getRtspUrl();
        id= mAlarmInfoList.get(position).getId();
        eventType=mAlarmInfoList.get(position).getEventType();
        Log.w(TAG,"rtspUrl:"+rtspUrl);
        Log.w(TAG,"id:"+id);
        Log.w(TAG,"eventType:"+eventType);
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_detail:
                    Intent intent0 = new Intent();
                    intent0.putExtra("rtsp", rtspUrl);
                    intent0.putExtra("id", id);
                    intent0.putExtra("eventType", eventType);
                    intent0.setClass(context, AlarmInfoActivity.class);
                    context.startActivity(intent0);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public int getItemCount() {
        return mAlarmInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View item;
        ImageView iv_fire;
        TextView tv_fire;
        ImageView iv_detail;
        TextView tv_position;
        TextView tv_time;
        TextView tv_level;
        TextView tv_descrip;
        ImageView iv_action;
        TextView tv_action;

        ViewHolder(View view) {
            super(view);
            item = view;
            iv_fire= view.findViewById(R.id.iv_fire);
            tv_fire = view.findViewById(R.id.tv_fire);
            tv_position = view.findViewById(R.id.tv_position);
            tv_time = view.findViewById(R.id.tv_time);
            tv_level = view.findViewById(R.id.tv_level);
            tv_descrip = view.findViewById(R.id.tv_descrip);
            iv_action = view.findViewById(R.id.iv_action);
            tv_descrip = view.findViewById(R.id.tv_descrip);
            tv_action = view.findViewById(R.id.tv_action);
            iv_detail=view.findViewById(R.id.iv_detail);
        }
    }
}
