package com.csgames.diabetus;

/**
 * Created by Thinesh on 2016-02-14.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Quiz Manager Singleton
 */

public class QuizManager {

    private static QuizManager quizManager;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private List<Question> questionList;

    public static final String QUESTIONS_TABLE = "Questions";
    public static final String QUESTION_COL = "question";
    public static final String QUESTIONID_COL = "question_id";
    public static final String ANSWERS_TABLE = "Answers";
    public static final String ANSWERS_COL = "answer";
    public static final String ANSWERS_CORRECT_COL = "isCorrect";

    // DB helper

    private QuizManager(Context context){
        questionList = new ArrayList<>();
        dbHelper = new DatabaseHelper(context, "questions", null, 1);

        //get db
        db = dbHelper.openDataBase();

        // populate questionList
        populateQuestions();


    }

    public static QuizManager getInstance(Context context){
        if(quizManager == null){
            quizManager = new QuizManager(context);
        }
        return quizManager;
    }

    private void populateQuestions(){

        // Query all questions
        String[] QuestionResultCol ={
                QUESTIONID_COL,
                QUESTION_COL
        };
        // Query all answers with specific q_id
        String[] AnswerResultCol ={
                ANSWERS_COL,
                ANSWERS_CORRECT_COL
        };

        final Cursor questioncursor = db.query(QUESTIONS_TABLE, QuestionResultCol, null, null, null, null, QUESTIONID_COL);

        int q_col_index =  questioncursor.getColumnIndex(QUESTION_COL);
        int qid_col_index =  questioncursor.getColumnIndex(QUESTIONID_COL);
        questioncursor.moveToFirst();
        while(!questioncursor.isAfterLast()){
            Question q = new Question(questioncursor.getInt(qid_col_index), questioncursor.getString(q_col_index));
            final String[] AnswerSelectCol ={
                    String.valueOf(questioncursor.getInt(qid_col_index))
            };

            Cursor answercursor = db.query(ANSWERS_TABLE, AnswerResultCol, QUESTIONID_COL+" = ?", AnswerSelectCol, null, null, null);
            int a_col_index =  answercursor.getColumnIndex(ANSWERS_COL);
            int acorrect_col_index =  answercursor.getColumnIndex(ANSWERS_CORRECT_COL);
            answercursor.moveToFirst();
            while(!answercursor.isAfterLast()){
                String a_a = answercursor.getString(a_col_index);
                int correct = answercursor.getInt(acorrect_col_index);
                Answer a = new Answer(a_a,correct);
                q.fillInAnswers(a);
                answercursor.moveToNext();
            }

            questionList.add(q);
            questioncursor.moveToNext();
        }

    }

    public Question getAQuestion(){
        return questionList.remove(0);
    }
}
