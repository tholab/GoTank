package com.example.black.go_tankuser.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.black.go_tankuser.DetailCv;
import com.example.black.go_tankuser.R;
import com.example.black.go_tankuser.model.ComentUser;
import com.example.black.go_tankuser.model.KomentarModel;
import com.example.black.go_tankuser.utils.ApiUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ComentUserAdapter extends RecyclerView.Adapter<ComentUserAdapter.ViewHolder> {

    private List<KomentarModel> comentUserList;
    private Context context;

    public ComentUserAdapter(List<KomentarModel> comentUserList, Context context) {
        this.comentUserList = comentUserList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_coment, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding(comentUserList.get(position), context);
    }

    @Override
    public int getItemCount() {
        return comentUserList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout ln_komen;
        private TextView tv_namaKomen, tv_KomenUser;
        private CircleImageView iv_UserKomen;
        public ViewHolder(View itemView) {
            super(itemView);
            ln_komen = itemView.findViewById(R.id.ln_komen);
            tv_namaKomen = itemView.findViewById(R.id.namaKomenUser);
            tv_KomenUser = itemView.findViewById(R.id.komenUser);
            iv_UserKomen = itemView.findViewById(R.id.iv_KomenUser);
        }

        void binding(final KomentarModel komentarModel, final Context context){
            if (komentarModel.getKomentar() != null){
                tv_namaKomen.setText(komentarModel.getName());
                tv_KomenUser.setText(komentarModel.getKomentar());
                Glide.with(context)
                        .load(ApiUtils.ENDPOINT+"img/"+komentarModel.getAvatar())
                        .into(iv_UserKomen);
            }else {
                ln_komen.setVisibility(View.GONE);
            }


        }
    }

}
