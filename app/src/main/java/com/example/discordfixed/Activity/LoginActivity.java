package com.example.discordfixed.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.discordfixed.Persistencia.UsuarioDAO;
import com.example.discordfixed.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText txtcorreo, txtcontraseña;
    private Button btnLogin, btnRegistro;
    private FirebaseAuth mAuth;

    public LoginActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        txtcorreo = (EditText) findViewById(R.id.idCorreoLogin);
        txtcontraseña = (EditText) findViewById(R.id.idContraseñaLogin);
        btnLogin = (Button) findViewById(R.id.idLoginlogin);
        btnRegistro = (Button) findViewById(R.id.idRegistroLogin);

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtcorreo.getText().length() > 0 && txtcontraseña.getText().length() > 0) {
                    String correo = txtcorreo.getText().toString();
                    if (isValidEmail(correo) && validPassword()) {
                        String contraseña = txtcontraseña.getText().toString();
                        mAuth.signInWithEmailAndPassword(correo, contraseña).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    nextActivity();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else {
                        Toast.makeText(getApplicationContext(), "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistroActivity.class));
                finish();
            }
        });

        //Solo ejecutar 1 vez, añadir foto de perfil
        //UsuarioDAO.getInstancia().addFotoPerfilDefault();

    }

    @Override
    protected void onResume() {
        super.onResume();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null)
        {
            nextActivity();
        }
    }

    //Verifica si ya te logeaste con alguna cuenta en tu dispositivo
    private void nextActivity(){
        startActivity(new Intent(LoginActivity.this, MensajeDefaultActivity.class));
        finish();
    }

    public final static boolean isValidEmail(CharSequence target)
    {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public Boolean validPassword()
    {
        String password1 = txtcontraseña.getText().toString();
            if(password1.length()>=6&&password1.length()<=16)
            {
                return true;
            }
            else{
                Toast.makeText(getApplicationContext(),"La contraseña debe tener al menos 6 digitos y como maximo 16 digitos",Toast.LENGTH_SHORT).show();
                return false;
            }
    }
}
