package com.techasylum.myquize;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StartActivity extends AppCompatActivity {
    public static final String EXTRA_DIFFICULTY = "extraDifficulty";
    public static final String EXTRA_CATEGORY_ID = "extra_Cat_Id";
    public static final String EXTRA_CATEGORY_NAME = "extra_Cat_name";

    private static final int REQUEST_CODE_QUIZ = 1;


    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_HIGHSCORE = "keyHighscore";
    int highscore;
    String difficultyLevel;
    Button start_quiz_btn;
    TextView textViewHighscore;
    Spinner spinnerDifficulty,spinnerCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //full screen code
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //assign resource id

        textViewHighscore = findViewById(R.id.text_view_highscore);
        spinnerDifficulty=findViewById(R.id.spinner_dificulty);
        spinnerCategory=findViewById(R.id.spinner_category);
        //delete try and catche
        /*try {
            QuizDbHelper.getInstance(this).DeleteCategory(1);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this,"dd"+e.getMessage(),Toast.LENGTH_LONG).show();
        }*/
        //spinner get category and level for quiz
        getCategoryFromDb();
        getLevelForQuestion();
        //click listner method
        loadHighscore();
        Button buttonStartQuiz = findViewById(R.id.button_start_quiz);
        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz();
            }
        });




    }
    private void AddQuestion() {
        Question question=new Question("SCIENCE Easy: A is correct",
                "A", "B", "C", 1, Question.DIFFICULTY_EASY,Category.SCIENCE);
        Question question2=new Question("SCIENCE Easy: b is correct",
                "A", "B", "C", 2, Question.DIFFICULTY_EASY,Category.SCIENCE);
        List<Question> questionList=new ArrayList<>();
        questionList.add(question);
        questionList.add(question2);
        QuizDbHelper.getInstance(this).addQuestionList(questionList);
    }

    private void addCategory(){
        Category category=new Category("science");
        QuizDbHelper.getInstance(this).addCategory(category);
    }




    private void getCategoryFromDb() {
        QuizDbHelper dbHelper = QuizDbHelper.getInstance(this);
        List<Category> categoriesList = dbHelper.getAllCategories();
        ArrayAdapter<Category> adapterCategories = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categoriesList);
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterCategories);
    }

    private void getLevelForQuestion() {
        String[] difficultylevel=Question.getAllDifficultyLevel();
        ArrayAdapter<String> adaptedifficulter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,difficultylevel);
        adaptedifficulter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(adaptedifficulter);
    }


    private void startQuiz() {
        Category categoryData=(Category) spinnerCategory.getSelectedItem();
        int id=categoryData.getId();
        String Neme=categoryData.getName();

        String difficulty = spinnerDifficulty.getSelectedItem().toString();
        Intent intent = new Intent(StartActivity.this, QuizeActivity.class);
        intent.putExtra(EXTRA_DIFFICULTY, difficulty);
        intent.putExtra(EXTRA_CATEGORY_ID, id);
        intent.putExtra(EXTRA_CATEGORY_NAME, Neme);
        startActivityForResult(intent, REQUEST_CODE_QUIZ);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_QUIZ) {
            if (resultCode == RESULT_OK) {
                int score = data.getIntExtra(QuizeActivity.KEY_SCORE, 0);
                if (score > highscore) {
                    updateHighscore(score);
                }
            }
        }
    }
    private void loadHighscore() {
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        highscore = prefs.getInt(KEY_HIGHSCORE, 0);
        textViewHighscore.setText("Highscore : " + highscore);
    }
    private void updateHighscore(int highscoreNew) {
        highscore = highscoreNew;
        textViewHighscore.setText("Highscore: " + highscore);
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_HIGHSCORE, highscore);
        editor.apply();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
