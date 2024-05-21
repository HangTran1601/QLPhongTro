package com.example.quanlyphongtro.Customer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quanlyphongtro.Customer.Model.RoomModel;
import com.example.quanlyphongtro.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RoomAdapter extends BaseAdapter {
    private List<RoomModel> list = new ArrayList<>();
    private Context context;

    public RoomAdapter(Context context, List<RoomModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.customlistroom, parent, false);
        RoomModel roomModel = list.get(position);
        ImageView imageView = itemView.findViewById(R.id.room_image); // Sử dụng itemView ở đây
        TextView address = itemView.findViewById(R.id.room_address); // Sử dụng itemView ở đây
        TextView price = itemView.findViewById(R.id.room_price); // Sử dụng itemView ở đây
        Picasso.get()
                .load(roomModel.getImage())
                .into(imageView);
        address.setText("Địa chỉ phòng: " + roomModel.getRoomAddress());
        price.setText("Giá phòng: " + roomModel.getRoomPrice());
        return itemView;
    }
}
