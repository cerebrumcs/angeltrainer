package com.cerebrumcs.angeltrainer.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.LinkedList;
import java.util.List;

import com.cerebrumcs.angeltrainer.model.Answer;

@Dao
public interface AnswerDao {

    @Query("SELECT * FROM Answer")
    List<Answer> getAnswers();

    @Query("SELECT * FROM Answer WHERE question_id = :questionid")
    List<Answer> getAnswersByQuestion(int questionid);

    @Insert
    long addAnswer(Answer answer);

    @Insert
    long [] addAllAnswers(List<Answer> answers);
}
