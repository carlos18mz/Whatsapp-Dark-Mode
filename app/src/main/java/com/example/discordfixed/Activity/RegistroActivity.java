package com.example.discordfixed.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.discordfixed.Entidades.Firebase.Mensaje;
import com.example.discordfixed.Entidades.Firebase.Usuario;
import com.example.discordfixed.Persistencia.UsuarioDAO;
import com.example.discordfixed.R;
import com.example.discordfixed.Utilidades.Constantes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kbeanie.multipicker.api.CacheLocation;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;

import java.io.File;
import java.net.URI;
import java.util.List;

public class RegistroActivity extends AppCompatActivity {

    private EditText txtNombre, txtCorreo, txtContraseña, txtContraseñaRepetida;
    private Button btnRegistrar;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private ImageView fotoPerfil;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private ImagePicker imagePicker;
    private CameraImagePicker cameraImagePicker;
    private String pickerPath;
    Uri u;
    Uri fotoPerilUri;
    String imagePath="";


    private static final int PHOTO_PERFIL = 2;
    private static final int REQUEST_CODE_CAMERA = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        getSupportActionBar().hide();
        txtNombre = (EditText)findViewById(R.id.idCorreoLogin);
        txtCorreo = (EditText)findViewById(R.id.idContraseñaLogin);
        txtContraseña = (EditText)findViewById(R.id.idRegistroContraseña);
        txtContraseñaRepetida = (EditText)findViewById(R.id.idRegistroContraseñaRepetida);
        btnRegistrar = (Button)findViewById(R.id.idRegistroRegistrar);
        fotoPerfil = (ImageView)findViewById(R.id.idFotoPerfilSet);

        imagePicker = new ImagePicker(RegistroActivity.this);
        cameraImagePicker = new CameraImagePicker(RegistroActivity.this);
        cameraImagePicker.setCacheLocation(CacheLocation.EXTERNAL_STORAGE_APP_DIR);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            final String email = txtCorreo.getText().toString();
            String nombre = txtNombre.getText().toString();
                if(isValidEmail(email) && validPassword() && validarNombre(nombre))
                {
                    String password = txtContraseña.getText().toString();
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegistroActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        if(fotoPerilUri != null)
                                        {
                                            UsuarioDAO.getInstancia().subirFotoUri(fotoPerilUri, new UsuarioDAO.IDevolverUrlFoto() {
                                                @Override
                                                public void devolerUrlString(String url) { //Asincrona
                                                    Usuario usuario = new Usuario();
                                                    usuario.setCorreo(txtCorreo.getText().toString());
                                                    usuario.setNombre(txtNombre.getText().toString());
                                                    usuario.setFotoPerfilURL(url);
                                                    FirebaseUser currentUser = mAuth.getCurrentUser();
                                                    DatabaseReference reference = database.getReference("Usuarios/" + currentUser.getUid());
                                                    reference.setValue(usuario);
                                                    Toast.makeText(getApplicationContext(), "Se registro correctamente", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(RegistroActivity.this, LoginActivity.class));
                                                    finish();
                                                }
                                            });
                                        }else{
                                            Usuario usuario = new Usuario();
                                            usuario.setCorreo(txtCorreo.getText().toString());
                                            usuario.setNombre(txtNombre.getText().toString());
                                            usuario.setFotoPerfilURL(Constantes.URL_FOTO_POR_DEFECTO_USUARIOS);
                                            FirebaseUser currentUser = mAuth.getCurrentUser();
                                            DatabaseReference reference = database.getReference("Usuarios/" + currentUser.getUid());
                                            reference.setValue(usuario);
                                            Toast.makeText(getApplicationContext(), "Se registro correctamente", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(RegistroActivity.this, LoginActivity.class));
                                            finish();
                                        }
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(getApplicationContext(),"Error al registrarse",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else{
                    Toast.makeText(getApplicationContext(),"Correo o contraseña incorrectos",Toast.LENGTH_SHORT).show();
                }
            }
        });

        cameraImagePicker.setImagePickerCallback(new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                String path_c = list.get(0).getOriginalPath();
                fotoPerilUri = Uri.fromFile(new File(path_c));
                fotoPerfil.setImageURI(fotoPerilUri);
            }

            @Override
            public void onError(String s) {
                Toast.makeText(getApplicationContext(),"Error : "+s,Toast.LENGTH_SHORT).show();
            }
        });

        imagePicker.setImagePickerCallback(new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                if(!list.isEmpty()){
                    String path_c = list.get(0).getOriginalPath();
                    fotoPerilUri = Uri.parse(path_c);
                    fotoPerfil.setImageURI(fotoPerilUri);
                }
            }

            @Override
            public void onError(String s) {
                Toast.makeText(RegistroActivity.this, "Error: "+s, Toast.LENGTH_SHORT).show();
            }
        });


        fotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(RegistroActivity.this, R.style.MyDialogTheme);
                dialog.setTitle("Foto de perfil");

                String[] items = {"Galeria","Camara"};

                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                Intent it = new Intent(Intent.ACTION_PICK);
                                it.setType("image/*");
                                startActivityForResult(Intent.createChooser(it,"Selecciona una foto"),PHOTO_PERFIL);
                                break;
                            case 1:
                                pickerPath = cameraImagePicker.pickImage();
                                break;
                        }
                    }
                });

                AlertDialog dialogConstruido = dialog.create();
                dialogConstruido.show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PHOTO_PERFIL && resultCode == RESULT_OK)
        {
            u = data.getData();
            imagePath = u.toString();
            fotoPerilUri = Uri.parse(imagePath);
            fotoPerfil.setImageURI(u);
        }

        if(requestCode == Picker.PICK_IMAGE_CAMERA && resultCode == RESULT_OK){
            cameraImagePicker.reinitialize(pickerPath);
            cameraImagePicker.submit(data);
        }
    }

    public final static boolean isValidEmail(CharSequence target)
    {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public Boolean validPassword()
    {
        String password1 = txtContraseña.getText().toString();
        String password2 = txtContraseñaRepetida.getText().toString();

        if(password1.equals(password2))
        {
            if(password1.length()>=6&&password1.length()<=16)
            {
                return true;
            }
            else{Toast.makeText(getApplicationContext(),"La contraseña debe tener al menos 6 digitos y como maximo 16 digitos",Toast.LENGTH_SHORT).show();
            return false;
            }
        }else {
            Toast.makeText(getApplicationContext(),"Las contraseñas no coinciden",Toast.LENGTH_SHORT).show();
            return false;
        }
    }



    public boolean validarNombre(String nombre){
        return !nombre.isEmpty();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // You have to save path in case your activity is killed.
        // In such a scenario, you will need to re-initialize the CameraImagePicker
        outState.putString("picker_path", pickerPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // After Activity recreate, you need to re-intialize these
        // two values to be able to re-intialize CameraImagePicker
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("picker_path")) {
                pickerPath = savedInstanceState.getString("picker_path");
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }


}
