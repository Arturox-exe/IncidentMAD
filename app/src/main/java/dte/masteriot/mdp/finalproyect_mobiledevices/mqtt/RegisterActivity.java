package dte.masteriot.mdp.finalproyect_mobiledevices.mqtt;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import dte.masteriot.mdp.finalproyect_mobiledevices.R;

public class RegisterActivity extends AppCompatActivity {

    Button btRegister;
    EditText eRegister, ePassword, eRPassword;
    private MyOpenHelper db;
    private User userdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btRegister = (Button) findViewById(R.id.register_button);

    }

    public void onRegisterClick(View view){
        db = new MyOpenHelper(this);
        eRegister = (EditText) findViewById(R.id.eRegisterUser);
        ePassword = (EditText) findViewById(R.id.ePasswordUser);
        eRPassword = (EditText) findViewById(R.id.eRPasswordUser);

        if(!eRegister.getText().toString().isEmpty()) {
            String user = eRegister.getText().toString();
            String password = ePassword.getText().toString();
            String rpassword = eRPassword.getText().toString();

            if(password.equals(rpassword)){
                Boolean message = db.insertar(user, password);


                if (message) {
                    Intent i = new Intent(this, PahoExampleActivity.class);
                    i.putExtra("user", user);
                    startActivity(i);
                }

                else{
                    Toast Tregister = Toast.makeText(getApplicationContext(),"The user name is already in use, try another", Toast.LENGTH_SHORT);
                    Tregister.show();
                }
            }

            else {
                Toast Tregister = Toast.makeText(getApplicationContext(),"The password must be equal", Toast.LENGTH_SHORT);
                Tregister.show();
            }


        }

        else{
            Toast Tregister = Toast.makeText(getApplicationContext(),"Your user name can't be empty", Toast.LENGTH_SHORT);
            Tregister.show();
        }

    }
}
