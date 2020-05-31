package com.example.ecopunto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class RessetActivity extends AppCompatActivity {
    private Button RessetPasswordEmail;
    private EditText RessetEmailInput;
    private FirebaseAuth mAuth;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resset);
        mAuth = FirebaseAuth.getInstance();

        RessetPasswordEmail = (Button) findViewById(R.id.buttonRes);
        RessetEmailInput = (EditText) findViewById(R.id.editTextRes);

        RessetPasswordEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = RessetEmailInput.getText().toString();
                if (TextUtils.isEmpty(userEmail)) {
                    Toast.makeText(RessetActivity.this, "Please write your valid email address first", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RessetActivity.this, "Please check your Email Account", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RessetActivity.this, MainActivity.class));
                            } else {
                                String messege = Objects.requireNonNull(task.getException()).getMessage();
                                Toast.makeText(RessetActivity.this, "Error Occurred: " + messege, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}