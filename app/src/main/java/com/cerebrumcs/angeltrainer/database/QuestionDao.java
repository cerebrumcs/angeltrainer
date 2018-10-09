package com.cerebrumcs.angeltrainer.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import com.cerebrumcs.angeltrainer.model.Answer;
import com.cerebrumcs.angeltrainer.model.Category;
import com.cerebrumcs.angeltrainer.model.Question;
import com.cerebrumcs.angeltrainer.model.QuestionWithAnswer;

@Dao
public interface QuestionDao {

    @Query("SELECT * FROM Question WHERE id = :questionId")
    public Question getQuestionById(int questionId);

    @Query("SELECT * FROM Question WHERE category_id = :categoryId")
    public List<Question> getQuestionByCategory(int categoryId);

    @Query("SELECT * FROM Question")
    public List<Question> getAllQuestion();


    @Insert
    public long addQuestion(Question question);

    @Insert
    public long [] addAllQuestion(List<Question> question);

    @Update
    public void updateQuestion(Question question);


}
