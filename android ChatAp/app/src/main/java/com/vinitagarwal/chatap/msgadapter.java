package com.vinitagarwal.chatap;

import android.bluetooth.le.ScanSettings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class msgadapter extends RecyclerView.Adapter {
    private static final int typemsgsent = 0;
    private static final int typemsgreceived = 1;
    private LayoutInflater inflater;
    private List<JSONObject> msg = new ArrayList<>();

    public msgadapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    private class sentmsg extends RecyclerView.ViewHolder {
        TextView sentmsgtext;

        public sentmsg(@NonNull View itemView) {
            super(itemView);
            sentmsgtext = itemView.findViewById(R.id.itemsentmsg);

        }
    }

    private class receivedmsg extends RecyclerView.ViewHolder {
        TextView receivedmsgtext;
        TextView nametxt;

        public receivedmsg(@NonNull View itemView) {
            super(itemView);
            receivedmsgtext = itemView.findViewById(R.id.itemreceivedmsg);
            nametxt = itemView.findViewById(R.id.nametxt);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case typemsgsent:
                view = inflater.inflate(R.layout.itemsentmessage, parent, false);
                return new sentmsg(view);
            case typemsgreceived:
                view = inflater.inflate(R.layout.itemreceivemessage, parent, false);
                return new receivedmsg(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        JSONObject getlist = msg.get(position);
        try {
            if (getlist.getBoolean("sent")) {
                sentmsg msgholder = (sentmsg) holder;
                msgholder.sentmsgtext.setText(getlist.getString("msg"));
                
            } else {
                receivedmsg msgholder = (receivedmsg) holder;
                msgholder.receivedmsgtext.setText(getlist.getString("msg"));
                msgholder.nametxt.setText(getlist.getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemViewType(int position) {
        JSONObject getlist = msg.get(position);
        try {
            if (getlist.getBoolean("sent")) {
                return typemsgsent;
            } else {
                return typemsgreceived;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return msg.size();
    }

    public void additem(JSONObject jsonobject) {
        msg.add(jsonobject);
        notifyDataSetChanged();
    }
}
