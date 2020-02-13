package com.example.discordfixed.Entidades.Logica;

import com.example.discordfixed.Entidades.Firebase.Mensaje;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LMensaje {
    private Mensaje mensaje;
    private String key;
    private LUsuario lusuario;

    public LMensaje(Mensaje mensaje, String key) {
        this.mensaje = mensaje;
        this.key = key;
    }

    public LUsuario getLusuario() {
        return lusuario;
    }

    public Mensaje getMensaje() {
        return mensaje;
    }

    public void setMensaje(Mensaje mensaje) {
        this.mensaje = mensaje;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setLusuario(LUsuario lusuario) {
        this.lusuario = lusuario;
    }

    public Long getCreatedTimestampLong(){
        return (long) mensaje.getCreateTimestamp();
    }

    public String fechaCreacionMensaje(){
        Date d = new Date(getCreatedTimestampLong());
        PrettyTime prettyTime = new PrettyTime(new Date(),Locale.getDefault());
        return prettyTime.format(d);
        //SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        //return sdf.format(d);
    }
}
