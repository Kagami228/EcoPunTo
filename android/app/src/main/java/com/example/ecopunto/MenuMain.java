package com.example.ecopunto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu_main);
        addListenerOnButton();
    }

    public void addListenerOnButton() {
        Button buttoninfo;
        buttoninfo = findViewById(R.id.buttoninfo);
        buttoninfo.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent =new Intent(MenuMain.this, Info.class);
                        startActivity(intent);
                    }

                }
        );
        Button buttonMap;
        buttonMap = findViewById(R.id.buttonmap);
        buttonMap.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent =new Intent(MenuMain.this, MapsActivity.class);
                        startActivity(intent);

                    }

                }
        );
        Button buttonSet;
        buttonSet = findViewById(R.id.buttonSet);
        buttonSet.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(".Planner");
                        startActivity(intent);

                    }

                }
        );
        Button buttonAny;
        buttonAny = findViewById(R.id.buttonany);
        buttonAny.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(".Settings");
                        startActivity(intent);

                    }

                }
        );


    }



}
