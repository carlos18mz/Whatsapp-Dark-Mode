package com.example.discordfixed.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.discordfixed.Entidades.Logica.LMensaje;
import com.example.discordfixed.Entidades.Logica.LUsuario;
import com.example.discordfixed.Holder.HolderMensajes;
import com.example.discordfixed.Persistencia.UsuarioDAO;
import com.example.discordfixed.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterMensajes extends RecyclerView.Adapter<HolderMensajes> {

    private List<LMensaje> listMensaje = new ArrayList<>();
    private Context c;

    public AdapterMensajes( Context c) {
        this.c = c;
    }

    public int addMensaje(LMensaje m){
        listMensaje.add(m);
        int posicion = listMensaje.size()-1;
        notifyItemInserted(listMensaje.size());//Notifica a que se actualize toda la vista por el nuevo mensaje
        return posicion;
    }

    public void actualizarMensaje(int posicion, LMensaje lMensaje){
        listMensaje.set(posicion, lMensaje);
        notifyItemChanged(posicion);//Notifica que se actualize solo esta posicion que se esta actualizando
    }

    @NonNull
    @Override
    public HolderMensajes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if(viewType == 1) {
            v = LayoutInflater.from(c).inflate(R.layout.card_view_mensajes_receptor, parent, false);
        }else{
            v = LayoutInflater.from(c).inflate(R.layout.card_view_mensajes_emisor, parent, false);
        }return new HolderMensajes(v);

    }

    @Override
    public void onBindViewHolder(@NonNull HolderMensajes holder, int position) {
        LMensaje lmensaje = listMensaje.get(position);
        LUsuario lusuario = lmensaje.getLusuario();

        if(lusuario!= null) {
            holder.getNombre().setText(lusuario.getUsuario().getNombre());
            Glide.with(c).load(lusuario.getUsuario().getFotoPerfilURL()).into(holder.getFotoMensajePerfil());
        }
        holder.getMensaje().setText(lmensaje.getMensaje().getMensaje());
        if(lmensaje.getMensaje().isContieneFoto())
        {
            holder.getFotoMensaje().setVisibility(View.VISIBLE);
            holder.getMensaje().setVisibility(View.VISIBLE);
            Glide.with(c).load(lmensaje.getMensaje().getUrlFoto()).into(holder.getFotoMensaje());
        }
        else if(!lmensaje.getMensaje().isContieneFoto())
        {
            holder.getFotoMensaje().setVisibility(View.GONE);
            holder.getMensaje().setVisibility(View.VISIBLE);
        }
        holder.getHora().setText(lmensaje.fechaCreacionMensaje());
    }

    @Override
    public int getItemCount() {
        return listMensaje.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(listMensaje.get(position).getLusuario()!=null)
        {
            if(listMensaje.get(position).getLusuario().getKey().equals(UsuarioDAO.getInstancia().getKeyUsuario())){
                return 2;
            }
            else return 1;
        }else return -1;

    }
}
