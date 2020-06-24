package com.techasylum.myquize;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static com.techasylum.myquize.StartActivity.EXTRA_CATEGORY_ID;
import static com.techasylum.myquize.StartActivity.EXTRA_CATEGORY_NAME;
import static com.techasylum.myquize.StartActivity.EXTRA_DIFFICULTY;

public class QuizeActivity extends AppCompatActivity {
    public static final long TOTAL_TIME_PER_QUESTION = 30000;
    public static final String KEY_SCORE = "score";
    public static final String KEY_CURRENT_QUESTION_COUNT = "currentQuestionCount";
    public static final String KEY_TIME_MILI_LEFT = "timeMillileft";
    public static final String KEY_QUESTION_LIST = "questionList";
    public static final String KEY_ANSWERED = "answered";

    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private TextView textViewCountDown;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private Button buttonConfirmNext;

    int Score;
    int hightScore;
    int currentQuestionCount, totalQuestionCount;
    boolean answered, countBoolean = true;
    private Question currentQuestion;

    private ColorStateList textColorDefaultRb;

    private long onBackpressed;

    long timeMillileft;
    CountDownTimer countDownTimer;

    ColorStateList getTextColorDefaultCountDown;

    private ArrayList<Question> questionList;

    String difficultylevel;
    int Category_Id;
    String Category_Name;
    TextView textView_level,textView_Category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quize);

        //resource id
        textViewQuestion = findViewById(R.id.text_view_question);
        textViewScore = findViewById(R.id.text_view_score);
        textViewQuestionCount = findViewById(R.id.text_view_question_count);
        textViewCountDown = findViewById(R.id.text_view_countdown);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        textView_level=findViewById(R.id.text_view_level);
        textView_Category=findViewById(R.id.text_view_category);
        buttonConfirmNext = findViewById(R.id.button_confirm_next);
        //colorstate
        getTextColorDefaultCountDown = textViewCountDown.getTextColors();

        //calling getlevel
        getLevelAndCategory();
        //add new question by user
      //  AddQuestion();

        if (savedInstanceState == null) {
            QuizDbHelper dbHelper =QuizDbHelper.getInstance(this);
            questionList = dbHelper.getAllQuestion(Category_Id,difficultylevel);
            //total question
            totalQuestionCount = questionList.size();
            //random question
            Collections.shuffle(questionList);

            showNextQuestion();
        } else {
            questionList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            totalQuestionCount = questionList.size();
            currentQuestionCount = savedInstanceState.getInt(KEY_CURRENT_QUESTION_COUNT);
            currentQuestion = questionList.get(currentQuestionCount - 1);
            Score = savedInstanceState.getInt(KEY_SCORE);
            answered = savedInstanceState.getBoolean(KEY_ANSWERED);
            timeMillileft = savedInstanceState.getLong(KEY_TIME_MILI_LEFT);
            if (answered) {
                updateCountDownText();
                showSolution();
            } else {
                startCountDown();
            }

        }


        //calling button method
        ButtonOnClick();



    }

       private void AddQuestion() {
        Question question=new Question("PROGRAMMING Easy: A is correct",
                "A", "B", "C", 1, Question.DIFFICULTY_EASY,Category.PROGRAMMING);
        Question question2=new Question("PROGRAMMING Easy: b is correct",
                "A", "B", "C", 2, Question.DIFFICULTY_EASY,Category.PROGRAMMING);
        List<Question> questionList=new ArrayList<>();
        questionList.add(question);
        questionList.add(question2);




        QuizDbHelper.getInstance(this).addQuestionList(questionList);
    }

    private  void getLevelAndCategory(){
        Intent intent = getIntent();
        Category_Id=intent.getIntExtra(EXTRA_CATEGORY_ID,0);
        Category_Name=intent.getStringExtra(EXTRA_CATEGORY_NAME);
       difficultylevel = intent.getStringExtra(EXTRA_DIFFICULTY);

       textView_Category.setText("Category : "+Category_Name);
        textView_level.setText("Level : "+difficultylevel);
        Toast.makeText(this, "level" + difficultylevel, Toast.LENGTH_SHORT).show();
    }
    private void ButtonOnClick() {
        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answered) {

                    showNextQuestion();

                } else {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked()) {

                        checkResult();
                    } else {
                        Toast.makeText(QuizeActivity.this, "please select answered", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    private void showNextQuestion() {
        rb1.setEnabled(true);
        rb2.setEnabled(true);
        rb3.setEnabled(true);
        rb1.setTextColor(Color.BLUE);
        rb2.setTextColor(Color.BLUE);
        rb3.setTextColor(Color.BLUE);
        rbGroup.clearCheck();

        if (currentQuestionCount < totalQuestionCount) {
            currentQuestion = questionList.get(currentQuestionCount);
            textViewQuestion.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());

            currentQuestionCount++;
            textViewQuestionCount.setText("Question : " + currentQuestionCount + "/" + totalQuestionCount);
            answered = false;

            timeMillileft = TOTAL_TIME_PER_QUESTION;
            startCountDown();

            buttonConfirmNext.setText("lock");

        } else {
            finishQuize();
        }

    }

    public void startCountDown() {
        countDownTimer = new CountDownTimer(timeMillileft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeMillileft = millisUntilFinished;
                updateCountDownText();

            }

            @Override
            public void onFinish() {

                timeMillileft = 0;
                if (!rb1.isChecked() &&!rb2.isChecked() && !rb3.isChecked()) {
                        //ShowTimeOver();
                    Toast.makeText(QuizeActivity.this, "time over", Toast.LENGTH_SHORT).show();
                }
                updateCountDownText();
                checkResult();

            }
        }.start();
    }
    private void ShowTimeOver(){

    }

    private void updateCountDownText() {
        int minutes = (int) (timeMillileft / 1000) / 60;
        int seconds = (int) (timeMillileft / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        textViewCountDown.setText(timeFormatted);
        if (timeMillileft < 10000) {
            textViewCountDown.setTextColor(Color.RED);
        } else {
            textViewCountDown.setTextColor(Color.GREEN);
        }
    }


    private void checkResult() {
        answered = true;
        countDownTimer.cancel();

        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        int answeredNR = rbGroup.indexOfChild(rbSelected) + 1;
        if (answeredNR == currentQuestion.getAnswerNr()) {
            Score++;
            textViewScore.setText("Score : " + Score);
        }

        showSolution();
    }

    private void showSolution() {
        rb1.setEnabled(false);
        rb2.setEnabled(false);
        rb3.setEnabled(false);
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);
        switch (currentQuestion.getAnswerNr()) {
            case 1:
                rb1.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 1 is correct");
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 2 is correct");
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 3 is correct");
                break;
        }
        if (currentQuestionCount < totalQuestionCount) {
            buttonConfirmNext.setText("Next Question");
        } else {
            buttonConfirmNext.setText("Finish");
        }
    }


    private void finishQuize() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_SCORE, Score);
        setResult(RESULT_OK, resultIntent);
        finish();
    }


    @Override
    public void onBackPressed() {
        if (onBackpressed + 2000 > System.currentTimeMillis()) {

            finishQuize();
        } else {
            Toast.makeText(this, "press again for exit", Toast.LENGTH_SHORT).show();
        }
        onBackpressed = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    //SAVE STATE FOR ORIENTATION

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCORE, Score);
        outState.putInt(KEY_CURRENT_QUESTION_COUNT, currentQuestionCount);
        outState.putLong(KEY_TIME_MILI_LEFT, timeMillileft);
        outState.putBoolean(KEY_ANSWERED, answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST, questionList);
    }


}
