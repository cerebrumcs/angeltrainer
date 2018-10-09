package com.cerebrumcs.angeltrainer.handler;



import com.cerebrumcs.angeltrainer.database.DatabaseUtils;
import com.cerebrumcs.angeltrainer.model.History;
import com.cerebrumcs.angeltrainer.model.Question;
import com.cerebrumcs.angeltrainer.model.QuestionWithAnswer;
import com.cerebrumcs.angeltrainer.model.Questionnaire;
import com.cerebrumcs.angeltrainer.model.QuestionnaireMode;

import java.util.Collections;
import java.util.List;

public class QuestionnaireLoader {

    public class QuestionnaireCatories {

        public static final int CATEGORY_1 = 1;
        public static final int CATEGORY_2 = 2;
        public static final int CATEGORY_3 = 3;
        public static final int CATEGORY_4 = 4;
        public static final int CATEGORY_5 = 5;

        public static final int ALL = 6;
        public static final int HARDEST = 7;

    }

    public static List<QuestionWithAnswer> loadQuestions(int category, int mode){
        List<QuestionWithAnswer> questions = null;
        switch (category) {
            case QuestionnaireCatories.CATEGORY_1:
                questions = DatabaseUtils.getDb().questionWithAnswerDao().getQuestionWithAnswersByCategory(1);
                break;
            case QuestionnaireCatories.CATEGORY_2:
                questions = DatabaseUtils.getDb().questionWithAnswerDao().getQuestionWithAnswersByCategory(2);
                break;
            case QuestionnaireCatories.CATEGORY_3:
                questions = DatabaseUtils.getDb().questionWithAnswerDao().getQuestionWithAnswersByCategory(3);
                break;
            case QuestionnaireCatories.CATEGORY_4:
                questions = DatabaseUtils.getDb().questionWithAnswerDao().getQuestionWithAnswersByCategory(4);
                break;
            case QuestionnaireCatories.CATEGORY_5:
                questions = DatabaseUtils.getDb().questionWithAnswerDao().getQuestionWithAnswersByCategory(5);
                break;
            case QuestionnaireCatories.ALL:
                questions = DatabaseUtils.getDb().questionWithAnswerDao().getAllQuestionWithAnswers();
                break;
            case QuestionnaireCatories.HARDEST:
                int noOfDays = 15;
                long end = TimeHandler.getCurrentTime();
                long start = TimeHandler.getTimeMsBeforeXDays(end, noOfDays);
                int noHardQuestions = 50;
                questions = DatabaseUtils.getDb().questionWithAnswerDao().getQuestionWithAnswerByScore(noHardQuestions, start, end);
                break;
        }
        return questions;
    }
    
    public static Questionnaire load(int category, int mode) {
        List<QuestionWithAnswer> questions = loadQuestions(category, mode);

        int startIndex = 0;
        if(!QuestionnaireMode.isRandom(mode)){
            History lastPlayed = DatabaseUtils.getDb().historyDao().getLastPlayedByMode(mode);
            if(lastPlayed != null) {
                long lastPlayedQuestionId = lastPlayed.getQuestionId();
                for (int i = 0; i < questions.size(); i++) {
                    QuestionWithAnswer q = questions.get(i);
                    if (q.getQuestion().getId() == lastPlayedQuestionId) {
                        startIndex = (i+1)%questions.size();
                        break;
                    }
                }
            }
        } else {
            Collections.shuffle(questions);
        }

        Questionnaire questionnaire = new Questionnaire(questions, startIndex);
        questionnaire.setMode(mode);

        return questionnaire;
    }


}
