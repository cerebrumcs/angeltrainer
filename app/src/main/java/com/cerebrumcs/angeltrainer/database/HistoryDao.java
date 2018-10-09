package com.cerebrumcs.angeltrainer.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.database.Cursor;

import java.util.List;

import com.cerebrumcs.angeltrainer.model.History;
import com.cerebrumcs.angeltrainer.model.Question;
import com.cerebrumcs.angeltrainer.model.Statistic;

@Dao
public abstract class HistoryDao {

    @Query("SELECT * FROM History")
    abstract public List<History> getAllHistory();

    @Query("SELECT * FROM History h JOIN Question q ON h.question_id = q.id WHERE q.category_id = :categoryId ")
    abstract public List<History> getHistoryByCategoryId(int categoryId);

    @Query("SELECT * FROM History h JOIN Answer a ON h.answer_id = a.id WHERE a.is_true = :correctAnswered")
    abstract public List<History> getHistoryByAnswerType(boolean correctAnswered);

    @Query("SELECT * FROM History WHERE timestamp >= :start AND timestamp <= :end")
    abstract public List<History> getHistoryByByDate(long start, long end);

    @Query("SELECT h1.* FROM History h1 INNER JOIN (SELECT id, max(timestamp) FROM History WHERE mode = :mode) h2 ON h1.id = h2.id")
    abstract public History getLastPlayedByMode(int mode);


    public Statistic getStatisticsByCategory(long categoryId, long start, long end){
        String stmt = "SELECT a.* FROM Answer a JOIN " +
                "(SELECT h.* FROM History h JOIN (SELECT * FROM Question q WHERE category_id = :category_id) s ON s.id = h.question_id) ss " +
                "ON a.id = ss.answer_id " +
                "WHERE ss.timestamp > :start AND ss.timestamp < :end";

        Cursor cursor = DatabaseUtils.getDb().query(stmt, new Object[]{categoryId, start, end});

        int correctAnswered = 0;
        int falseAnswered = 0;
        while(cursor.moveToNext()){
            int idx = cursor.getColumnIndex("is_true");
            int isTrue = cursor.getInt(idx);
            if(isTrue == 1){
                correctAnswered++;
            } else{
                falseAnswered++;
            }
        }

        return new Statistic(correctAnswered, falseAnswered);

    }

    @Insert
    abstract public void addHistory(History history);

    @Insert
    abstract public void addAllHistory(List<History> history);


}
