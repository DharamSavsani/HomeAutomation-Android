package com.example.homeautomation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Bed_rooms extends AppCompatActivity {
    TextView t2;
    String L1, L2, F1, F2;
    DatabaseReference myRef;
    String cmd;
    private static final int REQUEST_CODE_SPEECH_INPUT = 100;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bed_rooms);
        SharedPreferences sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        String app_id = sharedPreferences.getString("AppID", null);

        myRef = database.getReference(app_id);
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setDuration(1000);

        Switch light1_stc = findViewById(R.id.light1_stc);
        Switch fan1_stc = findViewById(R.id.fan1_stc);
        Switch light2_stc = findViewById(R.id.light2_stc);
        Switch fan2_stc = findViewById(R.id.fan2_stc);
        ImageView light_img = findViewById(R.id.light_img);
        ImageView fan_img = findViewById(R.id.fan_img);
        ImageView light1_img = findViewById(R.id.light1_img);
        ImageView fan1_img = findViewById(R.id.fan1_img);

        Calendar calendar = Calendar.getInstance();
        t2 = findViewById(R.id.t2);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        Button living = findViewById(R.id.Living);
        Button bed = findViewById(R.id.Bed_Rooms);
        Button kitchen = findViewById(R.id.Kitchen);
        Button home = findViewById(R.id.Home);
        ImageButton list = findViewById(R.id.list);
        ImageButton mic_btn = findViewById(R.id.mic_btn);
        ImageButton logout_btn = findViewById(R.id.logout_btn);

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
        mic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSpeechRecognition();
            }
        });
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed.putString("flag", "0");
                ed.apply();
                Intent i = new Intent(Bed_rooms.this, MainActivity.class);
                startActivity(i);
            }
        });

        living.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Bed_rooms.this, Living.class);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideRight(Bed_rooms.this);
            }
        });
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Bed_rooms.this, ListActivity.class));
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed.putBoolean("f", false);
                ed.apply();
                Intent i = new Intent(Bed_rooms.this, main_page.class);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideRight(Bed_rooms.this);
            }
        });
        kitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Bed_rooms.this, Kitchen.class);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(Bed_rooms.this);
            }
        });

        myRef.child("Beds").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                L1 = snapshot.child("L1").getValue(String.class);
                F1 = snapshot.child("F1").getValue(String.class);
                L2 = snapshot.child("L2").getValue(String.class);
                F2 = snapshot.child("F2").getValue(String.class);
                if (L1.equals("1")) {
                    light1_stc.setChecked(true);
                    light_img.setImageResource(R.drawable.light_on);
                } else {
                    light_img.setImageResource(R.drawable.light);
                    light1_stc.setChecked(false);
                }

                if (F1.equals("1")) {
                    fan1_stc.setChecked(true);
                    fan_img.startAnimation(rotateAnimation);
                } else {
                    fan_img.clearAnimation();
                    fan1_stc.setChecked(false);
                }

                if (L2.equals("1")) {
                    light2_stc.setChecked(true);
                    light1_img.setImageResource(R.drawable.light_on);
                } else {
                    fan1_img.clearAnimation();
                    light2_stc.setChecked(false);
                }

                if (F2.equals("1")) {
                    fan2_stc.setChecked(true);
                    fan1_img.startAnimation(rotateAnimation);
                } else {
                    fan1_img.clearAnimation();
                    fan2_stc.setChecked(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Bed_rooms.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });

        light1_stc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (light1_stc.isChecked()) {
                    light_img.setImageResource(R.drawable.light_on);
                    send_data("Beds", "L1", light1_stc.isChecked(), myRef);
                } else {
                    light_img.setImageResource(R.drawable.light);
                    send_data("Beds", "L1", light1_stc.isChecked(), myRef);
                }
            }
        });
        fan1_stc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (fan1_stc.isChecked()) {
                    fan_img.startAnimation(rotateAnimation);
                    send_data("Beds", "F1", fan1_stc.isChecked(), myRef);
                } else {
                    fan_img.clearAnimation();
                    send_data("Beds", "F1", fan1_stc.isChecked(), myRef);
                }
            }
        });
        light2_stc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (light2_stc.isChecked()) {
                    light1_img.setImageResource(R.drawable.light_on);
                    send_data("Beds", "L2", light2_stc.isChecked(), myRef);
                } else {
                    light1_img.setImageResource(R.drawable.light);
                    send_data("Beds", "L2", light2_stc.isChecked(), myRef);
                }
            }
        });
        fan2_stc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (fan2_stc.isChecked()) {
                    fan1_img.startAnimation(rotateAnimation);
                    send_data("Beds", "F2", fan2_stc.isChecked(), myRef);
                } else {
                    fan1_img.clearAnimation();
                    send_data("Beds", "F2", fan2_stc.isChecked(), myRef);
                }
            }
        });
    }


    void send_data(String field, String value, boolean b, DatabaseReference reference) {
        if (b) {
            Map<String, Object> bed_rooms_data = new HashMap<>();
            bed_rooms_data.put(value, "1");
            reference.child(field).updateChildren(bed_rooms_data, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if (error != null)
                        Toast.makeText(Bed_rooms.this, "Low connection", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Map<String, Object> bed_rooms_data = new HashMap<>();
            bed_rooms_data.put(value, "0");
            reference.child(field).updateChildren(bed_rooms_data, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if (error != null)
                        Toast.makeText(Bed_rooms.this, "Low connection", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void startSpeechRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now");
        startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
    }

    void mode(DatabaseReference reference, String F1, String F2) {
        Map<String, Object> bed_rooms_data = new HashMap<>();
        bed_rooms_data.put("L1", "0");
        bed_rooms_data.put("L2", "0");
        bed_rooms_data.put("F1", F1);
        bed_rooms_data.put("F2", F2);

        Map<String, Object> living_data = new HashMap<>();
        living_data.put("L1", "0");
        living_data.put("F1", "0");

        Map<String, Object> kitchen_data = new HashMap<>();
        kitchen_data.put("L1", "0");
        kitchen_data.put("F1", "0");

        reference.child("Beds").updateChildren(bed_rooms_data, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error != null)
                    Toast.makeText(Bed_rooms.this, "Low connection", Toast.LENGTH_SHORT).show();

            }
        });
        reference.child("Living").updateChildren(living_data, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error != null)
                    Toast.makeText(Bed_rooms.this, "Low connection", Toast.LENGTH_SHORT).show();
            }
        });
        reference.child("Kitchen").updateChildren(kitchen_data, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error != null)
                    Toast.makeText(Bed_rooms.this, "Low connection", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            cmd = results.get(0).toLowerCase();
            if (cmd.equals("bedroom one light") || cmd.equals("light one") || cmd.equals("start light one") || cmd.equals("on the light one") || cmd.equals("start the light one") || cmd.equals("bedroom 1 light") || cmd.equals("light 1") || cmd.equals("start light 1") || cmd.equals("on the light 1") || cmd.equals("start the light 1")) {
                send_data("Beds", "L1", true, myRef);
            } else if (cmd.equals("bedroom one fan") || cmd.equals("fan one") || cmd.equals("start fan one") || cmd.equals("on the fan one") || cmd.equals("start the fan one") || cmd.equals("bedroom 1 fan") || cmd.equals("fan 1") || cmd.equals("start fan 1") || cmd.equals("on the fan 1") || cmd.equals("start the fan 1")) {
                send_data("Beds", "F1", true, myRef);
            } else if (cmd.equals("bedroom two light") || cmd.equals("light two") || cmd.equals("start light two") || cmd.equals("on the light two") || cmd.equals("start the light two") || cmd.equals("bedroom 2 light") || cmd.equals("light 2") || cmd.equals("start light 2") || cmd.equals("on the light 2") || cmd.equals("start the light 2")) {
                send_data("Beds", "L2", true, myRef);
            } else if (cmd.equals("bedroom two fan") || cmd.equals("fan two") || cmd.equals("start fan two") || cmd.equals("on the fan two") || cmd.equals("start the fan two") || cmd.equals("bedroom 2 fan") || cmd.equals("fan 2") || cmd.equals("start fan 2") || cmd.equals("on the fan 2") || cmd.equals("start the fan 2")) {
                send_data("Beds", "F2", true, myRef);
            } else if (cmd.equals("bedroom one light off") || cmd.equals("bedroom one light of") || cmd.equals("light one off") || cmd.equals("light one of") || cmd.equals("off the light one") || cmd.equals("of the light one") || cmd.equals("bedroom 1 light off") || cmd.equals("bedroom 1 light of") || cmd.equals("light 1 off") || cmd.equals("light 1 of") || cmd.equals("off the light 1") || cmd.equals("of the light 1")) {
                send_data("Beds", "L1", false, myRef);
            } else if (cmd.equals("bedroom one fan off") || cmd.equals("bedroom one fan of") || cmd.equals("fan one off") || cmd.equals("fan one of") || cmd.equals("off the fan one") || cmd.equals("of the fan one") || cmd.equals("bedroom 1 fan off") || cmd.equals("bedroom 1 fan of") || cmd.equals("fan 1 off") || cmd.equals("fan 1 of") || cmd.equals("off the fan 1") || cmd.equals("of the fan 1")) {
                send_data("Beds", "F1", false, myRef);
            } else if (cmd.equals("bedroom two light off") || cmd.equals("bedroom two light of") || cmd.equals("light two off") || cmd.equals("light two of") || cmd.equals("off the light two") || cmd.equals("of the light two") || cmd.equals("bedroom 2 light off") || cmd.equals("bedroom 2 light of") || cmd.equals("light 2 off") || cmd.equals("light 2 of") || cmd.equals("off the light 2") || cmd.equals("of the light 2")) {
                send_data("Beds", "L2", false, myRef);
            } else if (cmd.equals("bedroom two fan off") || cmd.equals("bedroom two fan of") || cmd.equals("fan two off") || cmd.equals("fan two of") || cmd.equals("off the fan two") || cmd.equals("of the fan two") || cmd.equals("bedroom 2 fan off") || cmd.equals("bedroom 2 fan of") || cmd.equals("fan 2 off") || cmd.equals("fan 2 of") || cmd.equals("off the fan 2") || cmd.equals("of the fan 2")) {
                send_data("Beds", "F2", false, myRef);
            }

            // Modes

            else if (cmd.equals("exit mode") || cmd.equals("turn on exit mode") || cmd.equals("start exit mode")) {
                mode(myRef, "0", "0");
            } else if (cmd.equals("sleep mode") || cmd.equals("turn on sleep mode") || cmd.equals("start sleep mode")) {
                mode(myRef, "1", "1");
            }

            // Kitchen

            else if (cmd.equals("kitchen light") || cmd.equals("start kitchen light") || cmd.equals("on kitchen light") || cmd.equals("kitchen light on")) {
                send_data("Kitchen", "L1", true, myRef);
            } else if (cmd.equals("kitchen fan") || cmd.equals("start kitchen fan") || cmd.equals("on kitchen fan") || cmd.equals("kitchen fan on")) {
                send_data("Kitchen", "F1", true, myRef);
            } else if (cmd.equals("kitchen light off") || cmd.equals("kitchen light of") || cmd.equals("off kitchen light") || cmd.equals("of kitchen light")) {
                send_data("Kitchen", "L1", false, myRef);
            } else if (cmd.equals("kitchen fan off") || cmd.equals("kitchen fan of") || cmd.equals("off kitchen fan") || cmd.equals("of kitchen fan")) {
                send_data("Kitchen", "F1", false, myRef);
            }
            // Living

            else if (cmd.equals("living light") || cmd.equals("start living light") || cmd.equals("on living light") || cmd.equals("living light start")) {
                send_data("Living", "L1", true, myRef);
            } else if (cmd.equals("living fan") || cmd.equals("start living fan") || cmd.equals("on living fan") || cmd.equals("living fan start")) {
                send_data("Living", "F1", true, myRef);
            } else if (cmd.equals("living light off") || cmd.equals("living light of") || cmd.equals("off living light") || cmd.equals("of living light")) {
                send_data("Living", "L1", false, myRef);
            } else if (cmd.equals("living fan off") || cmd.equals("living fan of") || cmd.equals("off living fan") || cmd.equals("of living fan")) {
                send_data("Living", "F1", false, myRef);
            } else {
                View Toast_custom = getLayoutInflater().inflate(R.layout.toast, (ViewGroup) findViewById(R.id.main));
                TextView textView = Toast_custom.findViewById(R.id.textView);
                LottieAnimationView lottieAnimationView = Toast_custom.findViewById(R.id.lottieFile);
                lottieAnimationView.setVisibility(View.GONE);
                textView.setText("Wrong Command 😵😵");
                textView.setPadding(40, 40, 40, 40);
                Toast t = new Toast(getApplicationContext());
                t.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 200);
                t.setDuration(Toast.LENGTH_SHORT);
                t.setView(Toast_custom);
                t.show();
            }

        }
    }
}