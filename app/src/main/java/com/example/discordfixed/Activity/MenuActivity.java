package com.example.discordfixed.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.discordfixed.Entidades.Firebase.Usuario;
import com.example.discordfixed.Holder.HolderUsuarios;
import com.example.discordfixed.Persistencia.UsuarioDAO;
import com.example.discordfixed.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MenuActivity extends AppCompatActivity {

    private Button btnVerUsuario;
    private Button btnCerrarSesion;
    private Button btnSalaComun;
    private String NOMBRE_USUARIO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().hide();
        btnVerUsuario = findViewById(R.id.btnVerUsuarios);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnSalaComun = findViewById(R.id.btnSalachat3);

        btnVerUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, VerUsuariosActivity.class));
                finish();
            }
        });

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                returnLogin();
            }
        });

        btnSalaComun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, MensajeDefaultActivity.class));
                finish();
            }
        });

    }

    private void returnLogin(){
        startActivity(new Intent(MenuActivity.this, LoginActivity.class));
        finish();
    }

    protected void onResume() {
        super.onResume();
        if(UsuarioDAO.getInstancia().isuserLogged())
        {

        }else
        {returnLogin();}
    }
}
