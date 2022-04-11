package com.example.easygoal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private HashMap<String, String> passwords;
    private String user;
    private Activity containerActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        passwords = new HashMap<String, String>();
        setContentView(R.layout.activity_main);
        containerActivity = this;
        user = "";
        ((TextView) findViewById(R.id.accLabel)).setText(user);
        fullScreen();
    }

    public void fullScreen() {
        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    private void callLoginDialog()
    {
        Dialog myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.activity_sign_in);
        myDialog.setCancelable(false);
        Button login = (Button) myDialog.findViewById(R.id.signin);
        Button register = (Button) myDialog.findViewById(R.id.register);
        TextView feedback = (TextView) myDialog.findViewById(R.id.feedback);
        myDialog.show();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText uBox = (EditText) myDialog.findViewById(R.id.username);
                String username = uBox.getText().toString();
                EditText pBox = (EditText) myDialog.findViewById(R.id.password);
                String password = pBox.getText().toString();
                String pw = passwords.get(username);
                if(username.equals("") || password.equals("")) {
                    feedback.setTextColor(0xffff0000);
                    feedback.setText("Error: Username and password cannot be blank.");
                } else {
                    if(pw == null || !(pw.equals(password))) {
                        feedback.setTextColor(0xffff0000);
                        feedback.setText("Error: Incorrect username or password.");
                    } else {
                        feedback.setTextColor(0xff00ff00);
                        feedback.setText("Logging in, one second...");
                        user = username;
                        ((TextView) findViewById(R.id.accLabel)).setText(user);
                        Handler handler = new Handler();
                        handler.postDelayed(() -> { myDialog.hide(); }, 1500);
                    }
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText uBox = (EditText) myDialog.findViewById(R.id.username);
                String username = uBox.getText().toString();
                EditText pBox = (EditText) myDialog.findViewById(R.id.password);
                String password = pBox.getText().toString();
                TextView feedback = (TextView) myDialog.findViewById(R.id.feedback);
                if(username.equals("") || password.equals("")) {
                    feedback.setTextColor(0xffff0000);
                    feedback.setText("Error: Username and password cannot be blank.");
                } else {
                    feedback.setTextColor(0xff00ff00);
                    feedback.setText("Registering, one second...");
                    passwords.put(username, password);
                    System.out.println(username);
                    user = username;
                    ((TextView) containerActivity.findViewById(R.id.accLabel)).setText(user);
                    Handler handler = new Handler();
                    handler.postDelayed(() -> { myDialog.hide(); }, 1500);
                }
            }
        });

    }

    public void newTask(View view) {
        if(user == "") {
            new AlertDialog.Builder(this)
                    .setTitle("Please Sign In")
                    .setMessage(R.string.warning)
                    .setPositiveButton("OK",null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }
        Intent intent = new Intent(this, NewTaskActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    public void signIn(View view) {
        callLoginDialog();
    }
}