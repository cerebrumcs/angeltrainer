package com.cerebrumcs.angeltrainer.model;

public class QuestionnaireMode {

    private static final int RANDOM_OFFSET = 10;

    public static int getMode(boolean random, int category){
        int mode = 0;
        if(random){
            mode = RANDOM_OFFSET;
        }

        mode += category;

        return mode;
    }

    public static boolean isRandom(int mode){
        return mode > RANDOM_OFFSET;
    }

}
