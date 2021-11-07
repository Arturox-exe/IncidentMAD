package dte.masteriot.mdp.finalproyect_mobiledevices.mqtt;

public class User {
  
    int id;
    String nombre;
    String password;


    public User(int _id,String _nombre,String _password){
        id=_id;
        nombre=_nombre;
        password=_password;
    }


    @Override
    public String toString() {
        return nombre;
    }


    public int getId(){
        return id;
    }

    public String getNombre(){
        return nombre;
    }

    public String getPassword(){
        return password;
    }
}
