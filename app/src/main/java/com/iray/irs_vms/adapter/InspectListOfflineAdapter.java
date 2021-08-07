package com.iray.irs_vms.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iray.irs_vms.R;
import com.iray.irs_vms.activity.AlarmTaskActivity;
import com.iray.irs_vms.activity.AlarmUploadActivity;
import com.iray.irs_vms.activity.InspectionActivity;
import com.iray.irs_vms.info.InspectInfo;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class InspectListOfflineAdapter extends RecyclerView.Adapter<InspectListOfflineAdapter.ViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private List<InspectInfo> mInspectInfoList;
    private WeakReference<Context> reference;
    private Context context;
    private InspectionActivity.OfflineInspectListHandler handler;
    private String id;


    public InspectListOfflineAdapter(Context context, List<InspectInfo> mInspectInfoList, InspectionActivity.OfflineInspectListHandler inspectListHandler) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mInspectInfoList = mInspectInfoList;
        this.reference = new WeakReference<Context>(context);
        this.handler = inspectListHandler;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        return new ViewHolder(mLayoutInflater.inflate(R.layout.offline_inspect_list_item, parent, false));
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.inpsect_item_value.setText(mInspectInfoList.get(position).getName());

        //设置时间格式
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        /*try {
            Date date =  stringToDate(mInspectInfoList.get(position).getTime(),"yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(date);
            holder.tv_task_time.setText(dateString );
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        holder.tv_task_time.setText(mInspectInfoList.get(position).getTime());
        holder.tv_inspect_description.setText(mInspectInfoList.get(position).getTaskDescription());
        if(mInspectInfoList.get(position).getPollingStateV().equals("未完成")) {
            //PollingState 0:已完成   1：未完成
            holder.iv_finish.setImageResource(R.drawable.inspect_unfinish);
            holder.btn_task.setVisibility(View.VISIBLE);
        }else{
            holder.iv_finish.setImageResource(R.drawable.inspect_finish);
            holder.btn_task.setVisibility(View.INVISIBLE);
            holder.btn_upload.setVisibility(View.INVISIBLE);

        }
        id=mInspectInfoList.get(position).getId();
        Log.w("InspectListOfflineAdapter","id:_______________________________"+id);

        holder.btn_task.setOnClickListener(mOnClickListener);
        holder.btn_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id=mInspectInfoList.get(position).getId();
                Log.w("InspectListOfflineAdapter","id2:_______________________________"+id);
               /* Message msg = new Message();
                msg.what = InspectionActivity.HANDLER_GET_INSPECT_ID;
                Bundle b = new Bundle();
                b.putString("id", mInspectInfoList.get(position).getId());

                msg.setData(b);
                handler.sendMessage(msg);*/
                Intent intent1 = new Intent();
                intent1.putExtra("id",id);
                intent1.setClass(context, AlarmTaskActivity.class);
                context.startActivity(intent1);
            }
        });
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @SuppressLint("LongLogTag")
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_upload:
                    Intent intent0 = new Intent();
                    intent0.setClass(context,AlarmUploadActivity.class);
                    context.startActivity(intent0);
                    break;
                case R.id.btn_begintask:
                    /*Intent intent1 = new Intent();
                    intent1.putExtra("id",id);
                    intent1.setClass(context, AlarmTaskActivity.class);
                    context.startActivity(intent1);*/
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    public int getItemCount() {
        return mInspectInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View item;
        ImageView iv_finish;
        TextView inspect_item;
        TextView inpsect_item_value;
        TextView tv_task_time;
        TextView tv_inspect_description;
        TextView textView_sxt_name;
        Button btn_upload;
        Button btn_task;

        ViewHolder(View view) {
            super(view);
            item = view;
            iv_finish = view.findViewById(R.id.iv_unfinish);
            inspect_item = view.findViewById(R.id.inspect_item);
            inpsect_item_value = view.findViewById(R.id.inpsect_item_value);
            tv_task_time = view.findViewById(R.id.tv_task_time);
            tv_inspect_description = view.findViewById(R.id.tv_inspect_description);
            textView_sxt_name = view.findViewById(R.id.textView_sxt_name);
            btn_upload = view.findViewById(R.id.btn_upload);
            btn_upload.setOnClickListener(mOnClickListener);
            btn_task=view.findViewById(R.id.btn_begintask);
            btn_task.setOnClickListener(mOnClickListener);
        }
    }

    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

}
