package com.cerebrumcs.angeltrainer.handler;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;

import com.cerebrumcs.angeltrainer.QuestionnaireActivtiy;
import com.cerebrumcs.angeltrainer.R;

public class QuestionnaireSelectionEventHandler implements View.OnClickListener {

    private final int category;

    public QuestionnaireSelectionEventHandler(int category){
        this.category = category;
    }
    
    @Override
    public void onClick(View v) {

        CheckBox randomCheckbox = ((Activity)v.getContext()).findViewById(R.id.ckbRandom);
        boolean isChecked = randomCheckbox.isChecked();

        Intent intent = new Intent(v.getContext(), QuestionnaireActivtiy.class);
        intent.putExtra("selection", this.category);
        intent.putExtra("random", isChecked);

        v.getContext().startActivity(intent);
    }
}
