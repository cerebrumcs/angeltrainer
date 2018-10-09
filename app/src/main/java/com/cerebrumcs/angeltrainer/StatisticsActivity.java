package com.cerebrumcs.angeltrainer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cerebrumcs.angeltrainer.database.DatabaseUtils;
import com.cerebrumcs.angeltrainer.handler.QuestionnaireLoader;
import com.cerebrumcs.angeltrainer.handler.TimeHandler;
import com.cerebrumcs.angeltrainer.model.Statistic;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class StatisticsActivity extends AppCompatActivity {

    public DataPoint [] reverse(DataPoint [] arr){
        DataPoint [] reversedArr = new DataPoint[arr.length];
        for(int i = 0; i < arr.length; i++){
            reversedArr[i] = arr[arr.length - (i+1)];
        }
        return reversedArr;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_activity);

        Integer[] categories = new Integer[]{
                QuestionnaireLoader.QuestionnaireCatories.CATEGORY_1,
                QuestionnaireLoader.QuestionnaireCatories.CATEGORY_2,
                QuestionnaireLoader.QuestionnaireCatories.CATEGORY_3,
                QuestionnaireLoader.QuestionnaireCatories.CATEGORY_4,
                QuestionnaireLoader.QuestionnaireCatories.CATEGORY_5
        };


        GraphView[] graphs = new GraphView[]{
                (GraphView) findViewById(R.id.stats_category_1),
                (GraphView) findViewById(R.id.stats_category_2),
                (GraphView) findViewById(R.id.stats_category_3),
                (GraphView) findViewById(R.id.stats_category_4),
                (GraphView) findViewById(R.id.stats_category_5),
        };

        int statsDuration = 14;
        long currentTime = TimeHandler.getCurrentTime();
        long lastStart = currentTime;
        HashMap<Integer, List<DataPoint>> categoryWrongAnswered = new HashMap<>();
        HashMap<Integer, List<DataPoint>> categoryTrueAnswered = new HashMap<>();

        for (int i = statsDuration-1, j = 0; i > 0; i--, j++) {
            long start = TimeHandler.getTimeMsBeforeXDays(currentTime, i);
            long end = lastStart;
            lastStart = start;

            for (int cat : categories) {
                Statistic statsCat = DatabaseUtils.getDb().historyDao().getStatisticsByCategory(cat, start, end);
                if (!categoryTrueAnswered.containsKey(cat)) {
                    categoryTrueAnswered.put(cat, new LinkedList<DataPoint>());
                }
                if (!categoryWrongAnswered.containsKey(cat)) {
                    categoryWrongAnswered.put(cat, new LinkedList<DataPoint>());
                }
                System.out.println(statsCat.wrongAnswered);
                categoryTrueAnswered.get(cat).add(new DataPoint(j, statsCat.correctAnswered));
                categoryWrongAnswered.get(cat).add(new DataPoint(j, statsCat.wrongAnswered));
            }
        }


        for (int i = 0; i < graphs.length; i++) {
            GraphView gv = graphs[i];
            DataPoint[] s1Arr = categoryTrueAnswered.get(categories[i]).toArray(new DataPoint[categoryTrueAnswered.get(categories[i]).size()]);
            DataPoint[] s2Arr = categoryWrongAnswered.get(categories[i]).toArray(new DataPoint[categoryWrongAnswered.get(categories[i]).size()]);

            //s1Arr = reverse(s1Arr);
            //s2Arr = reverse(s2Arr);

            LineGraphSeries<DataPoint> s1 = new LineGraphSeries<>(s1Arr);
            LineGraphSeries<DataPoint> s2 = new LineGraphSeries<>(s2Arr);


            s1.setColor(Color.parseColor("#3d63a0"));
            s2.setColor(Color.parseColor("#af833b"));

            gv.setTitle(DatabaseUtils.getDb().categoryDao().getCategoryById(categories[i]).getName());
            gv.getGridLabelRenderer().setVerticalLabelsVisible(false);
            gv.getGridLabelRenderer().setHorizontalLabelsVisible(false);
            gv.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
            gv.getViewport().setDrawBorder(true);

            gv.addSeries(s1);
            gv.addSeries(s2);
        }
    }

}

