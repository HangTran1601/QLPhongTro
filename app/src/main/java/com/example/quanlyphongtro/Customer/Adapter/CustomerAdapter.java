package com.example.quanlyphongtro.Customer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quanlyphongtro.Customer.Model.CustomerModel;
import com.example.quanlyphongtro.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CustomerAdapter extends BaseAdapter {
    private List<CustomerModel> list = new ArrayList<>();
    private Context context;

    public CustomerAdapter(Context context, List<CustomerModel> list) {
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layoutcustomer, parent, false);
            holder = new ViewHolder();
            holder.textViewName = convertView.findViewById(R.id.textViewName);
            holder.textViewPhone = convertView.findViewById(R.id.textViewPhone);
            holder.textViewOccupation = convertView.findViewById(R.id.textViewOccupation);
            holder.textViewIDCard = convertView.findViewById(R.id.textViewIDCard);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CustomerModel customerModel = list.get(position);
        holder.textViewName.setText("Tên: " + customerModel.getName());
        holder.textViewPhone.setText("Số điện thoại: " + customerModel.getPhone());
        holder.textViewOccupation.setText("Nghề nghiệp: " + customerModel.getJob());
        holder.textViewIDCard.setText("Số CMND: " + customerModel.getCccd());

        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView textViewName;
        TextView textViewPhone;
        TextView textViewOccupation;
        TextView textViewIDCard;
    }
}
