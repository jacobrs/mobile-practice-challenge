package com.csgames.diabetus;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    public final MenuActivity parent = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Snackbar.make(findViewById(android.R.id.content), "Connected to Diabetus",
                Snackbar.LENGTH_SHORT).show();

        ((Button) findViewById(R.id.quiz)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent quiz = new Intent(parent, QuizActivity.class);
                startActivity(quiz);
            }
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
