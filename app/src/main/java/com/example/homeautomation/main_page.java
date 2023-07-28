package com.example.homeautomation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class main_page extends AppCompatActivity {

    LottieAnimationView splash_screen;
    String cmd;
    private static final int REQUEST_CODE_SPEECH_INPUT = 100;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    TextView t2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        SharedPreferences sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        String flag = sharedPreferences.getString("flag", "0");
        boolean f = sharedPreferences.getBoolean("f", true);
        ed.apply();

        Calendar calendar = Calendar.getInstance();
        t2 = findViewById(R.id.t2);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        splash_screen = findViewById(R.id.splach_screen);
        splash_screen.setZ(Float.MAX_VALUE);

        FloatingActionButton sleep_mode = findViewById(R.id.sleep_btn);
        FloatingActionButton exit_mode = findViewById(R.id.exit_btn);
        Button living = findViewById(R.id.Living);
        Button bed = findViewById(R.id.Bed_Rooms);
        Button kitchen = findViewById(R.id.Kitchen);
        Button home = findViewById(R.id.Home);
        ImageButton list = findViewById(R.id.list);
        ImageButton mic_btn = findViewById(R.id.mic_btn);
        ImageButton logout_btn = findViewById(R.id.logout_btn);
        String app_id = sharedPreferences.getString("AppID", null);
        myRef = database.getReference(app_id);

        if (flag.equals("1") && f) {
            splash_screen.setVisibility(View.VISIBLE);
            splash_screen.addAnimatorListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(@NonNull Animator animation) {

                }

                @Override
                public void onAnimationEnd(@NonNull Animator animation) {
                    splash_screen.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationCancel(@NonNull Animator animation) {
                    splash_screen.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(@NonNull Animator animation) {

                }
            });
        }


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
                Intent i = new Intent(main_page.this, MainActivity.class);
                startActivity(i);
            }
        });
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(main_page.this, ListActivity.class));
            }
        });


        living.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(main_page.this, Living.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(main_page.this);
            }
        });
        bed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(main_page.this, Bed_rooms.class);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(main_page.this);
            }
        });
        kitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(main_page.this, Kitchen.class);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(main_page.this);
            }
        });

        sleep_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode(myRef, "1", "1");
                View Toast_custom = getLayoutInflater().inflate(R.layout.toast, (ViewGroup) findViewById(R.id.main));
                TextView textView = Toast_custom.findViewById(R.id.textView);
                LottieAnimationView lottieAnimationView = Toast_custom.findViewById(R.id.lottieFile);
                textView.setText("Night Mode Is On, Have a Good Night");
                lottieAnimationView.setAnimation("sleeping_mode.json");
                Toast t = new Toast(getApplicationContext());
                t.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 200);
                t.setDuration(Toast.LENGTH_SHORT);
                t.setView(Toast_custom);
                t.show();
            }
        });
        exit_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode(myRef, "0", "0");
                View Toast_custom = getLayoutInflater().inflate(R.layout.toast, (ViewGroup) findViewById(R.id.main));
                TextView textView = Toast_custom.findViewById(R.id.textView);
                LottieAnimationView lottieAnimationView = Toast_custom.findViewById(R.id.lottieFile);
                textView.setText("Exit Mode Is On, Your All Appliances are Off");
                lottieAnimationView.setAnimation("exit_mode.json");
                Toast t = new Toast(getApplicationContext());
                t.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 200);
                t.setDuration(Toast.LENGTH_SHORT);
                t.setView(Toast_custom);
                t.show();
            }
        });


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
                    Toast.makeText(main_page.this, "Low connection", Toast.LENGTH_SHORT).show();

            }
        });
        reference.child("Living").updateChildren(living_data, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error != null)
                    Toast.makeText(main_page.this, "Low connection", Toast.LENGTH_SHORT).show();
            }
        });
        reference.child("Kitchen").updateChildren(kitchen_data, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error != null)
                    Toast.makeText(main_page.this, "Low connection", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void startSpeechRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now");
        startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            cmd = results.get(0).toLowerCase();
            if (cmd.equals("exit mode") || cmd.equals("turn on exit mode") || cmd.equals("start exit mode")) {
                mode(myRef, "0", "0");
            } else if (cmd.equals("sleep mode") || cmd.equals("turn on sleep mode") || cmd.equals("start sleep mode")) {
                mode(myRef, "1", "1");
            }
            // Bed Rooms
            else if (cmd.equals("bedroom one light") || cmd.equals("light one") || cmd.equals("start light one") || cmd.equals("on the light one") || cmd.equals("start the light one") || cmd.equals("bedroom 1 light") || cmd.equals("light 1") || cmd.equals("start light 1") || cmd.equals("on the light 1") || cmd.equals("start the light 1")) {
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

            // Living

            else if (cmd.equals("living light") || cmd.equals("start living light") || cmd.equals("on living light") || cmd.equals("living light start")) {
                send_data("Living", "L1", true, myRef);
            } else if (cmd.equals("living fan") || cmd.equals("start living fan") || cmd.equals("on living fan") || cmd.equals("living fan start")) {
                send_data("Living", "F1", true, myRef);
            } else if (cmd.equals("living light off") || cmd.equals("living light of") || cmd.equals("off living light") || cmd.equals("of living light")) {
                send_data("Living", "L1", false, myRef);
            } else if (cmd.equals("living fan off") || cmd.equals("living fan of") || cmd.equals("off living fan") || cmd.equals("of living fan")) {
                send_data("Living", "F1", false, myRef);
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


    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    void send_data(String field, String value, boolean b, DatabaseReference reference) {
        if (b) {
            Map<String, Object> bed_rooms_data = new HashMap<>();
            bed_rooms_data.put(value, "1");
            reference.child(field).updateChildren(bed_rooms_data, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if (error != null)
                        Toast.makeText(main_page.this, "Low connection", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Map<String, Object> bed_rooms_data = new HashMap<>();
            bed_rooms_data.put(value, "0");
            reference.child(field).updateChildren(bed_rooms_data, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if (error != null)
                        Toast.makeText(main_page.this, "Low connection", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}