package com.markushager.exercisetracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import com.markushager.exercisetracker.Const;
import com.markushager.exercisetracker.ExerciseInfo;
import com.markushager.exercisetracker.ListAdapter;
import com.markushager.exercisetracker.ExerciseActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int OPTION_DEFAULT = 0;

    private ListView mListView;
    private ListAdapter mListAdapter;
    private int mPosition;
    private Intent mIntent;
    private ArrayList<ExerciseInfo> mExerciseInfoArrayList;
    private RadioGroup mRgOption;
    private RadioButton mRbMoveUp;
    private RadioButton mRbMoveDown;
    private RadioButton mRbSelectItem;
    private int mOption = OPTION_DEFAULT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        mListView = findViewById(R.id.listView);

        initOptionRadioButtons();
        fillAdapter();

        // Anzeigen der ausgewählten Zeile
        mListView.setOnItemClickListener(listClick);

    }

    private void initOptionRadioButtons() {

        mRgOption = findViewById(R.id.rgOption);
        mRbMoveUp = findViewById(R.id.rbMoveUp);
        mRbMoveDown = findViewById(R.id.rbMoveDown);
        mRbSelectItem = findViewById(R.id.rbSelectItem);
        // set checked button
        mRgOption.check(R.id.rbSelectItem);

        mRgOption.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

//                switch (checkedId) {
//                    case R.id.rbMoveUp:
//                        mOption = 1;
//                        break;
//                    case R.id.rbMoveDown:
//                        mOption = 2;
//                        break;
//                    case R.id.rbSelectItem:
//                        mOption = OPTION_DEFAULT;
//                        break;
//                    default:
//                        mOption = OPTION_DEFAULT;
//                        Toast.makeText(getApplicationContext(),
//                            "Unknown Radio Button selected",Toast.LENGTH_LONG).show();
//                }
                if (checkedId == R.id.rbMoveUp)
                {
                    mOption = 1;
                }
                else if (checkedId == R.id.rbMoveDown)
                {
                    mOption = 2;
                }
                else if (checkedId == R.id.rbSelectItem)
                {
                    mOption = OPTION_DEFAULT;
                }
                else
                {
                    mOption = OPTION_DEFAULT;
                    Toast.makeText(getApplicationContext(),
                            "Unknown Radio Button selected",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private AdapterView.OnItemClickListener listClick = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

            if (position == 0)
                return; // ignore, because its header line
            setPosition(position);

            switch (mOption) {
                case OPTION_DEFAULT:
                    selectItem(position);
                    break;
                case 1:
                    moveItemUp(position);
                    break;
                case 2:
                    moveItemDown(position);
                    break;
                default:
                    selectItem(position);
                    break;
            }

        }
    };

    private void selectItem(int position) {

        ExerciseInfo exerciseInfo = (ExerciseInfo) mListView.getItemAtPosition(position);

        mIntent = new Intent(MainActivity.this, ExerciseActivity.class);

        mIntent.putExtra(Const.KEY_EXERCISE, exerciseInfo.getExerciseName());
        mIntent.putExtra(Const.KEY_CURR_WORK_WEIGHT, Double.toString(exerciseInfo.getCurrWorkWeight()));
        mIntent.putExtra(Const.KEY_FAILED_CNT, Integer.toString(exerciseInfo.getFailedCnt()));
        mIntent.putExtra(Const.KEY_DELOADED_CNT, Integer.toString(exerciseInfo.getDeloadedCnt()));
        mIntent.putExtra(Const.KEY_MODE_LEVEL, Integer.toString(exerciseInfo.getModeLevel()));
        mIntent.putExtra(Const.KEY_MODE_OPTION, Integer.toString(exerciseInfo.getModeOption()));
        mIntent.putExtra(Const.KEY_WEIGHT_INCR_SIZE, Double.toString(exerciseInfo.getWeightIncrSize()));

        startActivityForResult(mIntent, Const.REQUEST_CODE_EDIT_ITEM);
    }

    private void moveItemUp(int position) {

        ExerciseInfo exerciseInfo;
        String msg;

        try {
            if (position > 0) {
                exerciseInfo = mExerciseInfoArrayList.get(position);
                mExerciseInfoArrayList.remove(position);
                mExerciseInfoArrayList.add(position-1, exerciseInfo);
                mListAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Moved Item Up",
                    Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e) {
            msg = "Error moving Item Up: " + e.getMessage();
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        }
    }

    private void moveItemDown(int position) {

        ExerciseInfo exerciseInfo;
        String msg;

        try {
            if (position < mExerciseInfoArrayList.size()-1) {
                exerciseInfo = mExerciseInfoArrayList.get(position);
                mExerciseInfoArrayList.remove(position);
                mExerciseInfoArrayList.add(position+1, exerciseInfo);
                mListAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Moved Item Down",
                    Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e) {
            msg = "Error moving Item Down: " + e.getMessage();
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {

        super.onActivityResult(requestCode, resultCode, intent);

        ExerciseInfo exerciseInfo = (ExerciseInfo) mListView.getItemAtPosition(mPosition);
        String exercise;
        int modeLevel, modeOption, failedCnt, deloadedCnt, deleteExercise = 0, addExercise = 0;
        double currWorkWeight, weightIncrSize;

        if (requestCode == Const.REQUEST_CODE_EDIT_ITEM) {
            if (resultCode == RESULT_OK) {

                exercise = intent.getStringExtra(Const.KEY_EXERCISE);
                currWorkWeight = Double.parseDouble(intent.getStringExtra(Const.KEY_CURR_WORK_WEIGHT));
                failedCnt = Integer.parseInt(intent.getStringExtra(Const.KEY_FAILED_CNT));
                deloadedCnt = Integer.parseInt(intent.getStringExtra(Const.KEY_DELOADED_CNT));
                modeOption = Integer.parseInt(intent.getStringExtra(Const.KEY_MODE_OPTION));
                weightIncrSize = Double.parseDouble(intent.getStringExtra(Const.KEY_WEIGHT_INCR_SIZE));
                modeLevel = Integer.parseInt(intent.getStringExtra(Const.KEY_MODE_LEVEL));
                deleteExercise = Integer.parseInt(intent.getStringExtra(Const.KEY_DELETE_EXERCISE));

                if (deleteExercise == 1) {
                    mExerciseInfoArrayList.remove(mPosition);
                }
                else {
                    exerciseInfo.setExerciseName(exercise);
                    exerciseInfo.setCurrWorkWeight(currWorkWeight);
                    exerciseInfo.setFailedCnt(failedCnt);
                    exerciseInfo.setDeloadedCnt(deloadedCnt);
                    exerciseInfo.setModeOption(modeOption);
                    exerciseInfo.setWeightIncrSize(weightIncrSize);
                    exerciseInfo.setModeLevel(modeLevel);
                }

                mListAdapter.notifyDataSetChanged();
            }
        }
        else if (requestCode == Const.REQUEST_CODE_ADD_ITEM) {
            if (resultCode == RESULT_OK) {

                exercise = intent.getStringExtra(Const.KEY_EXERCISE);
                addExercise = Integer.parseInt(intent.getStringExtra(Const.KEY_ADD_EXERCISE));

                if (addExercise == 1) {
                    mExerciseInfoArrayList.add(new ExerciseInfo(exercise, Const.DEFAULT_STARTING_WEIGHT));
                    mListAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //return true;

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        Intent intent;
        String password;
        String msg;

//        switch (id) {
//            case R.id.itemAddItem:
//                intent = new Intent(MainActivity.this, AddItemActivity.class);
//                startActivityForResult(intent, Const.REQUEST_CODE_ADD_ITEM);
//                return true;
//            case R.id.itemInfo:
//                //Toast.makeText(getApplicationContext(), "Item 3 Selected", Toast.LENGTH_LONG).show();
//                return true;
//            case R.id.versionInfo:
//                msg = "Version: " + Const.VERSION;
//                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
        if (id == R.id.itemAddItem)
        {
            intent = new Intent(MainActivity.this, AddItemActivity.class);
            startActivityForResult(intent, Const.REQUEST_CODE_ADD_ITEM);
            return true;
        }
        else if (id == R.id.itemInfo)
        {
            //Toast.makeText(getApplicationContext(), "Item 3 Selected", Toast.LENGTH_LONG).show();
            return true;
        }
        else if (id == R.id.versionInfo)
        {
            msg = "Version: " + Const.VERSION;
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            return true;
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        writeDataFile();
        finish();
    }

    private void writeDataFile() {
        List<String> list = new ArrayList<String>();
        IO io;
        boolean success = false;
        boolean append = false;
        String line;

        for (int i = 1; i < mListView.getAdapter().getCount(); i++) {
            ExerciseInfo exerciseInfo = (ExerciseInfo) mListView.getItemAtPosition(i);
            // Line:
            // ExerciseName;CurrWorkWeight;ModeLevel;Failed;Deloaded;ModeOption;WeightIncrSize
            line = exerciseInfo.getExerciseName() + Const.DEL +
                Double.toString(exerciseInfo.getCurrWorkWeight()) + Const.DEL +
                Integer.toString(exerciseInfo.getModeLevel()) + Const.DEL +
                Integer.toString(exerciseInfo.getFailedCnt()) + Const.DEL +
                Integer.toString(exerciseInfo.getDeloadedCnt()) + Const.DEL +
                Integer.toString(exerciseInfo.getModeOption()) + Const.DEL +
                Double.toString(exerciseInfo.getWeightIncrSize());
            list.add(line);
        }

        try {
            io = new IO(this, Const.FILENAME);
            if (success = io.writeFile(list, append) == true)
                Log.d(TAG,"writing to file "+ io.getFilename() + " succeeded");
            else
                Log.d(TAG,"writing to file "+ io.getFilename() + " failed");
        }
        catch (Exception e)
        {
            Log.e(TAG, e.getMessage());
        }
    }

    public void fillAdapter() {

        mExerciseInfoArrayList = new ArrayList<>();

        // try reading Data file
        if (!readDataFile(mExerciseInfoArrayList))
        {
            // no data file: therefore start with default data
            mExerciseInfoArrayList.add(new ExerciseInfo());  // Header
            mExerciseInfoArrayList.add(new ExerciseInfo("Squat", 80));
            mExerciseInfoArrayList.add(new ExerciseInfo("Bench Press", 90));
            mExerciseInfoArrayList.add(new ExerciseInfo("Deadlift", 140));
            mExerciseInfoArrayList.add(new ExerciseInfo("Press", 50));
            mExerciseInfoArrayList.add(new ExerciseInfo("Barbell Row", 90));
            mExerciseInfoArrayList.add(new ExerciseInfo("Incline Press", 80));
            mExerciseInfoArrayList.add(new ExerciseInfo("Power Clean", 60));
            mExerciseInfoArrayList.add(new ExerciseInfo("Leg Press", 140));
            mExerciseInfoArrayList.add(new ExerciseInfo("Pull Up", 5));

            mListAdapter = new ListAdapter(this, mExerciseInfoArrayList);
            mListView.setAdapter(mListAdapter);
        }
    }

    private boolean readDataFile(ArrayList<ExerciseInfo> exerciseInfoArrayList) {
        IO io;
        boolean success = false;
        List<String> list = null;
        int idx = 1;
        String parts[];

        try {
            io = new IO(this, Const.FILENAME);
            if (!io.fileExists())
                return false;
            if (success = io.readFile() == true)
                Log.d(TAG,"reading from file "+ io.getFilename() +" succeeded");
            exerciseInfoArrayList.add(new ExerciseInfo()); // Header Line
            list = io.getBuffer();
            for(String line : list) {
                Log.d(TAG, idx++ + ": " +line);
                //idx++;
                parts = line.split(Const.DEL);
                ExerciseInfo exerciseInfo = new ExerciseInfo(parts[0], Double.parseDouble(parts[1]));
                exerciseInfo.setModeLevel(Integer.parseInt(parts[2]));
                exerciseInfo.setFailedCnt(Integer.parseInt(parts[3]));
                exerciseInfo.setDeloadedCnt(Integer.parseInt(parts[4]));
                exerciseInfo.setModeOption(Integer.parseInt(parts[5]));
                exerciseInfo.setWeightIncrSize(Double.parseDouble(parts[6]));
                exerciseInfoArrayList.add(exerciseInfo);
            }
            mListAdapter = new ListAdapter(this, exerciseInfoArrayList);
            mListView.setAdapter(mListAdapter);
        }
        catch (Exception e)
        {
            Log.e(TAG,  e.getMessage());
            return false;
        }

        return true;
    }

    public void setPosition(int position) { mPosition = position; }
}
