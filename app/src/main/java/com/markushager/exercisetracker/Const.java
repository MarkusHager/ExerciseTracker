package com.markushager.exercisetracker;


public class Const {

    public static final String VERSION = "1.0.1.1";

    public static final String FILENAME = "ExerciseTracker.txt";
    public static final String DEL = ";";
    public static final String EMPTY_STRING = "";
    // Keys
    public static final String KEY_MODE_LEVEL = "ModeLevel";
    public static final String KEY_MODE_OPTION = "ModeOption";
    public static final String KEY_WEIGHT_INCR_SIZE = "WeightIncr";
    public static final String KEY_EXERCISE = "Exercise";
    public static final String[] KEY_WARMUP_REPS =
        new String[] {"Warmup1Reps", "Warmup2Reps", "Warmup3Reps", "Warmup4Reps"};
    public static final String[] KEY_WARMUP_WEIGHT =
        new String[] {"Warmup1Weight", "Warmup2Weight", "Warmup3Weight", "Warmup4Weight"};
    public static final String KEY_WORK_MODE = "WorkMode";
    public static final String KEY_WORK_SETS = "WorkSets";
    public static final String KEY_WORK_REPS = "WorkReps";
    public static final String KEY_CURR_WORK_WEIGHT = "CurrWorkWeight";
    public static final String KEY_FAILED_CNT = "FailedCnt";
    public static final String KEY_DELOADED_CNT = "DeloadedCnt";
    public static final String KEY_STATE = "State";
    public static final String KEY_DELETE_EXERCISE = "DeleteExercise";
    public static final String KEY_ADD_EXERCISE = "AddExercise";


    public final static int REQUEST_CODE_EDIT_ITEM = 1;
    public final static int REQUEST_CODE_ADD_ITEM = 2;

    public static final int MODE_LEVEL_5x5 = 0;
    public static final int MODE_LEVEL_3x5 = 1;
    public static final int MODE_LEVEL_3x3 = 2;
    public static final int MODE_LEVEL_1x3 = 3;
    public static final int MODE_LEVEL_5x3 = 4;
    public static final int MODE_LEVEL_1x5 = 5;
    public static final int[] MODE_LEVEL =
        new int[] {MODE_LEVEL_5x5, MODE_LEVEL_3x5, MODE_LEVEL_3x3, MODE_LEVEL_1x3, MODE_LEVEL_5x3,
            MODE_LEVEL_1x5};
    public static final int DEFAULT_DELOAD_PERCENTAGE = 10;
    public static final int DEFAULT_DELOAD_MAX_FAILS = 2;
    public static final int DEFAULT_REPEAT_MAX_FAILS = 3;
    public static final int[] WARMUP_PERCENTAGES =  new int[] {50, 50, 70, 90};
    public static final int[] WARMUP_REPS =  new int[] {10, 8, 4, 1};
    public static final int[] WORK_SETS = new int[] {5, 3, 3, 1, 5, 1};
    public static final int[] WORK_REPS = new int[] {5, 5, 3, 3, 3, 5};
    public static final double WEIGHT_INCREMENTS = 2.5f;
    public static final double MIN_WEIGHT = 0f;
    public static final int MODE_OPTION_FIX = 0;
    public static final int MODE_OPTION_VAR = 1;
    public static final double DEFAULT_STARTING_WEIGHT = 40.0f;
    public static final double WEIGHT_INCR_SMALL = 1.25f;
    public static final double WEIGHT_INCR_BIG = 2.5f;
    public static final int DEFAULT_MODE_LEVEL = MODE_LEVEL_3x5;
    public static final int DEFAULT_MODE_OPTION = MODE_OPTION_VAR;
    public static final double DEFAULT_WEIGHT_INCR = WEIGHT_INCR_SMALL;
}
