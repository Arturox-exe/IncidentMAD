package dte.masteriot.mdp.IncidentMAD.mqtt;

import androidx.annotation.NonNull;

public class User {
  
    int id;
    String nombre;
    String password;

    public User(int _id,String _nombre,String _password){
        id=_id;
        nombre=_nombre;
        password=_password;
    }

    @NonNull
    @Override
    public String toString() {return nombre;}

    public int getId(){
        return id;
    }

    public String getPassword(){
        return password;
    }
}
