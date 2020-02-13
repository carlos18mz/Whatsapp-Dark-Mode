package com.example.discordfixed.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.discordfixed.Entidades.Firebase.Usuario;
import com.example.discordfixed.Entidades.Logica.LUsuario;
import com.example.discordfixed.Holder.HolderUsuarios;
import com.example.discordfixed.R;
import com.example.discordfixed.Utilidades.Constantes;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class VerUsuariosActivity extends AppCompatActivity {

    private RecyclerView rvUsuarios;
    private FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_usuarios);
        getSupportActionBar().setTitle("Elige el usuario para crear chat");
        rvUsuarios = findViewById(R.id.rvUsuarios);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);//Se debe agregar a un linear layout para poder visualizarlo en la pantalla
        rvUsuarios.setLayoutManager(linearLayoutManager);

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child(Constantes.NODO_USUARIOS);

        FirebaseRecyclerOptions<Usuario> options =
                new FirebaseRecyclerOptions.Builder<Usuario>()
                        .setQuery(query, Usuario.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Usuario, HolderUsuarios>(options) {
            @Override
            public HolderUsuarios onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_usuario, parent, false);
                return new HolderUsuarios(view);
            }

            @Override
            protected void onBindViewHolder(HolderUsuarios holder, int position, final Usuario model) {
                Glide.with(VerUsuariosActivity.this).load(model.getFotoPerfilURL()).into(holder.getFotoPerfil());
                holder.getNombrePerfil().setText(model.getNombre());

                final LUsuario lUsuario = new LUsuario(getSnapshots().getSnapshot(position).getKey(),model);//OBTENIENDO EL LUSUARIO POR EL USUARIO Y LA LLAVE

                holder.getLinearLayoutCompat().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(VerUsuariosActivity.this,MensajeActivity.class);
                        intent.putExtra("key_receptor",lUsuario.getKey());
                        intent.putExtra("imgPerfil",lUsuario.getUsuario().getFotoPerfilURL());
                        intent.putExtra("nombreReceptor",lUsuario.getUsuario().getNombre());
                        startActivity(intent);
                    }
                });
            }
        };
        rvUsuarios.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
