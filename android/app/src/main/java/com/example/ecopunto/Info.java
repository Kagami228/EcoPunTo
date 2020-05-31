package com.example.ecopunto;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Info extends AppCompatActivity {
    private boolean isImageScaled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        final TextView textView_1=findViewById(R.id.textView6);

        ImageView img_1 = findViewById(R.id.image3);
        img_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isImageScaled) {
                    textView_1.animate().scaleX(1f).scaleY(0.5f).setDuration(500);
                    v.animate().scaleX(1.6f).scaleY(1.6f).setDuration(500);
                }
                if (isImageScaled) {

                    textView_1.animate().scaleX(1f).scaleY(1f).setDuration(500);

                    v.animate().scaleX(1f).scaleY(1f).setDuration(500);
                }
                isImageScaled = !isImageScaled;

            }
        });
        final TextView textView_2= findViewById(R.id.textView7);

        ImageView img_2 =  findViewById(R.id.image4);
        img_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isImageScaled) {
                    textView_2.animate().scaleX(1f).scaleY(0.5f).setDuration(500);
                    v.animate().scaleX(1.6f).scaleY(1.6f).setDuration(500);
                }
                if (isImageScaled) {

                    textView_2.animate().scaleX(1f).scaleY(1f).setDuration(500);

                    v.animate().scaleX(1f).scaleY(1f).setDuration(500);
                }
                isImageScaled = !isImageScaled;

            }
        });
        final TextView textView_3= findViewById(R.id.textView5);

        ImageView img_3 = findViewById(R.id.image2);
        img_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isImageScaled) {
                    textView_3.animate().scaleX(1f).scaleY(0.5f).setDuration(500);
                    v.animate().scaleX(1.6f).scaleY(1.6f).setDuration(500);
                }
                if (isImageScaled) {

                    textView_3.animate().scaleX(1f).scaleY(1f).setDuration(500);

                    v.animate().scaleX(1f).scaleY(1f).setDuration(500);
                }
                isImageScaled = !isImageScaled;

            }
        });
        final TextView textView_4= findViewById(R.id.textView4);

        ImageView img_4 = findViewById(R.id.image1);
        img_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isImageScaled) {
                    textView_4.animate().scaleX(1f).scaleY(0.5f).setDuration(500);
                    v.animate().scaleX(1.6f).scaleY(1.6f).setDuration(500);
                }
                if (isImageScaled) {

                    textView_4.animate().scaleX(1f).scaleY(1f).setDuration(500);

                    v.animate().scaleX(1f).scaleY(1f).setDuration(500);
                }
                isImageScaled = !isImageScaled;

            }
        });
    }
}