package dte.masteriot.mdp.finalproyect_mobiledevices.mqtt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import dte.masteriot.mdp.finalproyect_mobiledevices.R;

public class LoginActivity extends AppCompatActivity {

    Button btLogin;
    EditText eLogin, ePassword;
    private MyOpenHelper db;
    private User userdb;

    private ArrayList<User> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btLogin= (Button) findViewById(R.id.login_button);

    }

    public void onLoginClick(View view){
        db = new MyOpenHelper(this);
        eLogin = (EditText) findViewById(R.id.eUser);
        ePassword = (EditText) findViewById(R.id.ePassword);


        if(!eLogin.getText().toString().isEmpty()) {
            String user = eLogin.getText().toString();
            String password = ePassword.getText().toString();

            userdb = db.query(user);


            if(userdb != null) {
                if(userdb.getPassword().equals(password)) {
                    Intent i = new Intent(this, MqttClient.class);
                    i.putExtra("user", user);
                    startActivity(i);
                }
                else{
                    Toast Tlogin = Toast.makeText(getApplicationContext(),"The name or the password is incorrect", Toast.LENGTH_SHORT);
                    Tlogin.show();
                }
            }

            else{
                Toast Tlogin = Toast.makeText(getApplicationContext(),"The name or the password is incorrect", Toast.LENGTH_SHORT);
                Tlogin.show();
            }

        }
        else{
            Toast Tlogin = Toast.makeText(getApplicationContext(),"Your user name can't be empty", Toast.LENGTH_SHORT);
            Tlogin.show();
        }

    }

    public void onRegisterClick(View view){
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }


}
