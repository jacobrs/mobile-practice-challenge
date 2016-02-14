package com.csgames.diabetus;

/**
 * Created by Thinesh on 2016-02-14.
 */
public class Answer {

    private String answer;
    private boolean isCorrect;

    public Answer(String answer, int isCorrect){
        this.answer = answer;
        if(isCorrect==1)
            this.isCorrect = true;
        else
            this.isCorrect = false;
    }

    public String getAnswer(){return answer;};
    public boolean getCorrect(){return isCorrect;};

}
