package com.cerebrumcs.angeltrainer.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.cerebrumcs.angeltrainer.model.Category;

import java.util.List;

@Dao
public interface CategoryDao {

    @Query("SELECT * FROM Category")
    public List<Category> getAllCategory();

    @Query("SELECT * FROM Category WHERE id = :id")
    public Category getCategoryById(long id);

    @Insert
    public Long addCategory(Category category);

    @Insert
    public List<Long> addAllCategory(List<Category> category);

    @Update
    public void updateCategory(Category category);

}
