package com.example.exercises;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AnswerResult extends AppCompatActivity {
    private TextView resultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_result);
        resultView = (TextView)findViewById(R.id.result);

        Intent intent = getIntent();
        String result = intent.getStringExtra("result");
        String myAnswer = intent.getStringExtra("myAnswer");
        int correctNum = 0;

        for (int i=0; i<result.length(); i++) {
            if (result.charAt(i) == 'T') correctNum ++;
        }

        resultView.setText("你一共做了" + result.length() + "题，答对了" + correctNum + "题。");
    }
}
