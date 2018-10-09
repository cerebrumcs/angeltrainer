package com.cerebrumcs.angeltrainer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.cerebrumcs.angeltrainer.database.DatabaseUtils;
import com.cerebrumcs.angeltrainer.handler.QuestionnaireLoader;
import com.cerebrumcs.angeltrainer.handler.QuestionnaireSelectionEventHandler;
import com.cerebrumcs.angeltrainer.model.Category;

public class TrainActivity extends AppCompatActivity {

    private Button setText(View btnView, long id){
        Button btn = (Button)btnView;
        Category cat = DatabaseUtils.getDb().categoryDao().getCategoryById(id);
        btn.setText(cat.getName());
        return btn;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);

        // Get buttons and add category name to the category buttons
        Button btnCategory1 = setText(findViewById(R.id.btnCategory1), QuestionnaireLoader.QuestionnaireCatories.CATEGORY_1);
        Button btnCategory2 = setText(findViewById(R.id.btnCategory2), QuestionnaireLoader.QuestionnaireCatories.CATEGORY_2);
        Button btnCategory3 = setText(findViewById(R.id.btnCategory3), QuestionnaireLoader.QuestionnaireCatories.CATEGORY_3);
        Button btnCategory4 = setText(findViewById(R.id.btnCategory4), QuestionnaireLoader.QuestionnaireCatories.CATEGORY_4);
        Button btnCategory5 = setText(findViewById(R.id.btnCategory5), QuestionnaireLoader.QuestionnaireCatories.CATEGORY_5);

        Button btnCategoryRandom = findViewById(R.id.btnCategoryRandom);
        Button btnCategoryHard = findViewById(R.id.btnCategoryHard);

        // Add actions to the buttons
        btnCategory1.setOnClickListener(new QuestionnaireSelectionEventHandler(QuestionnaireLoader.QuestionnaireCatories.CATEGORY_1));
        btnCategory2.setOnClickListener(new QuestionnaireSelectionEventHandler(QuestionnaireLoader.QuestionnaireCatories.CATEGORY_2));
        btnCategory3.setOnClickListener(new QuestionnaireSelectionEventHandler(QuestionnaireLoader.QuestionnaireCatories.CATEGORY_3));
        btnCategory4.setOnClickListener(new QuestionnaireSelectionEventHandler(QuestionnaireLoader.QuestionnaireCatories.CATEGORY_4));
        btnCategory5.setOnClickListener(new QuestionnaireSelectionEventHandler(QuestionnaireLoader.QuestionnaireCatories.CATEGORY_5));

        btnCategoryRandom.setOnClickListener(new QuestionnaireSelectionEventHandler(QuestionnaireLoader.QuestionnaireCatories.ALL));
        btnCategoryHard.setOnClickListener(new QuestionnaireSelectionEventHandler(QuestionnaireLoader.QuestionnaireCatories.HARDEST));

    }
}
