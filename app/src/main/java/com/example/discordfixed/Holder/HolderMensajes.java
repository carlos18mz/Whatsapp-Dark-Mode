package com.example.discordfixed.Holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.discordfixed.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class HolderMensajes extends androidx.recyclerview.widget.RecyclerView.ViewHolder {

    private TextView nombre;
    private TextView mensaje;
    private TextView hora;
    private CircleImageView fotoMensajePerfil;
    private ImageView fotoMensaje;

    public HolderMensajes(@NonNull View itemView) {
        super(itemView);
        nombre = (TextView)itemView.findViewById(R.id.nombreMensaje);
        mensaje = (TextView)itemView.findViewById(R.id.mensajeMensaje);
        hora = (TextView)itemView.findViewById(R.id.horaMensaje);
        fotoMensajePerfil = (CircleImageView)itemView.findViewById(R.id.fotoPerfilMensaje);
        fotoMensaje = (ImageView)itemView.findViewById(R.id.mensajeFoto);
    }

    public void setNombre(TextView nombre) {
        this.nombre = nombre;
    }

    public void setMensaje(TextView mensaje) {
        this.mensaje = mensaje;
    }

    public void setHora(TextView hora) {
        this.hora = hora;
    }

    public void setFotoMensajePerfil(CircleImageView fotoMensajePerfil) {
        this.fotoMensajePerfil = fotoMensajePerfil;
    }

    public TextView getNombre() {
        return nombre;
    }

    public TextView getMensaje() {
        return mensaje;
    }

    public TextView getHora() {
        return hora;
    }

    public CircleImageView getFotoMensajePerfil() {
        return fotoMensajePerfil;
    }

    public ImageView getFotoMensaje() {
        return fotoMensaje;
    }

    public void setFotoMensaje(ImageView fotoMensaje) {
        this.fotoMensaje = fotoMensaje;
    }
}
