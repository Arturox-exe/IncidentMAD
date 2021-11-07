package dte.masteriot.mdp.finalproyect_mobiledevices.mqtt;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyOpenHelper extends SQLiteOpenHelper {
    private static final String USERS_TABLE_CREATE = "CREATE TABLE users(_id INTEGER PRIMARY KEY AUTOINCREMENT, user TEXT, password TEXT)";
    private static final String DB_NAME = "users.sqlite";
    private static final int DB_VERSION = 1;
    private SQLiteDatabase db = getReadableDatabase();



    public MyOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        db=this.getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USERS_TABLE_CREATE);
        //mockData();
    }

    private void mockData() {
        Log.d("AQUIIIIII", "MockData");
        Boolean hola = insertar("Arturo", "password");
    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //Insertar un nuevo comentario
    public Boolean insertar(String nombre,String password){
        User usr = query(nombre);
        Boolean message;

        if(usr == null) {
            ContentValues cv = new ContentValues();
            cv.put("user", nombre);
            cv.put("password", password);
            db.insert("users", null, cv);
            message = true;
        }
        else{
            message = false;
        }
        return message;
    }

    //Borrar un comentario a partir de su id
    public void delete(int id){
        String[] args = new String[]{String.valueOf(id)};
        db.delete("users", "_id=?", args);
    }

    public User query(String nombre){
        User usr = null;
        String[] args = new String[] {nombre};
        Cursor c = db.rawQuery(" SELECT _id, user, password FROM users WHERE user=? ", args);

        if (c != null && c.getCount()>0) {
            //do {
                c.moveToFirst();
                String user = c.getString(c.getColumnIndexOrThrow("user"));
                String password = c.getString(c.getColumnIndexOrThrow("password"));
                int id = c.getInt(c.getColumnIndexOrThrow("_id"));
                usr = new User(id, user, password);
            //} while (c.moveToNext());
        }


        c.close();
        return usr;
    }


/*
    //Obtener la lista de comentarios en la base de datos
    public ArrayList<User> getUsers(){
        //Creamos el cursor
        ArrayList<User> lista=new ArrayList<User>();
        Cursor c = db.rawQuery("select _id, user,comment from comments", null);
        if (c != null && c.getCount()>0) {
            c.moveToFirst();
            do {
                //Asignamos el valor en nuestras variables para crear un nuevo objeto Comentario
                String user = c.getString(c.getColumnIndexOrThrow("user"));
                String comment = c.getString(c.getColumnIndexOrThrow("password"));
                int id=c.getInt(c.getColumnIndexOrThrow("_id"));
                User com =new User(id,user,comment);
                //Añadimos el comentario a la lista
                lista.add(com);
            } while (c.moveToNext());
        }

        //Cerramos el cursor
        c.close();
        return lista;
    }

*/
}