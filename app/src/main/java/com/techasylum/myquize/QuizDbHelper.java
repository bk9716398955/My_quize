package com.techasylum.myquize;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;
import com.techasylum.myquize.QuizContract.*;

import java.util.ArrayList;
import java.util.List;

public class QuizDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="MY_DATA.db";
    public static final int DATABASE_VERSION=2;
    private  Context context;
    List<Category> categoryList;
   private SQLiteDatabase db;
    private static  QuizDbHelper instance;
        //singleton class
    public static synchronized QuizDbHelper getInstance(Context context) {
        if(instance==null){
            instance=new QuizDbHelper(context.getApplicationContext());
        }

        return instance;
    }

    private QuizDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db=db;

        final String SQL_CREATE_CATEGORIES_TABLE= "CREATE TABLE " +
                CategoriesTable.TABLE_NAME + " ( " +
                CategoriesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CategoriesTable.COLUMN_NAME + " TEXT " +
                ")";

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionsTable.COLUMN_ANSWER_NR + " INTEGER, " +
                QuestionsTable.COLUMN_DIFFICULTY + " TEXT, " +
                QuestionsTable.COLUMN_CATEGORY_ID + " INTEGER, "+
                " FOREIGN KEY (" + QuestionsTable.COLUMN_CATEGORY_ID + ") REFERENCES " +
                CategoriesTable.TABLE_NAME + "(" + CategoriesTable._ID + ")" + "ON DELETE CASCADE" +
                ")";

        db.execSQL(SQL_CREATE_CATEGORIES_TABLE);
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillCategoryTable();
        fillQuestionsTable();

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CategoriesTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);

    }
    public void fillCategoryTable(){
        Category category1=new Category("programming");
        insertCategory(category1);
        Category category2=new Category("geography");
        insertCategory(category2);
        Category category3=new Category("math");
        insertCategory(category3);


    }
    public void addCategory(Category category){
        db=getWritableDatabase();
        insertCategory(category);
    }

    public void addCategories(List<Category> categoryList){
        db=getWritableDatabase();


        for (Category category:categoryList){
            insertCategory(category);
        }
    }
    public void DeleteCategory(int CategoryID)
    {
        db=getWritableDatabase();



            String[] arg = new String[]{String.valueOf(CategoryID)};
            db.delete(CategoriesTable.TABLE_NAME, CategoriesTable._ID +"=?", arg);

            Toast.makeText(context, "delete category", Toast.LENGTH_SHORT).show();

    }

    public void insertCategory(Category category){
        ContentValues cv=new ContentValues();
        cv.put(CategoriesTable.COLUMN_NAME,category.getName());
        db.insert(CategoriesTable.TABLE_NAME,null,cv);
    }


    private void fillQuestionsTable() {
        Question q1 = new Question("PROGRAMMING Easy: A is correct",
                "A", "B", "C", 1, Question.DIFFICULTY_EASY,Category.PROGRAMMING);
        insertQuestion(q1);
        Question q2 = new Question("GEOGRAPHY Medium: B is correct",
                "A", "B", "C", 2, Question.DIFFICULTY_MEDIUM,Category.GEOGRAPHY);
        insertQuestion(q2);
        Question q3 = new Question("GEOGRAPHY Medium: C is correct",
                "A", "B", "C", 3, Question.DIFFICULTY_MEDIUM,Category.GEOGRAPHY);
        insertQuestion(q3);
        Question q4 = new Question(" MATH Hard: A is correct",
                "A", "B", "C", 1, Question.DIFFICULTY_HARD,Category.MATH);
        insertQuestion(q4);
        Question q5 = new Question("MATH Hard: B is correct",
                "A", "B", "C", 2, Question.DIFFICULTY_HARD,Category.MATH);
        insertQuestion(q5);
        Question q6 = new Question("not listed  Hard: C is correct",
                "A", "B", "C", 3, Question.DIFFICULTY_MEDIUM,Category.MATH);
        insertQuestion(q6);
        Question q7 = new Question("not listed  Hard: C is correct",
                "A", "B", "C", 3, Question.DIFFICULTY_EASY,Category.MATH);
        insertQuestion(q7);
        Question q8 = new Question("PROGRAMMING Easy: A is correct",
                "A", "B", "C", 1, Question.DIFFICULTY_MEDIUM,Category.PROGRAMMING);
        insertQuestion(q8);
        Question q9 = new Question("PROGRAMMING Easy: A is correct",
                "A", "B", "C", 1, Question.DIFFICULTY_MEDIUM,Category.PROGRAMMING);
        insertQuestion(q9);
        Question q10 = new Question("PROGRAMMING Easy: A is correct",
                "A", "B", "C", 1, Question.DIFFICULTY_HARD,Category.PROGRAMMING);
        insertQuestion(q10);
        Question q31 = new Question("GEOGRAPHY Medium: C is correct",
                "A", "B", "C", 3, Question.DIFFICULTY_EASY,Category.GEOGRAPHY);
        insertQuestion(q31);
        Question q32 = new Question("GEOGRAPHY Medium: C is correct",
                "A", "B", "C", 3, Question.DIFFICULTY_EASY,Category.GEOGRAPHY);
        insertQuestion(q32);
        Question q33 = new Question("GEOGRAPHY Medium: C is correct",
                "A", "B", "C", 3, Question.DIFFICULTY_HARD,Category.GEOGRAPHY);
        insertQuestion(q33);

    }

    public void addQuestion(Question question){
        db=getWritableDatabase();
        insertQuestion(question);

    }

    public void addQuestionList(List<Question> questionList){

        db=getWritableDatabase();
        for(Question question:questionList){
            insertQuestion(question);
        }
    }

    public void DeleteQuestion(int question_Id)
    {
        db=getWritableDatabase();

        String id = "_id=?";
        String[] arg=new String[]{String.valueOf(question_Id)};
        db.delete(QuestionsTable.TABLE_NAME,id,arg);

        Toast.makeText(context, "delete Question", Toast.LENGTH_SHORT).show();

    }
    private void insertQuestion(Question question) {
        ContentValues cv = new ContentValues();



        cv.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionsTable.COLUMN_ANSWER_NR, question.getAnswerNr());
        cv.put(QuestionsTable.COLUMN_DIFFICULTY,question.getDifficulty());
        cv.put(QuestionsTable.COLUMN_CATEGORY_ID,question.getCategoryID());

        db.insert(QuestionsTable.TABLE_NAME, null, cv);


        Toast.makeText(context ,"inserter", Toast.LENGTH_SHORT).show();
    }

    public List<Category> getAllCategories() {
        categoryList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + CategoriesTable.TABLE_NAME, null);
        if (c.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(c.getInt(c.getColumnIndex(CategoriesTable._ID)));
                category.setName(c.getString(c.getColumnIndex(CategoriesTable.COLUMN_NAME)));
                categoryList.add(category);
            } while (c.moveToNext());
        }
        c.close();
        return categoryList;
    }

    public  ArrayList<Question> getAllQuestion(){
        ArrayList<Question> questionList=new ArrayList<>();
        db = getReadableDatabase();

        Cursor c=db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME,null);
        if(c.moveToFirst()){
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(QuestionsTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                question.setCategoryID(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_CATEGORY_ID)));

                questionList.add(question);

            }while(c.moveToNext());
        }


        c.close();
        return questionList;
    }
    public  ArrayList<Question> getAllQuestion(int categoryID, String difficulty){
        ArrayList<Question> questionList=new ArrayList<>();
        db = getReadableDatabase();
        String selection = QuestionsTable.COLUMN_CATEGORY_ID + " = ? " +
                " AND " + QuestionsTable.COLUMN_DIFFICULTY + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(categoryID), difficulty};
        Cursor c = db.query(
                QuestionsTable.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if(c.moveToFirst()){
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(QuestionsTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                question.setCategoryID(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_CATEGORY_ID)));

                questionList.add(question);

            }while(c.moveToNext());
        }


        c.close();
        return questionList;
    }
}
