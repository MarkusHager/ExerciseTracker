package com.markushager.exercisetracker;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.activity.OnBackPressedDispatcher;


public class ExerciseActivity extends AppCompatActivity
{

    private static final String TAG = ExerciseActivity.class.getSimpleName();
    private static final String SET_TITLE = "Sets";
    private static final int WARMUP_SETS = 4;
    private static final int MAX_WORK_SETS = 5;
    private static final String FAILED_TITLE = "Failed = ";
    private static final String DELOADED_TITLE = "Deloaded = ";
    private static final String MODE_LEVEL_TITLE = "Mode Level";
    private static final String WEIGHT_INCR_TITLE = "Weight Incr";
    private static final String MODE_OPTION_TITLE = "Mode Option";
    private static final String TITLE_COLOR = "Yellow";
    private static final double WEIGHT_CHANGE_SMALL = 1.25f;
    private static final double WEIGHT_CHANGE_BIG = 5.00f;

    int[] mWaSetArray = new int[]{R.id.waSet1, R.id.waSet2, R.id.waSet3, R.id.waSet4};
    int[] modeLevelArray = new int[]{R.id.mode5x5, R.id.mode3x5, R.id.mode3x3, R.id.mode1x3,
        R.id.mode5x3, R.id.mode1x5};
    int[] mModeOptionArray = new int[]{R.id.modeFix, R.id.modeVar};

    private TextView[] mTvWarmupSet = new TextView[WARMUP_SETS];
    private TextView mTvWorkSet;

    private TextView mTvWarmUpTitle;

    private TextView mTvFailedTitle;
    private TextView mTvDeloadedTitle;
    private TextView mTvFailed;
    private TextView mTvDeloaded;

    private TextView mTvOutput;

    private TextView mTvModeLevelTitle;
    private TextView mTvModeOptionTitle;
    private TextView mTvWeightIncrTitle;

    private TextView mTvEditWeight;
    private TextView mTvExercise;

    private Button mBtnDone;
    private Button mBtnFailed;
    private Button mBtnWeightIncrSmall;
    private Button mBtnWeightIncrBig;
    private Button mBtnWeightDecrSmall;
    private Button mBtnWeightDecrBig;
    private Button mBtnDelete;
    private Button mBtnBack;

    private RadioGroup mRgModeLevel;
    private RadioButton mRbMode5x5;
    private RadioButton mRbMode3x5;
    private RadioButton mRbMode3x3;
    private RadioButton mRbMode1x5;
    private RadioButton mRbMode1x3;
    private RadioButton mRbMode5x3;

    private RadioGroup mRgModeOption;
    private RadioButton mRbModeFix;
    private RadioButton mRbModeVar;

    private RadioGroup mRgWeightIncrSize;
    private RadioButton mRbWeightIncrSmall;
    private RadioButton mRbWeightIncrBig;

    private String mExerciseName;

    private int[] mWarmupReps = new int[WARMUP_SETS];
    private double[] mWarmupWeight = new double[WARMUP_SETS];
    private int mWorkSets;
    private int mWorkReps;
    private double mCurrWorkWeight;
    private int mFailedCnt;
    private int mDeloadedCnt;
    private int mModeLevel;
    private String mOutput;
    private int mState = 0; // -1=failed, 0=nothing, 1=succeeded
    private int mModeOption = 0;
    private double mWeightIncrSize;
    private int mDeleteExercise = 0;

    private Intent mIntent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        try
        {
            setContentView(R.layout.item);

            // Back Button in Action (Menu) Bar (calls onCreate of MainActivity.java)
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            getDataFromIntent();

            initExercise();
            initSets();
            initState();
            initButtons();
            initModeLevelRadioButtons();
            initModeOptionRadioButtons();
            initWeightIncrRadioButtons();

            // Output Line
            mTvOutput = findViewById(R.id.output);
        }
        catch (Exception e)
        {
            Log.e(TAG, "Error in function onCreate: " + e);
        }

    }


    public boolean onOptionsItemSelected(MenuItem item)
    {

        /*
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }*/

        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);

        return true;
    }

    private void getDataFromIntent()
    {

        mIntent = getIntent();

        mExerciseName = mIntent.getStringExtra(Const.KEY_EXERCISE);
        mCurrWorkWeight = Double.parseDouble(mIntent.getStringExtra(Const.KEY_CURR_WORK_WEIGHT));
        mFailedCnt = Integer.parseInt(mIntent.getStringExtra(Const.KEY_FAILED_CNT));
        mDeloadedCnt = Integer.parseInt(mIntent.getStringExtra(Const.KEY_DELOADED_CNT));
        mModeLevel = Integer.parseInt(mIntent.getStringExtra(Const.KEY_MODE_LEVEL));
        mModeOption = Integer.parseInt(mIntent.getStringExtra(Const.KEY_MODE_OPTION));
        mWeightIncrSize = Double.parseDouble(mIntent.getStringExtra(Const.KEY_WEIGHT_INCR_SIZE));

        mWorkSets = Const.WORK_SETS[mModeLevel];
        mWorkReps = Const.WORK_REPS[mModeLevel];
    }

    private void initExercise()
    {

        // Connect Variables with Layout File
        // Title
        mTvExercise = findViewById(R.id.tvExercise);
        mTvExercise.setText(mExerciseName);
        mTvExercise.setBackgroundResource(R.color.Aqua);
        mTvExercise.setTypeface(null, Typeface.BOLD);
    }

    private void initSets()
    {


        // Warmup Sets
        mTvWarmUpTitle = findViewById(R.id.setTitle);
        mTvWarmUpTitle.setText(SET_TITLE);
        mTvWarmUpTitle.setTypeface(null, Typeface.BOLD);

        actualizeWarmUpSets();

        mTvWorkSet = findViewById(R.id.workSet);
        actualizeWorkSets();
    }

    private void actualizeWarmUpSets()
    {

        for (int set = 1; set <= Const.WARMUP_PERCENTAGES.length; set++)
        {
            mWarmupReps[set - 1] = Const.WARMUP_REPS[set - 1];
            mWarmupWeight[set - 1] = ExerciseInfo.getWarmupWeight(mCurrWorkWeight, set);
            mTvWarmupSet[set - 1] = findViewById(mWaSetArray[set - 1]);
            mTvWarmupSet[set - 1].setText(mWarmupReps[set - 1] + " x " + mWarmupWeight[set - 1] + "kg");
        }
    }

    private void actualizeWorkSets()
    {

        mTvWorkSet.setText(mWorkSets + " x " + mWorkReps + " x " + mCurrWorkWeight + "kg");
    }

    private void initState()
    {

        // state info
        mTvFailedTitle = findViewById(R.id.failedTitle);
        mTvFailedTitle.setText(FAILED_TITLE);
        mTvFailed = findViewById(R.id.failedCnt);
        mTvFailed.setText(Integer.toString(mFailedCnt));
        mTvDeloadedTitle = findViewById(R.id.deloadedTitle);
        mTvDeloadedTitle.setText(DELOADED_TITLE);
        mTvDeloaded = findViewById(R.id.deloadedCnt);
        mTvDeloaded.setText(Integer.toString(mDeloadedCnt));
    }

    private void initButtons()
    {

        // Buttons
        mBtnDone = findViewById(R.id.buttonDone);
        mBtnDone.setBackgroundResource(R.color.LightGreen);
        mBtnDone.setOnClickListener(new DoneButtonClick());

        mBtnFailed = findViewById(R.id.buttonFailed);
        mBtnFailed.setBackgroundResource(R.color.HotPink);
        mBtnFailed.setOnClickListener(new FailedButtonClick());

        mBtnWeightDecrBig = findViewById(R.id.buttonWeightDecrBig);
        mBtnWeightDecrBig.setBackgroundResource(R.color.HotPink);
        mBtnWeightDecrBig.setTypeface(null, Typeface.BOLD);
        mBtnWeightDecrBig.setOnClickListener(new DecrWeightBigButtonClick());

        mBtnWeightDecrSmall = findViewById(R.id.buttonWeightDecrSmall);
        mBtnWeightDecrSmall.setBackgroundResource(R.color.HotPink);
        mBtnWeightDecrSmall.setTypeface(null, Typeface.BOLD);
        mBtnWeightDecrSmall.setOnClickListener(new DecrWeightSmallButtonClick());

        mBtnWeightIncrSmall = findViewById(R.id.buttonWeightIncrSmall);
        mBtnWeightIncrSmall.setBackgroundResource(R.color.LightGreen);
        mBtnWeightIncrSmall.setTypeface(null, Typeface.BOLD);
        mBtnWeightIncrSmall.setOnClickListener(new IncrWeightSmallButtonClick());

        mBtnWeightIncrBig = findViewById(R.id.buttonWeightIncrBig);
        mBtnWeightIncrBig.setBackgroundResource(R.color.LightGreen);
        mBtnWeightIncrBig.setTypeface(null, Typeface.BOLD);
        mBtnWeightIncrBig.setOnClickListener(new IncrWeightBigButtonClick());

        mBtnDelete = findViewById(R.id.buttonDelete);
        mBtnDelete.setBackgroundResource(R.color.HotPink);
        mBtnDelete.setTypeface(null, Typeface.BOLD);
        mBtnDelete.setOnClickListener(new DeleteButtonClick());

        mBtnBack = findViewById(R.id.buttonBack);
        mBtnBack.setBackgroundResource(R.color.LightGreen);
        mBtnBack.setTypeface(null, Typeface.BOLD);
        mBtnBack.setText("< Back");
        mBtnBack.setOnClickListener(new BackButtonClick());
    }

    private void initModeLevelRadioButtons()
    {


        // Radio buttons
        mTvModeLevelTitle = findViewById(R.id.modeTitle);
        mTvModeLevelTitle.setText(MODE_LEVEL_TITLE);
        mTvModeLevelTitle.setTypeface(null, Typeface.BOLD);
        mRgModeLevel = findViewById(R.id.modeLevel);
        mRbMode5x5 = findViewById(R.id.mode5x5);
        mRbMode3x5 = findViewById(R.id.mode3x5);
        mRbMode3x3 = findViewById(R.id.mode3x3);
        mRbMode1x3 = findViewById(R.id.mode1x3);
        mRbMode5x3 = findViewById(R.id.mode5x3);
        mRbMode1x5 = findViewById(R.id.mode1x5);
        // set checked button
        mRgModeLevel.check(modeLevelArray[mModeLevel]);

        mRgModeLevel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId)
                {
                    case R.id.mode5x5:
                        mModeLevel = Const.MODE_LEVEL_5x5;
                        break;
                    case R.id.mode3x5:
                        mModeLevel = Const.MODE_LEVEL_3x5;
                        break;
                    case R.id.mode3x3:
                        mModeLevel = Const.MODE_LEVEL_3x3;
                        break;
                    case R.id.mode1x3:
                        mModeLevel = Const.MODE_LEVEL_1x3;
                        break;
                    case R.id.mode5x3:
                        mModeLevel = Const.MODE_LEVEL_5x3;
                        mModeOption = Const.MODE_OPTION_FIX;
                        mRgModeOption.check(mModeOptionArray[mModeOption]);
                        break;
                    case R.id.mode1x5:
                        mModeLevel = Const.MODE_LEVEL_1x5;
                        mModeOption = Const.MODE_OPTION_FIX;
                        mRgModeOption.check(mModeOptionArray[mModeOption]);
                        break;
                    default:
                        Log.e(TAG, "Unknown mode level selected");
                }
                mWorkSets = Const.WORK_SETS[mModeLevel];
                mWorkReps = Const.WORK_REPS[mModeLevel];
                actualizeWorkSets();
            }
        });
    }

    private void initModeOptionRadioButtons()
    {

        mTvModeOptionTitle = findViewById(R.id.modeOptionTitle);
        mTvModeOptionTitle.setText(MODE_OPTION_TITLE);
        mTvModeOptionTitle.setTypeface(null, Typeface.BOLD);
        mRgModeOption = findViewById(R.id.modeOption);
        mRbModeFix = findViewById(R.id.modeFix);
        mRbModeVar = findViewById(R.id.modeVar);
        // set checked button
        mRgModeOption.check(mModeOptionArray[mModeOption]);

        mRgModeOption.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                switch (checkedId)
                {
                    case R.id.modeFix:
                        mModeOption = Const.MODE_OPTION_FIX;
                        break;
                    case R.id.modeVar:
                        mModeOption = Const.MODE_OPTION_VAR;
                        break;
                    default:
                        Log.e(TAG, "Unknown mode option selected");
                }
            }
        });
    }

    private void initWeightIncrRadioButtons()
    {

        mTvWeightIncrTitle = findViewById(R.id.sizeWeightIncr);
        mTvWeightIncrTitle.setText(WEIGHT_INCR_TITLE);
        mTvWeightIncrTitle.setTypeface(null, Typeface.BOLD);
        mRgWeightIncrSize = findViewById(R.id.weightIncrSize);
        mRbWeightIncrSmall = findViewById(R.id.weightIncrSmall);
        mRbWeightIncrBig = findViewById(R.id.weightIncrBig);
        // set checked button
        if (mWeightIncrSize == Const.WEIGHT_INCR_SMALL)
            mRgWeightIncrSize.check(R.id.weightIncrSmall);
        else
            mRgWeightIncrSize.check(R.id.weightIncrBig);

        mRgWeightIncrSize.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId)
                {
                    case R.id.weightIncrSmall:
                        mWeightIncrSize = Const.WEIGHT_INCR_SMALL;
                        break;
                    case R.id.weightIncrBig:
                        mWeightIncrSize = Const.WEIGHT_INCR_BIG;
                        break;
                    default:
                        Log.e(TAG, "Unknown weight increment size selected");
                }
            }
        });
    }

    private void doneButtonClicked()
    {

        if (mState == 0)
        {
            mFailedCnt = 0;  // reset
            mCurrWorkWeight = ExerciseInfo.incrWorkWeight(mCurrWorkWeight, mWeightIncrSize);
            mOutput = "well done, next time increase weight by " + mWeightIncrSize + "kg";
            mTvFailed.setText(Integer.toString(mFailedCnt));
            mTvOutput.setText(mOutput);
            actualizeWarmUpSets();
            actualizeWorkSets();
            mState = 1;
        }
        else if (mState == 1)
        {
            mOutput = "You already klicked Done!";
            mTvOutput.setText(mOutput);
        }
        else if (mState == -1)
        {
            mOutput = "You already klicked Failed!";
            mTvOutput.setText(mOutput);
        }
    }

    private void failedButtonClicked()
    {
        int newModeLevel;

        if (mState == 0)
        {
            mFailedCnt += 1;
            // change mode if possible
            if (mFailedCnt >= Const.DEFAULT_REPEAT_MAX_FAILS &&
                mDeloadedCnt >= Const.DEFAULT_DELOAD_MAX_FAILS)
            {

                newModeLevel = ExerciseInfo.nextModeLevel(mModeOption, mModeLevel);
                if (newModeLevel > mModeLevel)
                {
                    mModeLevel = newModeLevel;
                    mRgModeLevel.check(modeLevelArray[mModeLevel]);
                    mWorkSets = Const.WORK_SETS[mModeLevel];
                    mWorkReps = Const.WORK_REPS[mModeLevel];
                    mDeloadedCnt = 0; // Reset
                    mFailedCnt = 0;
                    mOutput = "change mode next time ...";
                }
                // deload
                else
                {
                    mOutput = "deload next time ...";
                    mCurrWorkWeight = ExerciseInfo.deloadWorkWeight(mCurrWorkWeight);
                    mDeloadedCnt += 1;
                    mFailedCnt = 0;
                }
            }
            // deload
            else if (mFailedCnt >= Const.DEFAULT_REPEAT_MAX_FAILS)
            {
                mOutput = "deload next time ...";
                mCurrWorkWeight = ExerciseInfo.deloadWorkWeight(mCurrWorkWeight);
                mDeloadedCnt += 1;
                mFailedCnt = 0;
            }
            // retry
            else
            {
                mOutput = "try again next time ...";
            }
            actualizeWarmUpSets();
            actualizeWorkSets();
            mTvOutput.setText(mOutput);
            mTvFailed.setText(Integer.toString(mFailedCnt));
            mTvDeloaded.setText(Integer.toString(mDeloadedCnt));
            mState = -1;
        }
        else if (mState == 1)
        {
            mOutput = "You already klicked Done!";
            mTvOutput.setText(mOutput);
        }
        else if (mState == -1)
        {
            mOutput = "You already klicked Failed!";
            mTvOutput.setText(mOutput);
        }
    }

    private void incrWeightSmallButtonClicked()
    {

        mCurrWorkWeight = ExerciseInfo.incrWorkWeight(mCurrWorkWeight, WEIGHT_CHANGE_SMALL);
        actualizeWarmUpSets();
        actualizeWorkSets();
    }

    private void incrWeightBigButtonClicked()
    {

        mCurrWorkWeight = ExerciseInfo.incrWorkWeight(mCurrWorkWeight, WEIGHT_CHANGE_BIG);
        actualizeWarmUpSets();
        actualizeWorkSets();
    }

    private void decrWeightSmallButtonClicked()
    {

        mCurrWorkWeight = ExerciseInfo.decrWorkWeight(mCurrWorkWeight, WEIGHT_CHANGE_SMALL);
        actualizeWarmUpSets();
        actualizeWorkSets();
    }

    private void decrWeightBigButtonClicked()
    {

        mCurrWorkWeight = ExerciseInfo.decrWorkWeight(mCurrWorkWeight, WEIGHT_CHANGE_BIG);
        actualizeWarmUpSets();
        actualizeWorkSets();
    }

    private void deleteButtonClicked()
    {
        // Build an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(ExerciseActivity.this);

        // Set a title for alert dialog
        builder.setTitle("Select your answer.");

        // Ask the final question
        builder.setMessage("Are you sure to delete Exercise" + mExerciseName + "?");

        // Set the alert dialog yes button click listener
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // Do something when user clicked the Yes button
                // Set the TextView visibility GONE
                Toast.makeText(getApplicationContext(),
                    "Exercise will be deleted", Toast.LENGTH_LONG).show();
                mDeleteExercise = 1;
            }
        });

        // Set the alert dialog no button click listener
        builder.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // Do something when No button clicked
                Toast.makeText(getApplicationContext(),
                    "Exercise will be kept", Toast.LENGTH_LONG).show();
            }
        });

        AlertDialog dialog = builder.create();
        // Display the alert dialog on interface
        dialog.show();
    }

    private void backButtonClicked()
    {
        onBackPressed();
    }

    private void doOnModeLevelChanged(RadioGroup group, int checkedId)
    {

    }

    class DoneButtonClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            doneButtonClicked();
        }
    }

    class FailedButtonClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            failedButtonClicked();
        }
    }

    class IncrWeightSmallButtonClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            incrWeightSmallButtonClicked();
        }
    }

    class IncrWeightBigButtonClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            incrWeightBigButtonClicked();
        }
    }

    class DecrWeightSmallButtonClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            decrWeightSmallButtonClicked();
        }
    }


    class DeleteButtonClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            deleteButtonClicked();
        }
    }

    class BackButtonClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            backButtonClicked();
        }
    }

    class DecrWeightBigButtonClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            decrWeightBigButtonClicked();
        }
    }

    class ModeLevelButtonCheck implements RadioGroup.OnCheckedChangeListener
    {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId)
        {
            doOnModeLevelChanged(group, checkedId);
        }
    }

    @Override
    protected void onPause()
    {

        super.onPause();
    }

    @Override
    public void onBackPressed()
    {

        saveNote();
        finish(); // enable Back Button
        super.onBackPressed();
    }

    public void saveNote()
    {

        Intent intent = new Intent();

        intent.putExtra(Const.KEY_EXERCISE, mExerciseName);
        intent.putExtra(Const.KEY_FAILED_CNT, Integer.toString(mFailedCnt));
        intent.putExtra(Const.KEY_DELOADED_CNT, Integer.toString(mDeloadedCnt));
        intent.putExtra(Const.KEY_CURR_WORK_WEIGHT, Double.toString(mCurrWorkWeight));
        intent.putExtra(Const.KEY_MODE_LEVEL, Integer.toString(mModeLevel));
        intent.putExtra(Const.KEY_MODE_OPTION, Integer.toString(mModeOption));
        intent.putExtra(Const.KEY_WEIGHT_INCR_SIZE, Double.toString(mWeightIncrSize));
        intent.putExtra(Const.KEY_DELETE_EXERCISE, Integer.toString(mDeleteExercise));

        setResult(RESULT_OK, intent);
    }
}
