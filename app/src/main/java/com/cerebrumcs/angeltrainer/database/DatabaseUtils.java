package com.cerebrumcs.angeltrainer.database;

import android.arch.persistence.room.Room;
import android.content.ContentProvider;
import android.content.res.AssetManager;
import android.util.Log;
import android.view.animation.AnimationSet;

import com.cerebrumcs.angeltrainer.Angeltrainer;
import com.cerebrumcs.angeltrainer.WelcomeActivity;
import com.cerebrumcs.angeltrainer.model.Answer;
import com.cerebrumcs.angeltrainer.model.Category;
import com.cerebrumcs.angeltrainer.model.Question;
import com.cerebrumcs.angeltrainer.model.QuestionWithAnswer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class DatabaseUtils {

    private static AppDatabase db = null;

    private static String DB_NAME = "angeltrainer";

    public static AppDatabase getDb() {
        if (db == null) {
            db = Room.databaseBuilder(Angeltrainer.getInstance().getApplicationContext(), AppDatabase.class, DB_NAME).allowMainThreadQueries().build();
        }
        return db;
    }

    private static Question makeQuestionEntry(String line) {
        Question question = new Question();
        try {
            String[] split = line.split(";");
            int id = Integer.parseInt(split[0]);
            String text = split[1];
            int categoryId = Integer.parseInt(split[2]);

            question.setId(id);
            question.setQuestion(text);
            question.setCategoryId(categoryId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return question;
    }

    private static Answer makeAnswerEntry(String line) {

        Answer answer = new Answer();

        try {
            String[] split = line.split(";");
            int id = Integer.parseInt(split[0]);
            String text = split[1];
            boolean isTrue = Boolean.parseBoolean(split[2]);
            int questionId = Integer.parseInt(split[3]);

            answer.setId(id);
            answer.setAnswer(text);
            answer.setTrue(isTrue);
            answer.setQuestionId(questionId);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return answer;
    }

    private static Category makeCategoryEntry(String line) {

        Category category = new Category();

        try {
            String[] split = line.split(";");
            int id = Integer.parseInt(split[0]);
            String name = split[1];

            category.setId(id);
            category.setName(name);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return category;
    }

    public static void fillDatabase(AssetManager assets) {

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(assets.open("fragen"), "utf-8"));

            String text = "";
            String line = null;

            // Load the questions.
            // Thus, the statement:
            // - adds a line break after lines containing: 'Lösung' in order to seperate all question / answer / solution block.
            // - discards empty lines
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                line = line.replaceAll("\n", "");
                line = line.trim();

                if(firstLine){
                    firstLine = false;
                    continue;
                } else if (line.startsWith("Lösung") || line.startsWith("CATEGORY:")) {
                    text += line + "\n\n";
                } else if (line.isEmpty()) {
                    continue;
                } else {
                    text += line + "\n";
                }
            }

            // Split on empty lines separates the per question blocks
            String [] blocks = text.split("\n\n");

            // Create Object Structure
            Category currentCategory = null;
            List<Category> categories = new LinkedList<>();
            List<QuestionWithAnswer> questionsWithAnswer = new LinkedList<>();
            for(String block : blocks){
                if(block.startsWith("CATEGORY:")){
                    String categoryName = block.replace("CATEGORY:", "").trim();
                    currentCategory = new Category(categoryName);
                    long categoryId = DatabaseUtils.getDb().categoryDao().addCategory(currentCategory);

                    currentCategory.setId(categoryId);
                    categories.add(currentCategory);

                } else if(!block.trim().isEmpty()){

                    int indexOfQuestion = 0;
                    int indexOfA = block.indexOf("a)");
                    int indexOfB = block.indexOf("b)");
                    int indexOfC = block.indexOf("c)");
                    int indexOfSolution = block.indexOf("Lösung:");

                    if(indexOfA < 0 || indexOfB < 0 || indexOfC < 0){
                        System.out.println("############# Cannot find all answers ########## ");
                    }

                    String qText = block.substring(indexOfQuestion, indexOfA);
                    String aText = block.substring(indexOfA, indexOfB).replace("a)", "").trim();
                    String bText = block.substring(indexOfB, indexOfC).replace("b)", "").trim();
                    String cText = block.substring(indexOfC, indexOfSolution).replace("c)", "").trim();
                    String sText = block.substring(indexOfSolution).replace("Lösung:", "").trim().toLowerCase();

                    int categoryId = (int) currentCategory.getId();
                    Question question = new Question(qText, categoryId);
                    Answer answerA = new Answer(aText, sText.equals("a"));
                    Answer answerB = new Answer(bText, sText.equals("b"));
                    Answer answerC = new Answer(cText, sText.equals("c"));

                    QuestionWithAnswer questionWithAnswer = new QuestionWithAnswer();
                    questionWithAnswer.setQuestion(question);
                    questionWithAnswer.addAnswer(answerA);
                    questionWithAnswer.addAnswer(answerB);
                    questionWithAnswer.addAnswer(answerC);

                    questionsWithAnswer.add(questionWithAnswer);
                }
            }

            DatabaseUtils.getDb().questionWithAnswerDao().addAllQuestionWithAnswer(questionsWithAnswer);

        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public static void deleteDb() {
        try {
            Angeltrainer.getInstance().getApplicationContext().deleteDatabase(DB_NAME);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
