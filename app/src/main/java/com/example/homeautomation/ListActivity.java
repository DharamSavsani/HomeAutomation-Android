package com.example.homeautomation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class ListActivity extends AppCompatActivity {
    TextView t2, livingLightStatus, livingFanStatus, kitchenLightStatus, kitchenFanStatus, bedLightOneStatus, bedLightTwoStatus, bedFanOneStatus, bedFanTwoStatus,NightModeStatus,ExitModeStatus;
    DatabaseReference myRef;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    String llStatus, lfStatus, klStatus, kfStatus, blOneStatus, bfOneStatus, blTwoStatus, bfTwoStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        getSupportActionBar().hide();
        SharedPreferences sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        String app_id = sharedPreferences.getString("AppID", null);
        myRef = database.getReference(app_id);
        Button living = findViewById(R.id.Living);
        Button bed = findViewById(R.id.Bed_Rooms);
        Button kitchen = findViewById(R.id.Kitchen);
        Button home = findViewById(R.id.Home);
        Calendar calendar = Calendar.getInstance();
        t2 = findViewById(R.id.t2);
        livingLightStatus = findViewById(R.id.livingLightStatus);
        livingFanStatus = findViewById(R.id.livingFanStatus);
        kitchenLightStatus = findViewById(R.id.kitchenLightStatus);
        kitchenFanStatus = findViewById(R.id.kitchenFanStatus);
        bedLightOneStatus = findViewById(R.id.bedLightOneStatus);
        bedLightTwoStatus = findViewById(R.id.bedLightTwoStatus);
        bedFanOneStatus = findViewById(R.id.bedFanOneStatus);
        bedFanTwoStatus = findViewById(R.id.bedFanTwoStatus);
        NightModeStatus = findViewById(R.id.NightModeStatus);
        ExitModeStatus = findViewById(R.id.ExitModeStatus);

        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        if (hourOfDay >= 0 && hourOfDay < 12) {
            int a = 0x263A;
            t2.setText("Good Morning!  " + new String(Character.toChars(a)));
        } else if (hourOfDay >= 12 && hourOfDay < 18) {
            int a = 0x1F60E;
            t2.setText("Good Afternoon!  " + new String(Character.toChars(a)));
        } else {
            int a = 0x1F920;
            t2.setText("Good evening!  " + new String(Character.toChars(a)));
        }

        myRef.child("Living").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                llStatus = snapshot.child("L1").getValue(String.class);
                lfStatus = snapshot.child("F1").getValue(String.class);
                if (llStatus.equals("1")) {
                    livingLightStatus.setText("ON");
                    livingLightStatus.setTextColor(Color.rgb(0,255,0));
                    livingLightStatus.setShadowLayer(2,0,0,Color.BLACK);
                } else {
                    livingLightStatus.setText("OFF");
                    livingLightStatus.setTextColor(Color.rgb(255,0,0));
                    livingLightStatus.setShadowLayer(2,0,0,Color.BLACK);
                }
                if (lfStatus.equals("1")) {
                    livingFanStatus.setText("ON");
                    livingFanStatus.setTextColor(Color.rgb(0,255,0));
                    livingFanStatus.setShadowLayer(2,0,0,Color.BLACK);
                } else {
                    livingFanStatus.setText("OFF");
                    livingFanStatus.setTextColor(Color.rgb(255,0,0));
                    livingFanStatus.setShadowLayer(2,0,0,Color.BLACK);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ListActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });
        myRef.child("Kitchen").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                klStatus = snapshot.child("L1").getValue(String.class);
                kfStatus = snapshot.child("F1").getValue(String.class);
                if (klStatus.equals("1")) {
                    kitchenLightStatus.setText("ON");
                    kitchenLightStatus.setTextColor(Color.rgb(0,255,0));
                    kitchenLightStatus.setShadowLayer(2,0,0,Color.BLACK);
                } else {
                    kitchenLightStatus.setText("OFF");
                    kitchenLightStatus.setTextColor(Color.rgb(255,0,0));
                    kitchenLightStatus.setShadowLayer(2,0,0,Color.BLACK);
                }
                if (kfStatus.equals("1")) {
                    kitchenFanStatus.setText("ON");
                    kitchenFanStatus.setTextColor(Color.rgb(0,255,0));
                    kitchenFanStatus.setShadowLayer(2,0,0,Color.BLACK);
                } else {
                    kitchenFanStatus.setText("OFF");
                    kitchenFanStatus.setTextColor(Color.rgb(255,0,0));
                    kitchenFanStatus.setShadowLayer(2,0,0,Color.BLACK);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ListActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });
        myRef.child("Beds").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                blOneStatus = snapshot.child("L1").getValue(String.class);
                bfOneStatus = snapshot.child("F1").getValue(String.class);
                blTwoStatus = snapshot.child("L2").getValue(String.class);
                bfTwoStatus = snapshot.child("F2").getValue(String.class);

                if (blOneStatus.equals("1")) {
                    bedLightOneStatus.setText("ON");
                    bedLightOneStatus.setTextColor(Color.rgb(0,255,0));
                    bedLightOneStatus.setShadowLayer(2,0,0,Color.BLACK);
                } else {
                    bedLightOneStatus.setText("OFF");
                    bedLightOneStatus.setTextColor(Color.rgb(255,0,0));
                    bedLightOneStatus.setShadowLayer(2,0,0,Color.BLACK);
                }
                if (bfOneStatus.equals("1")) {
                    bedFanOneStatus.setText("ON");
                    bedFanOneStatus.setTextColor(Color.rgb(0,255,0));
                    bedFanOneStatus.setShadowLayer(2,0,0,Color.BLACK);
                } else {
                    bedFanOneStatus.setText("OFF");
                    bedFanOneStatus.setTextColor(Color.rgb(255,0,0));
                    bedFanOneStatus.setShadowLayer(2,0,0,Color.BLACK);
                }
                if (blTwoStatus.equals("1")) {
                    bedLightTwoStatus.setText("ON");
                    bedLightTwoStatus.setTextColor(Color.rgb(0,255,0));
                    bedLightTwoStatus.setShadowLayer(2,0,0,Color.BLACK);
                } else {
                    bedLightTwoStatus.setText("OFF");
                    bedLightTwoStatus.setTextColor(Color.rgb(255,0,0));
                    bedLightTwoStatus.setShadowLayer(2,0,0,Color.BLACK);
                }
                if (bfTwoStatus.equals("1")) {
                    bedFanTwoStatus.setText("ON");
                    bedFanTwoStatus.setTextColor(Color.rgb(0,255,0));
                    bedFanTwoStatus.setShadowLayer(2,0,0,Color.BLACK);
                } else {
                    bedFanTwoStatus.setText("OFF");
                    bedFanTwoStatus.setTextColor(Color.rgb(255,0,0));
                    bedFanTwoStatus.setShadowLayer(2,0,0,Color.BLACK);
                }

                try {
                    if (llStatus.equals("0") && lfStatus.equals("0") && klStatus.equals("0") && kfStatus.equals("0") && blOneStatus.equals("0") && blTwoStatus.equals("0") && bfOneStatus.equals("1") && bfTwoStatus.equals("1")) {
                        NightModeStatus.setText("Night Mode On");
                        NightModeStatus.setTextColor(Color.rgb(0,255,0));
                        NightModeStatus.setShadowLayer(2,0,0,Color.BLACK);
                    } else {
                        NightModeStatus.setText("Night Mode Off");
                        NightModeStatus.setTextColor(Color.rgb(255,0,0));
                        NightModeStatus.setShadowLayer(2,0,0,Color.BLACK);
                    }

                    if (llStatus.equals("0") && lfStatus.equals("0") && klStatus.equals("0") && kfStatus.equals("0") && blOneStatus.equals("0") && blTwoStatus.equals("0") && bfOneStatus.equals("0") && bfTwoStatus.equals("0")) {
                        ExitModeStatus.setText("Exit Mode On");
                        ExitModeStatus.setTextColor(Color.rgb(0,255,0));
                        ExitModeStatus.setShadowLayer(2,0,0,Color.BLACK);
                    } else {
                        ExitModeStatus.setText("Exit Mode Off");
                        ExitModeStatus.setTextColor(Color.rgb(255,0,0));
                        ExitModeStatus.setShadowLayer(2,0,0,Color.BLACK);
                    }
                } catch (Exception e) {
                    Toast.makeText(ListActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ListActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });



        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed.putBoolean("f", false);
                ed.apply();
                Intent i = new Intent(ListActivity.this, main_page.class);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideRight(ListActivity.this);
            }
        });
        bed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListActivity.this, Bed_rooms.class);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(ListActivity.this);
            }
        });
        kitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListActivity.this, Kitchen.class);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(ListActivity.this);
            }
        });
        living.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListActivity.this, Living.class);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(ListActivity.this);
            }
        });

    }
}