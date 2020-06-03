package com.example.ecopunto;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MenuMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_menu_main);
        addListenerOnButton();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);
        if (firstStart) {
            showStartDialog();
        }
    }
    private void showStartDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Welcome to Eco-Punto!")
                .setMessage("Map - You can see all the tanks nearby, Info - Information how to recycle properly, Notification - Ability to set notifications, Any- in the future")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
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
                        Intent intent = new Intent(".Any");
                        startActivity(intent);

                    }

                }
        );


    }



}
