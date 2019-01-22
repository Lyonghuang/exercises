package com.example.exercises;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button getQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getQuestion = (Button)findViewById(R.id.get_question);
        getQuestion.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_question:
                new NetAsyncTask("http://192.168.1.4:8080/Exercises/NetTest", "请稍等，正在获取题目……", new DoFinally() {
                    @Override
                    public void doFinally(Object o) {
                        String result = (String)o;
                        Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
                    }
                });
        }
    }
}
