package com.example.frameworkfeaturetest.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frameworkfeaturetest.R;

import java.util.List;
import java.util.Map;

public class MainTestListAdapter extends RecyclerView.Adapter {

    private static final String TAG = "MainTestListAdapter";

    private Context mContext;
    private ArrayMap<String, Class> mDataMap;

    public MainTestListAdapter(Context context, ArrayMap<String, Class> data) {
        Log.d(TAG, "MainTestListAdapter: ");
        mContext = context;
        mDataMap = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_test_item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: position=" + position);
        ((MyViewHolder) holder).mTextView.setText(mDataMap.keyAt(position));
        ((MyViewHolder) holder).mTextView.setOnClickListener(v -> {
            mContext.startActivity(new Intent(mContext, mDataMap.valueAt(position)));
        });
    }

    @Override
    public int getItemCount() {
        return mDataMap.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "MyViewHolder: ");
            mTextView = itemView.findViewById(R.id.tv_start_activity);
        }
    }
}
