package com.iray.irs_vms.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iray.irs_vms.R;
import com.iray.irs_vms.activity.TempCheckActivity;
import com.iray.irs_vms.info.TempCheckInfo;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class TempListAdapter extends RecyclerView.Adapter<TempListAdapter.ViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private List<TempCheckInfo> mTempInfoList;
    private WeakReference<Context> reference;
    private TempCheckActivity.TempListHandler handler;



    public TempListAdapter(Context context, List<TempCheckInfo> mTempInfoList, TempCheckActivity.TempListHandler deviceListHandler) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mTempInfoList = mTempInfoList;
        this.reference = new WeakReference<Context>(context);
        this.handler = deviceListHandler;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.temp_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        new Thread(new Runnable(){
            @Override
            public void run() {
                URL url = null;
                try {
                    url = new URL(mTempInfoList.get(position).getImgUrl());
                    Bitmap bitmap = null;
                    bitmap = BitmapFactory.decodeStream(url.openStream());
                    //更新UI操作；
                    final Bitmap targetBitmap = bitmap;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            holder.iv_temp_item.setImageBitmap(targetBitmap);
                        }
                    });


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        holder.tv_temp_name.setText(mTempInfoList.get(position).getDeviceName());
        holder.tv_temp_value.setText(mTempInfoList.get(position).getTemp());
        holder.tv_temp_kouzhao.setText(mTempInfoList.get(position).getMask());
        holder.tv_temp_time.setText(mTempInfoList.get(position).getEventTimeStamp());
       // holder.tv_temp_area.setText(mTempInfoList.get(position).get);
    }



    @Override
    public int getItemCount() {
        return mTempInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View item;
        ImageView iv_temp_item;
        TextView tv_temp_name;
        TextView tv_temp_value;
        TextView tv_temp_kouzhao;
        TextView tv_temp_time;
        TextView tv_temp_area;

        ViewHolder(View view) {
            super(view);
            item = view;
            iv_temp_item= view.findViewById(R.id.iv_temp_item);
            tv_temp_name = view.findViewById(R.id.tv_temp_name);
            tv_temp_value = view.findViewById(R.id.tv_temp_value);
            tv_temp_kouzhao = view.findViewById(R.id.tv_temp_kouzhao);
            tv_temp_time = view.findViewById(R.id.tv_temp_time);
            tv_temp_area = view.findViewById(R.id.tv_temp_area);
        }

    }

}
