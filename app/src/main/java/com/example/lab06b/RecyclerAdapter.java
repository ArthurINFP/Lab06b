package com.example.lab06b;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.BoringLayout;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    public static int REQUEST_CODE = 181;
    Context context;
    LayoutInflater inflater;
    ArrayList<Data> data, enableData, disableData, displayData;

    public RecyclerAdapter(Context context, ArrayList<Data> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
        initData();
        displayData = disableData;
    }

    private void initData() {
        if (enableData == null && disableData == null) {
            enableData = new ArrayList<>();
            disableData = new ArrayList<>();
        }
        for (Data a : data) {
            if (a.isState()) {
                enableData.add(a);
            } else {
                disableData.add(a);
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.update();
    }


    @Override
    public int getItemCount() {
        return displayData.size();
    }

    public void setData(boolean state) {
        displayData = (state) ? enableData : disableData;
        notifyDataSetChanged();
    }

    public void clearData() {
        data.clear();
        displayData.clear();
        enableData.clear();
        disableData.clear();
        notifyDataSetChanged();
    }

    public void addNewData(Data newData) {
        data.add(newData);
        disableData.add(newData);
    }

    public void editData(Data data, Data newData) {
        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).getTitle().equals(data.getTitle())) {
                this.data.set(i, newData);
                break;
            }
        }
        if (data.isState()) {
            for (int i = 0; i < enableData.size(); i++) {
                if (enableData.get(i).getTitle().equals(data.getTitle())) {
                    enableData.set(i, newData);
                    notifyItemChanged(i);
                    break;
                }
            }
        } else {

            for (int i = 0; i < disableData.size(); i++) {
                if (disableData.get(i).getTitle().equals(data.getTitle())) {
                    disableData.set(i, newData);
                    notifyItemChanged(i);
                    break;
                }
            }
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        TextView tvTitle, tvRoom, tvDate, tvTime;
        Switch swState;
        final String[] room = {"C201", "C202", "C203", "C204"};

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvRoom = itemView.findViewById(R.id.tv_room);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvTime = itemView.findViewById(R.id.tv_time);
            swState = itemView.findViewById(R.id.sw_state);
            swState.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        public void update() {
            Data myData = displayData.get(getAdapterPosition());
            tvTitle.setText(myData.getTitle());
            tvRoom.setText(myData.getRoom());
            tvDate.setText(myData.getDate());
            tvTime.setText(myData.getTime());
            swState.setChecked(myData.isState());
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Data selectedData = displayData.get(position);
            boolean state = ((Switch) view).isChecked();
            if (state) {
                disableData.remove(selectedData);
                enableData.add(selectedData);
            } else {
                enableData.remove(selectedData);
                disableData.add(selectedData);
            }
            selectedData.setState(state);
            notifyItemRemoved(position);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            MenuItem op1 = contextMenu.add(Menu.NONE, 1, Menu.NONE, "Edit");
            MenuItem op2 = contextMenu.add(Menu.NONE, 2, Menu.NONE, "Delete");
            op1.setOnMenuItemClickListener(this);
            op2.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
            int position = getAdapterPosition();
            switch (menuItem.getItemId()) {
                case 1:
                    Intent intent = new Intent(context, AddActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", displayData.get(position));
                    intent.putExtras(bundle);
                    ((Activity) context).startActivityForResult(intent, REQUEST_CODE);
                    return true;
                case 2:
                    if (swState.isChecked()) {
                        enableData.remove(position);
                    } else {
                        disableData.remove(position);
                    }
                    notifyItemRemoved(position);
                    return true;
                default:
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    return false;
            }
        }
    }
}
