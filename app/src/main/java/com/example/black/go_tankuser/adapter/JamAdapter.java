package com.example.black.go_tankuser.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.black.go_tankuser.R;
import com.example.black.go_tankuser.Reservasi;
import com.example.black.go_tankuser.model.JamModel;

import java.util.List;

public class JamAdapter extends RecyclerView.Adapter<JamAdapter.ViewHolder> {

    private List<JamModel> jamModelList;
    private Context context;

    public JamAdapter(List<JamModel> jamModelList, Context context) {
        this.jamModelList = jamModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public JamAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_jam, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull JamAdapter.ViewHolder holder, int position) {
        holder.binding(jamModelList.get(position), context);

    }

    @Override
    public int getItemCount() {
        return jamModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tx_jam;
        public ViewHolder(View itemView) {
            super(itemView);
            tx_jam = itemView.findViewById(R.id.tv_jam);

        }

        void binding(final JamModel jamModel, final Context context){
            tx_jam.setText(jamModel.getJam());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Reservasi.edt_jam.setText(String.valueOf(jamModel.getId()));
//                    Reservasi.edt_jam.setText(jamModel.getJam());
//                    Reservasi.JAM_ID = jamModel.getId();
                }
            });
        }
    }
}
