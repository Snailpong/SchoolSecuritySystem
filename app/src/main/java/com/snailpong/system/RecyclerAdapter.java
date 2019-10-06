package com.snailpong.system;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder>{
    private ArrayList<Data> listData = new ArrayList<>();

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    void setItem(ArrayList<Data> d) {
        listData = d;
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView status;
        private TextView busnumber;
        private TextView time;

        ItemViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.item_student_name);
            status = itemView.findViewById(R.id.item_status);
            busnumber = itemView.findViewById(R.id.item_bus_num);
            time = itemView.findViewById(R.id.item_time);
        }

        void onBind(final Data data) {
            name.setText(data.getName());

            if(data.getStates() != null) status.setText(data.getStates());
            else status.setText("Not found");
            if(data.getBusnumber() == null || data.getBusnumber().equals("")) busnumber.setText("-");
            else busnumber.setText(data.getBusnumber());

            if(data.getTime() != null){
                int times = Integer.parseInt(data.getTime());
                time.setText(String.format("%02d:%02d:%02d",times%86400000/3600000+15,times%3600000/60000,times%60000/1000));
            } else time.setText("-");
            if(data.getStates() != null && data.getStates().equals("Got on Bus")) status.setTextColor(Color.parseColor("#FF7F00"));
            if(data.getStates() != null && data.getStates().equals("Got off Bus")) status.setTextColor(Color.parseColor("#55FF00"));
            if(data.getStates() != null && data.getStates().equals("Walk Off")) status.setTextColor(Color.parseColor("#0055FF"));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), DialogActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Data",data);
                    intent.putExtras(bundle);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
