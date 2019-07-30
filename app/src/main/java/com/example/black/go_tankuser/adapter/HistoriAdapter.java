package com.example.black.go_tankuser.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.black.go_tankuser.Detail_histori;
import com.example.black.go_tankuser.model.PesanModel;
import com.example.black.go_tankuser.R;
import com.example.black.go_tankuser.utils.ApiUtils;

import java.util.List;

public class HistoriAdapter extends RecyclerView.Adapter<HistoriAdapter.ProductViewHolder> {

    private Context context;
    private List<PesanModel> productHistoriList;

    public HistoriAdapter(Context context, List<PesanModel> productHistoriList) {
        this.context = context;
        this.productHistoriList = productHistoriList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_histori,null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.binding(productHistoriList.get(position), context);
    }

    @Override
    public int getItemCount() {
        return productHistoriList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView txtNama, txtTgl_pesen, txtStatus;

        public ProductViewHolder(View itemView){
            super(itemView);

            imageView = itemView.findViewById(R.id.imageViewHistori);
            txtNama = itemView.findViewById(R.id.textNama);
            txtTgl_pesen = itemView.findViewById(R.id.textTglPemesanan);
            txtStatus = itemView.findViewById(R.id.textStatus);
        }

        void binding(final PesanModel pesanModel, final Context context){
            txtNama.setText(pesanModel.getName());
            txtTgl_pesen.setText(pesanModel.getTgl_pesan());
            txtStatus.setText(pesanModel.getStatus());
            Glide.with(context)
                    .load(ApiUtils.ENDPOINT+"img/"+pesanModel.getAvatar())
                    .placeholder(R.drawable.no_image)
                    .error(R.drawable.no_image)
                    .into(imageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, company.getName()+" tapped", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, Detail_histori.class);
                    intent.putExtra("ID", pesanModel.getId());
                    //Toast.makeText(context, ""+pesanModel.getId(), Toast.LENGTH_SHORT).show();
                    context.startActivity(intent);
                }
            });
        }
    }


}
