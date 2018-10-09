package com.cerebrumcs.angeltrainer.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.cerebrumcs.angeltrainer.model.Answer;
import com.cerebrumcs.angeltrainer.model.Category;
import com.cerebrumcs.angeltrainer.model.History;
import com.cerebrumcs.angeltrainer.model.Question;
import com.cerebrumcs.angeltrainer.model.QuestionWithAnswer;

@Database(entities = {Answer.class, Category.class, History.class, Question.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    abstract public AnswerDao answerDao();

    abstract public QuestionDao questionDao();

    abstract public HistoryDao historyDao();

    abstract public CategoryDao categoryDao();

    abstract public QuestionWithAnswerDao questionWithAnswerDao();


}
