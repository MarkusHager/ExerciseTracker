package com.markushager.exercisetracker;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import com.markushager.exercisetracker.ExerciseInfo;

public class ListAdapter extends ArrayAdapter<ExerciseInfo> {

    private static final String TAG = ListAdapter.class.getSimpleName();
    private static final String EXERCISE_TITLE = "Exercise";
    private static final String MODE_TITLE = "Mode";
    private static final String WEIGHT_TITLE = "Weight";
    private static final String CURR_REPS_TITLE = "Reps";
    private static final String FAILED_TITLE = "Failed";
    private static final String DELOADED_TITLE = "Deloaded";
    private Context mContext;
    private List<ExerciseInfo> mExerciseInfoList = new ArrayList<>();

    public ListAdapter(@NonNull Context context, @LayoutRes ArrayList<ExerciseInfo> list) {
        super(context, 0 , list);
        mContext = context;
        mExerciseInfoList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItem = convertView;

        try {
            if (listItem == null) {
                listItem = LayoutInflater.from(mContext).inflate(R.layout.list, parent, false);
            }

            if (position == 0) // Title
            {
                // header
                TextView exerciseName = listItem.findViewById(R.id.exerciseName);
                exerciseName.setText(EXERCISE_TITLE);
                exerciseName.setTypeface(null, Typeface.BOLD);
                exerciseName.setBackgroundColor(Color.YELLOW);

                TextView setsAndReps = listItem.findViewById(R.id.setsAndReps);
                setsAndReps.setText(MODE_TITLE);
                setsAndReps.setTypeface(null, Typeface.BOLD);
                setsAndReps.setBackgroundColor(Color.YELLOW);

                TextView currWeight = listItem.findViewById(R.id.currWeight);
                currWeight.setText(WEIGHT_TITLE);
                currWeight.setTypeface(null, Typeface.BOLD);
                currWeight.setBackgroundColor(Color.YELLOW);
            }
            else {
                ExerciseInfo exerciseInfo = mExerciseInfoList.get(position);

                TextView exerciseName = listItem.findViewById(R.id.exerciseName);
                exerciseName.setText(exerciseInfo.getExerciseName());
                exerciseName.setTypeface(null, Typeface.NORMAL);
                exerciseName.setBackgroundColor(Color.CYAN);

                TextView setsAndReps = listItem.findViewById(R.id.setsAndReps);
                setsAndReps.setText(Integer.toString(exerciseInfo.getSets()) + " x " +
                    Integer.toString(exerciseInfo.getReps()));
                setsAndReps.setTypeface(null, Typeface.NORMAL);
                setsAndReps.setBackgroundColor(Color.GRAY);

                TextView currWeight = listItem.findViewById(R.id.currWeight);
                currWeight.setText(Double.toString(exerciseInfo.getCurrWorkWeight()));
                currWeight.setTypeface(null, Typeface.NORMAL);
                currWeight.setBackgroundColor(Color.MAGENTA);

                /*
                TextView failed = listItem.findViewById(R.id.failed);
                failed.setText(Integer.toString(exerciseInfo.getFailedCnt()));
                failed.setBackgroundColor(Color.RED);

                TextView deloaded = listItem.findViewById(R.id.deloaded);
                deloaded.setText(Integer.toString(exerciseInfo.getDeloadedCnt()));
                deloaded.setBackgroundColor(Color.BLUE);*/
            }
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
        }

        return listItem;
    }
}

