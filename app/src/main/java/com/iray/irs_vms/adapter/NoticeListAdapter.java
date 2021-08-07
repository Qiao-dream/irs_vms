package com.iray.irs_vms.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iray.irs_vms.R;
import com.iray.irs_vms.activity.NoticeDetailActivity;
import com.iray.irs_vms.activity.NoticesActivity;
import com.iray.irs_vms.info.NoticeInfo;

import java.lang.ref.WeakReference;
import java.util.List;

public class NoticeListAdapter extends RecyclerView.Adapter<NoticeListAdapter.ViewHolder> {
    public static String TAG="NoticeListAdapter";
    private final LayoutInflater mLayoutInflater;
    private List<NoticeInfo> mNoticesInfoList;
    private WeakReference<Context> reference;
    private NoticesActivity.NoticeListHandler handler;
    private Context mContext;
    private String ID;



    public NoticeListAdapter(Context context, List<NoticeInfo> mNoticeInfoList, NoticesActivity.NoticeListHandler noticeListHandler) {
        mContext=context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mNoticesInfoList = mNoticeInfoList;
        this.reference = new WeakReference<Context>(context);
        this.handler = noticeListHandler;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.notice_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.tv_notice_name.setText(mNoticesInfoList.get(position).getHeadline());
        holder.tv_notice_value.setText(mNoticesInfoList.get(position).getSubhead());
        holder.tv_notice_time.setText(mNoticesInfoList.get(position).getTime());
        holder.tv_notice_name.setOnClickListener(mOnClickListener);
        ID=mNoticesInfoList.get(position).getId();
        Log.w(TAG,"notice id:"+ID);


    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //跳转到通知详情页面
                case R.id.tv_notice_name:

                    Intent intent = new Intent(mContext, NoticeDetailActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("id",ID);
                    mContext.startActivity(intent);
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    public int getItemCount() {
        return mNoticesInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View item;
        TextView tv_notice_name;
        TextView tv_notice_value;
        TextView tv_notice_time;

        ViewHolder(View view) {
            super(view);
            item = view;

            tv_notice_name = view.findViewById(R.id.tv_notice_name);
            tv_notice_value = view.findViewById(R.id.tv_notice_value);
            tv_notice_time = view.findViewById(R.id.tv_notice_time);

        }

    }

}
