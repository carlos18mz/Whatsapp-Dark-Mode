package com.example.discordfixed.Persistencia;

import com.example.discordfixed.Entidades.Firebase.Mensaje;
import com.example.discordfixed.Utilidades.Constantes;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MensajesDAO {
    private static MensajesDAO mensajesDAO;

    private FirebaseDatabase database;
    private DatabaseReference referenceMensajes;


    public static MensajesDAO getInstancia(){
        if(mensajesDAO==null) mensajesDAO = new MensajesDAO();
        return mensajesDAO;
    }

    private MensajesDAO(){
        database = FirebaseDatabase.getInstance();
        referenceMensajes =database.getReference(Constantes.NODO_MENSAJES);
    }

    public void nuevoMensaje(String key_emisor, String key_receptor, Mensaje mensaje){
        DatabaseReference referenceEmisor = referenceMensajes.child(key_emisor).child(key_receptor); //Busca o crea la carpeta de mensajes ../Emisor/Receptor
        DatabaseReference referenceReceptor = referenceMensajes.child(key_receptor).child(key_emisor); //Busca o crea la carpeta de mensajes ../Receptor/Emisor
        referenceEmisor.push().setValue(mensaje);//guarda el mensaje en la direccion
        referenceReceptor.push().setValue(mensaje);//guarda el mensaje en la direccion
    }
}
