package com.cerebrumcs.angeltrainer.database;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cerebrumcs.angeltrainer.model.Answer;
import com.cerebrumcs.angeltrainer.model.Question;
import com.cerebrumcs.angeltrainer.model.QuestionWithAnswer;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Dao
public abstract class QuestionWithAnswerDao {

    @Transaction
    @Query("SELECT * FROM Question WHERE id = :questionId")
    abstract public QuestionWithAnswer getQuestionWithAnswersById(int questionId);

    @Transaction
    @Query("SELECT * FROM Question WHERE id IN (:questionIds)")
    abstract public List<QuestionWithAnswer> getQuestionsWithAnswersById(List<Long> questionIds);

    @Transaction
    @Query("SELECT * FROM Question WHERE category_id = :categoryId")
    abstract public List<QuestionWithAnswer> getQuestionWithAnswersByCategory(int categoryId);

    @Transaction
    @Query("SELECT * FROM Question")
    abstract public List<QuestionWithAnswer> getAllQuestionWithAnswers();


    ///////////////////////////////////////////
    ///////////////////////////////////////////
    ///////////// HELPER CLASSES //////////////
    ///////////////////////////////////////////
    ///////////////////////////////////////////

    class Ratio{
        int falsePlayed;
        int truePlayed;
    }

    class Pair<T,V>{
        T t;
        V v;
        Pair(){}
        Pair(T t, V v){
            this.t = t;
            this.v = v;
        }
    }

    public List<QuestionWithAnswer> getQuestionWithAnswerByScore(int numberOfQuestions, long start, long end){

        HashMap<Long, Ratio> questionRatios = new HashMap<>();

        // get order of false answered questions
        String false_played = "SELECT q.id, fa.false_answered FROM Question q LEFT JOIN (SELECT h.question_id, COUNT(h.question_id) as false_answered FROM History h JOIN Answer a ON h.answer_id = a.id WHERE a.is_true = 0 AND timestamp > :start AND timestamp < :end GROUP BY h.question_id) fa ON fa.question_id = q.id";
        Cursor cursor = DatabaseUtils.getDb().query(false_played, new Object[]{start, end});
        while(cursor.moveToNext()){
            Ratio ratio = new Ratio();

            int quidx = cursor.getColumnIndex("id");
            int faidx = cursor.getColumnIndex("false_answered");

            long questionId = cursor.getLong(quidx);

            int noFalseAnswered = 0;
            if(!cursor.isNull(faidx)){
                noFalseAnswered = cursor.getInt(faidx);
            }

            ratio.falsePlayed = noFalseAnswered;
            questionRatios.put(questionId, ratio);
        }

        // get order of correct answered questions
        String true_played  = "SELECT q.id, ta.true_answered FROM Question q LEFT JOIN (SELECT h.question_id, COUNT(h.question_id) as true_answered FROM History h JOIN Answer a ON h.answer_id = a.id WHERE a.is_true = 1 AND timestamp > :start AND timestamp < :end GROUP BY h.question_id) ta ON ta.question_id = q.id";
        cursor = DatabaseUtils.getDb().query(true_played, new Object[]{start, end});
        while(cursor.moveToNext()){

            int quidx = cursor.getColumnIndex("id");
            int taidx = cursor.getColumnIndex("true_answered");

            long questionId = cursor.getLong(quidx);

            int noCorrectAnswered = 0;
            if(!cursor.isNull(taidx)){
                noCorrectAnswered = cursor.getInt(taidx);
            }

            Ratio ratio = questionRatios.get(questionId);
            ratio.truePlayed = noCorrectAnswered;
        }

        // Determine the count of the most often played question
        int maxTotal = 0;
        for(Long id : questionRatios.keySet()){
            Ratio ratio = questionRatios.get(id);
            int totalPlayed = ratio.falsePlayed + ratio.truePlayed;
            maxTotal = totalPlayed > maxTotal? totalPlayed : maxTotal;
        }


        List<Pair<Long, Double>> questionScores = new LinkedList<>();

        // Rank by logarithmic weighted score
        for(Long id : questionRatios.keySet()){
            Ratio ratio = questionRatios.get(id);

            int totalPlayed = ratio.truePlayed + ratio.falsePlayed;

            // if total played == 0 or true played == 0 => assign a max score to that question
            double score = Double.MAX_VALUE;
            if(ratio.truePlayed != 0){
                // weight score by uncertainty
                double trueToTotalRatio = totalPlayed / (double)ratio.truePlayed;
                double uncertainty = maxTotal / totalPlayed;
                score = uncertainty * trueToTotalRatio;
            }
            questionScores.add(new Pair(id, score));
        }

        Collections.sort(questionScores, new Comparator<Pair<Long, Double>>() {
            @Override
            public int compare(Pair<Long, Double> p1, Pair<Long, Double> p2) {
                return -p1.v.compareTo(p2.v);
            }
        });

        // Select highest rated questions and return
        List<Long> questionsToRequest = new LinkedList<>();
        for(int i = 0; i < numberOfQuestions; i++){
            questionsToRequest.add(questionScores.get(i).t);
        }

        List<QuestionWithAnswer> questionsWithAnswers = this.getQuestionsWithAnswersById(questionsToRequest);

        return questionsWithAnswers;
    }


    @Transaction
    public void addQuestionWithAnswer(QuestionWithAnswer questionWithAnswer) {
        Question q = questionWithAnswer.getQuestion();
        long questionId = DatabaseUtils.getDb().questionDao().addQuestion(q);
        for (Answer a : questionWithAnswer.getAnswers()) {
            a.setQuestionId(questionId);
        }

        DatabaseUtils.getDb().answerDao().addAllAnswers((List<Answer>)questionWithAnswer.getAnswers());
    }

    @Transaction
    public void addAllQuestionWithAnswer(List<QuestionWithAnswer> questionsWithAnswer) {

        List<Question> questions = new LinkedList<>();
        for(QuestionWithAnswer qa : questionsWithAnswer){
            questions.add(qa.getQuestion());
        }

        long [] questionIds = DatabaseUtils.getDb().questionDao().addAllQuestion(questions);

        List<Answer> answers = new LinkedList<>();
        for (int i = 0; i < questionIds.length; i++){
            long questionId = questionIds[i];
            for (Answer a : questionsWithAnswer.get(i).getAnswers()) {
                a.setQuestionId(questionId);
                answers.add(a);
            }
        }
        DatabaseUtils.getDb().answerDao().addAllAnswers(answers);

    }


}
