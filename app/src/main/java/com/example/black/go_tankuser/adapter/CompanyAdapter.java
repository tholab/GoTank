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
import com.example.black.go_tankuser.DetailCv;
import com.example.black.go_tankuser.R;
import com.example.black.go_tankuser.model.Company;
import com.example.black.go_tankuser.utils.ApiUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.ViewHolder> {
    private List<Company> companies;
    private Context context;

    public CompanyAdapter(List<Company> companies, Context context) {
        this.companies = companies;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding(companies.get(position), context);
    }

    @Override
    public int getItemCount() { return companies.size(); }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name, address;
        private ImageView avatar;
        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvNama);
            address = itemView.findViewById(R.id.tvAlamat);
            avatar = itemView.findViewById(R.id.ivCv);
        }

        void binding(final Company company, final Context context){
            name.setText(company.getName());
            address.setText(company.getAddress());
            Glide.with(context)
                    .load(ApiUtils.ENDPOINT+"img/"+company.getAvatar())
                    //.placeholder(R.drawable.placeholder_gray)
                    .error(R.drawable.no_image)
                    .into(avatar);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, company.getName()+" tapped", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, DetailCv.class);
                    intent.putExtra("ID", company.getId());
                    context.startActivity(intent);
                }
            });
        }
    }
}
