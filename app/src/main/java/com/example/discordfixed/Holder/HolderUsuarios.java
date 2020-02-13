package com.example.discordfixed.Holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discordfixed.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class HolderUsuarios extends RecyclerView.ViewHolder {

    private CircleImageView fotoPerfil;
    private TextView nombrePerfil;
    private LinearLayoutCompat linearLayoutCompat;

    public CircleImageView getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(CircleImageView fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public TextView getNombrePerfil() {
        return nombrePerfil;
    }

    public void setNombrePerfil(TextView nombrePerfil) {
        this.nombrePerfil = nombrePerfil;
    }

    public HolderUsuarios(@NonNull View itemView) {
        super(itemView);

        fotoPerfil = itemView.findViewById(R.id.civFotoPerfil);
        nombrePerfil = itemView.findViewById(R.id.txtNombreUssuario);
        linearLayoutCompat = itemView.findViewById(R.id.layoutPrincipal);

    }

    public LinearLayoutCompat getLinearLayoutCompat() {
        return linearLayoutCompat;
    }

    public void setLinearLayoutCompat(LinearLayoutCompat linearLayoutCompat) {
        this.linearLayoutCompat = linearLayoutCompat;
    }
}
