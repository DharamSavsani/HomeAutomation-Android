package com.example.homeautomation;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// Raiderfont
public class MainActivity extends AppCompatActivity {
    LottieAnimationView anim,loding,splash_screen;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loding = findViewById(R.id.loding);
        anim = findViewById(R.id.login_anim);
        View main = findViewById(R.id.main);
        splash_screen = findViewById(R.id.splach_screen);
        anim.playAnimation();
        Button signin_btn = findViewById(R.id.signin_btn);
        EditText app_id_input = findViewById(R.id.app_id_input);
        EditText pass_input = findViewById(R.id.pass_input);
        SharedPreferences sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        Intent main_intent = new Intent(MainActivity.this, main_page.class);
        String  flag = sharedPreferences.getString("flag","0");
        if (flag.equals("1")){
            startActivity(main_intent);
            ed.putBoolean("f",true);
            ed.apply();
        }
        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pass_input.getText().toString().isEmpty() && app_id_input.getText().toString().isEmpty()){
                    pass_input.setError("Enter the Password");
                    app_id_input.setError("Enter the Application ID");
                }
                else if(app_id_input.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Enter the value", Toast.LENGTH_SHORT).show();
                    app_id_input.setError("Enter the Application ID");
                }
                else if(pass_input.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Enter the pass", Toast.LENGTH_SHORT).show();
                    pass_input.setError("Enter the Password");
                }
                else {
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                    if (networkInfo != null && networkInfo.isConnected()) {
                        loding.setVisibility(View.VISIBLE);
                        String id_input = app_id_input.getText().toString();
                        String psw_input = pass_input.getText().toString();
                        DatabaseReference myRef = database.getReference(id_input);
                        ed.putString("AppID",id_input);
                        ed.apply();
                        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().exists()) {
                                        DataSnapshot ds = task.getResult();
                                        String ID = ds.child("ID").getValue(String.class);
                                        String Pass = ds.child("Password").getValue(String.class);
                                        if(!ID.equals(id_input)){
                                            app_id_input.setError("Enter the valid Appliction ID");
                                            app_id_input.setText("");
                                            loding.setVisibility(View.INVISIBLE);
                                        }
                                        if(!Pass.equals(pass_input)){
                                            pass_input.setError("Enter the valid Password");
                                            loding.setVisibility(View.INVISIBLE);
                                        }
                                        if (ID.equals(id_input) && Pass.equals(psw_input)) {
                                            signin_btn.setVisibility(View.GONE);
                                            anim.setVisibility(View.GONE);
                                            pass_input.setVisibility(View.GONE);
                                            app_id_input.setVisibility(GONE);
                                            splash_screen.setVisibility(View.VISIBLE);
                                            splash_screen.addAnimatorListener(new Animator.AnimatorListener() {
                                                @Override
                                                public void onAnimationStart(@NonNull Animator animation) {

                                                }

                                                @Override
                                                public void onAnimationEnd(@NonNull Animator animation) {
                                                    ed.putString("flag", "1");
                                                    ed.putBoolean("f",false);
                                                    ed.apply();
                                                    startActivity(main_intent);
                                                }

                                                @Override
                                                public void onAnimationCancel(@NonNull Animator animation) {
                                                    ed.putString("flag", "1");
                                                    ed.apply();
                                                    startActivity(main_intent);
                                                }

                                                @Override
                                                public void onAnimationRepeat(@NonNull Animator animation) {

                                                }
                                            });

                                        }
                                    } else {
                                        app_id_input.setError("Enter the valid Appliction ID");
                                        app_id_input.setText("");
                                        pass_input.setError("Enter the valid Password");
                                        pass_input.setText("");
                                        loding.setVisibility(View.INVISIBLE);
                                        Toast.makeText(MainActivity.this, "Your data is not Found", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, "Poor connection", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }     
            }
        });
    }
    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}