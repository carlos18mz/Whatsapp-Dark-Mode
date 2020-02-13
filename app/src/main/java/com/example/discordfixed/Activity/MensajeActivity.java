package com.example.discordfixed.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.discordfixed.Adaptadores.AdapterMensajes;
import com.example.discordfixed.Entidades.Firebase.Mensaje;
import com.example.discordfixed.Entidades.Firebase.Usuario;
import com.example.discordfixed.Entidades.Logica.LMensaje;
import com.example.discordfixed.Entidades.Logica.LUsuario;
import com.example.discordfixed.Persistencia.MensajesDAO;
import com.example.discordfixed.Persistencia.UsuarioDAO;
import com.example.discordfixed.R;
import com.example.discordfixed.Utilidades.Constantes;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MensajeActivity extends AppCompatActivity {

    private CircleImageView fotoperfil;
    private TextView nombre;
    private RecyclerView rvMensajes;
    private EditText txtMensajes;
    private Button btnEnviar;
    private ImageButton cerrarSesion;
    private AdapterMensajes adapter;
    private ImageButton btnEnviarFoto;
    private String fotoPerfilCadena;
    private Spinner menuOpciones;

    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;

    private static final int PHOTO_SENT = 1;
    private static final int PHOTO_PERFIL = 2;
    private String KEY_RECEPTOR="";
    private String NOMBRE_USUARIO="";
    private String NOMBRE_RECEPTOR="";
    private String FOTO_RECEPTOR="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensaje);
        getSupportActionBar().hide();

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            KEY_RECEPTOR = bundle.getString("key_receptor");
            NOMBRE_RECEPTOR = bundle.getString("nombreReceptor");
            FOTO_RECEPTOR = bundle.getString("imgPerfil");
        }else finish();

        fotoperfil = (CircleImageView)findViewById(R.id.fotoPerfil);
        nombre = (TextView)findViewById(R.id.nombre);
        rvMensajes = (RecyclerView)findViewById(R.id.rvMensajes);
        txtMensajes = (EditText)findViewById(R.id.txtMensaje);
        btnEnviar = (Button)findViewById(R.id.btnEnviar);
        cerrarSesion = (ImageButton)findViewById(R.id.cerrarSesion);
        btnEnviarFoto = (ImageButton)findViewById(R.id.btnEnviarFoto);
        fotoPerfilCadena = "";
        menuOpciones = (Spinner)findViewById(R.id.spmenuOpciones);

        adapter = new AdapterMensajes(this);
        LinearLayoutManager l = new LinearLayoutManager(this);
        rvMensajes.setLayoutManager(l);
        rvMensajes.setAdapter(adapter);

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();

        String [] opc_menu = {"Cambiar foto de perfil", "Volver al chat principal", "Banearlos a todos", "Cerrar Sesión"};
        ArrayAdapter<String> adapter_menuSalir = new ArrayAdapter<String>(this, R.layout.spinner1, opc_menu);
        menuOpciones.setAdapter(adapter_menuSalir);
        menuOpciones.setSelection(0,false);


        menuOpciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item.toString().equals("Cerrar Sesión")) {
                    FirebaseAuth.getInstance().signOut();
                    returnLogin();
                }
                if(item.toString().equals("Cambiar foto de perfil"))
                {
                    Intent i = new Intent(Intent.ACTION_PICK);
                    i.setType("image/*");
                    startActivityForResult(Intent.createChooser(i,"Selecciona una foto"),PHOTO_PERFIL);
                }
                if(item.toString().equals("Banearlos a todos"))
                {
                    Toast.makeText(getApplicationContext(),"Disponible en la versión Premiun",Toast.LENGTH_SHORT).show();
                }
                if(item.toString().equals("Volver al chat principal")){
                    startActivity(new Intent(MensajeActivity.this,MensajeDefaultActivity.class));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mensaje m = new Mensaje();
                String t = txtMensajes.getText().toString();
                if(!t.isEmpty())
                {
                    m.setMensaje(t);
                    m.setContieneFoto(false);
                    m.setKeyEmisor(UsuarioDAO.getInstancia().getKeyUsuario());
                    MensajesDAO.getInstancia().nuevoMensaje(UsuarioDAO.getInstancia().getKeyUsuario(),KEY_RECEPTOR,m);
                    txtMensajes.setText("");
                }

            }
        });

        btnEnviarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                Intent it = new Intent(Intent.ACTION_PICK);
                it.setType("image/*");
                startActivityForResult(Intent.createChooser(it,"Selecciona una foto"),PHOTO_PERFIL);
            }
        });

        fotoperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i,"Selecciona una foto"),PHOTO_PERFIL);
            }
        });

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollbar();
            }
        });

        FirebaseDatabase.getInstance().getReference(Constantes.NODO_MENSAJES)
                .child(UsuarioDAO.getInstancia().getKeyUsuario()).child(KEY_RECEPTOR)
                .addChildEventListener(new ChildEventListener() {

            //Traer la informacion del usuario
            //Guardamos la informacion del usuario en una lista temporal
            //obtenemos la infomacion guardada por la llave

            Map<String, LUsuario> mapUsuariosTemporales= new HashMap<>();

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final Mensaje m = dataSnapshot.getValue(Mensaje.class);
                final LMensaje lm = new LMensaje(m,dataSnapshot.getKey());
                final int pos = adapter.addMensaje(lm);

                if(mapUsuariosTemporales.get(m.getKeyEmisor())!=null){
                    lm.setLusuario(mapUsuariosTemporales.get(m.getKeyEmisor()));
                    adapter.actualizarMensaje(pos,lm);
                }else
                {
                    UsuarioDAO.getInstancia().obtenerInformacionDeUsuarioPorLLave(m.getKeyEmisor(), new UsuarioDAO.IDevolverUsuario() {
                        @Override
                        public void devolverUsuario(LUsuario lUsuario) {
                            mapUsuariosTemporales.put(m.getKeyEmisor(), lUsuario);
                            lm.setLusuario(lUsuario);
                            adapter.actualizarMensaje(pos,lm);
                        }

                        @Override
                        public void devolverError(String error) {

                        }
                    });
                }

            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        verifyStoragePermissions(this);

        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                returnLogin();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_1, menu);
        return true;
    }

    public static boolean verifyStoragePermissions(Activity activity) {
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        int REQUEST_EXTERNAL_STORAGE = 1;
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
            return false;
        }else{
            return true;
        }
    }

    private void setScrollbar(){
        rvMensajes.scrollToPosition(adapter.getItemCount()-1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PHOTO_SENT && resultCode == RESULT_OK){
            Uri u = data.getData();
            storageReference = storage.getReference("Imagenes_chat");//Imagenes_chat
            final StorageReference fotoReferencia = storageReference.child(u.getLastPathSegment());

            fotoReferencia.putFile(u).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fotoReferencia.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri uri = task.getResult();
                        Mensaje m = new Mensaje();
                        m.setMensaje(NOMBRE_USUARIO+" ha enviado una foto");
                        m.setUrlFoto(uri.toString());
                        m.setContieneFoto(true);
                        m.setKeyEmisor(UsuarioDAO.getInstancia().getKeyUsuario());
                        MensajesDAO.getInstancia().nuevoMensaje(UsuarioDAO.getInstancia().getKeyUsuario(),KEY_RECEPTOR,m);
                    }
                }
            });
        }
        if(requestCode == PHOTO_PERFIL && resultCode == RESULT_OK)
        {
            Uri u = data.getData();
            fotoperfil.setImageURI(u);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null)
        {
            DatabaseReference reference = database.getReference("Usuarios/"+currentUser.getUid());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                    NOMBRE_USUARIO = usuario.getNombre();
                    nombre.setText(NOMBRE_RECEPTOR);
                    Glide.with(MensajeActivity.this).load(FOTO_RECEPTOR).into(fotoperfil);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else
        {returnLogin();}
    }

    private void returnLogin(){
        startActivity(new Intent(MensajeActivity.this, LoginActivity.class));
        finish();
    }
}
