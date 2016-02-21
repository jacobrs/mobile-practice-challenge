package com.csgames.diabetus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thinesh on 2016-02-14.
 */
public class Question {

    private int q_id;
    public String question;
    public List<Answer> answers;
    DatabaseHelper dbhelper;

    public Question(int q_id, String question){
        answers = new ArrayList<Answer>();
        this.q_id = q_id;
        this.question = question;


    }

    private void fillInQuestion(){

    }
    public void fillInAnswers(Answer a){
        answers.add(a);

    }



}
