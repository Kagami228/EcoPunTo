package com.example.ecopunto;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "MAIN";
   // private static final String TAG = "EmailPassword";

    private EditText ETemail;
    private EditText ETpassword;
    private TextView Res;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser(); }
        };

        ETemail = findViewById(R.id.et_email);
        ETpassword = findViewById(R.id.et_password);
        Res=(TextView)findViewById(R.id.resss);
        findViewById(R.id.btn_sign_in).setOnClickListener(this);
        findViewById(R.id.btn_registration).setOnClickListener(this);
        Res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onComplete: found location!");
                Intent intent =new Intent(MainActivity.this, RessetActivity.class);
                startActivity(intent);
            }
        });

        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null){
            //  Intent intent =new Intent(MainMenu.this,MapsActivity.class);
            //   startActivity(intent);
        }

    }


    public void onClick(View view) {
        if(view.getId() == R.id.btn_sign_in)
        {
            sign(ETemail.getText().toString(),ETpassword.getText().toString());
        }else if (view.getId() == R.id.btn_registration)
        {
            registration(ETemail.getText().toString(),ETpassword.getText().toString());
        }

    }

    public void sign(String email , String password)
    {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Aвторизация успешна", Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent(MainActivity.this, MenuMain.class);
                    startActivity(intent);
                }else
                    Toast.makeText(MainActivity.this, "Aвторизация провалена", Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void registration (String email , String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(MainActivity.this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(MainActivity.this, "Регистрация провалена", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
