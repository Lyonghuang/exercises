package com.example.exercises;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DoExercise extends AppCompatActivity implements View.OnClickListener {
    public static final String CHECKSTR = "http://192.168.1.4:8080/ExerciseServer/CheckAnswers?answers=";
    public static final String TAG = "DoExercise: ";
    private String questionsStr;
    private int currentIndex = 0;
    private Button preQ;
    private Button nextQ;
    private TextView textQ;
    private RadioGroup option;
    private RadioButton optionA;
    private RadioButton optionB;
    private RadioButton optionC;
    private RadioButton optionD;
    private ProgressDialog progressDialog2;

    private List<Question> questionList = new ArrayList<>();
    private List<Integer> answerList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_exercise);
        preQ = (Button)findViewById(R.id.pre_q);
        nextQ = (Button)findViewById(R.id.next_q);
        textQ = (TextView)findViewById(R.id.q_text);
        option = (RadioGroup)findViewById(R.id.option);
        optionA = (RadioButton)findViewById(R.id.optionA);
        optionB = (RadioButton)findViewById(R.id.optionB);
        optionC = (RadioButton)findViewById(R.id.optionC);
        optionD = (RadioButton)findViewById(R.id.optionD);

        Intent intent = getIntent();
        questionsStr = intent.getStringExtra("questions");

        initQList();

        preQ.setOnClickListener(this);
        nextQ.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pre_q:
                recordAnswer();
                if (currentIndex == 0) {
                    Toast.makeText(DoExercise.this, "这已经是第一题了！", Toast.LENGTH_SHORT).show();
                } else {
                    currentIndex --;
                    loadQuestion(currentIndex);
                }
                break;
            case R.id.next_q:
                recordAnswer();
                if (currentIndex == questionList.size() - 1) {
                    progressDialog2 = new ProgressDialog(DoExercise.this);
                    progressDialog2.setTitle("请稍等");
                    progressDialog2.setMessage("正在提交答案");
                    progressDialog2.setCancelable(false);
                    progressDialog2.show();
                    submitAnswers();
                } else {
                    currentIndex ++;
                    loadQuestion(currentIndex);
                    if (currentIndex == questionList.size() - 1) {
                        nextQ.setText("提交");
                    }
                }
        }
    }

    public void initQList() {
        try {
            JSONArray jsonArray = new JSONArray(questionsStr);
            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String qText = i + 1 + "." + jsonObject.getString("text");
                String answerA = "A." + jsonObject.getString("answerA");
                String answerB = "B." + jsonObject.getString("answerB");
                String answerC = "C." + jsonObject.getString("answerC");
                String answerD = "D." + jsonObject.getString("answerD");
                questionList.add(new Question(qText, answerA, answerB, answerC, answerD));
                answerList.add(0);
            }
            loadQuestion(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadQuestion(int index) {
        option.clearCheck();
        Question question = questionList.get(index);
        textQ.setText(question.getQuestionText());
        optionA.setText(question.getAnswerA());
        optionB.setText(question.getAnswerB());
        optionC.setText(question.getAnswerC());
        optionD.setText(question.getAnswerD());
    }

    public void recordAnswer() {
        switch (option.getCheckedRadioButtonId()) {
            case R.id.optionA:
                answerList.set(currentIndex, 1);
                break;
            case R.id.optionB:
                answerList.set(currentIndex, 2);
                break;
            case R.id.optionC:
                answerList.set(currentIndex, 3);
                break;
            case R.id.optionD:
                answerList.set(currentIndex, 4);
                break;
            default:
                break;
        }
    }

    public void submitAnswers() {
        final StringBuilder answer = new StringBuilder();
        String checkUrl;
        for (int i=0; i<answerList.size(); i++) {
            answer.append(answerList.get(i));
        }
        checkUrl = CHECKSTR + answer.toString();

        new NetAsyncTask(checkUrl, new DoFinally() {
            @Override
            public void doFinally(Object o) {
                progressDialog2.cancel();
                String result = (String)o;
                Intent intent1 = new Intent(DoExercise.this, AnswerResult.class);
                intent1.putExtra("result", result);
                intent1.putExtra("myAnswer", answer.toString());
                startActivity(intent1);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }
}
