package com.example.photoshop3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class login_admin extends AppCompatActivity {

    EditText editText;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        editText=findViewById(R.id.password_text);
        button=findViewById(R.id.password_btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password=editText.getText().toString();
                if (password.contentEquals("akash.more")){
                    startActivity(new Intent(getApplicationContext(),HomeActivity2.class));
                }
                if (password.contentEquals("12345")){
                    startActivity(new Intent(getApplicationContext(),HomeActivity2.class));
                }else {
                    Toast.makeText(login_admin.this, "Ask a admin password", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }
}