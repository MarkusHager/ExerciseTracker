package com.markushager.exercisetracker;

import static java.lang.Math.round;
import com.markushager.exercisetracker.Const;

public class ExerciseInfo {

    private static final String TAG = ExerciseInfo.class.getSimpleName();
    private static final String HEADER = "Header";
    private String mExerciseName;
    private int mCurrId = 0;
    private int mId;
    private int mSets;
    private int mReps;
    private int mDeloadPercent;
    private int mDeloadedCnt = 0;
    private int mFailedCnt = 0;
    private int mModeLevel;
    private int mModeOption;
    private double mWeightIncrSize;
    private double mCurrWeight;
    private double mLastWeight;


    // für Header
    public ExerciseInfo() {
        mExerciseName = HEADER;
        mReps = 0;
        mSets = 0;
        mCurrWeight = 0.0;
    }


    public ExerciseInfo(String exerciseName, double currWeight) {

        mId = mCurrId++;
        mExerciseName = exerciseName;
        mCurrWeight = currWeight;
        initializeExercise();
    }

    public void initializeExercise() {

        mLastWeight = mCurrWeight;
        mModeLevel = Const.MODE_LEVEL[Const.DEFAULT_MODE_LEVEL];
        setModeLevel(mModeLevel);
        mDeloadPercent = Const.DEFAULT_DELOAD_PERCENTAGE;
        mDeloadedCnt = 0;
        mFailedCnt = 0;
        mModeOption = Const.DEFAULT_MODE_OPTION;
        mWeightIncrSize = Const.DEFAULT_WEIGHT_INCR;
    }


    public static double roundToNextWeight(double weight) {

        double roundedWeight = weight;
        // weight can only have multiples of WEIGHT_INCREMENTS
        roundedWeight = ((int)Math.round(weight / Const.WEIGHT_INCREMENTS) * Const.WEIGHT_INCREMENTS);

        return roundedWeight;
    }

    public static double getWarmupWeight(double currWeight, int set) {

        if (set < 1 || set > Const.WARMUP_PERCENTAGES.length)
            set = 1;
        currWeight = round(currWeight * Const.WARMUP_PERCENTAGES[set-1] / 100);
        currWeight = roundToNextWeight(currWeight);

        return currWeight;
    }


    public String getWorkMode() {

        return Const.WORK_SETS[mModeLevel] + " x " + Const.WORK_REPS[mModeLevel];
    }

    public int getModeLevel() {

        return mModeLevel;
    }

    public void setModeLevel(int modeLevel) {

        mModeLevel = modeLevel;
        mSets = Const.WORK_SETS[mModeLevel];
        mReps = Const.WORK_REPS[mModeLevel];
    }


    public static double incrWorkWeight(double weight, double weightChange) {

        weight += weightChange;

        return weight;
    }

    public static String doubleToString(double value) {
        return String.format("%.2f",  value);
    }

    public static double decrWorkWeight(double weight, double weightChange) {

        weight -= weightChange;
        if (weight < Const.MIN_WEIGHT)
            weight = Const.MIN_WEIGHT;

        return weight;
    }

    public void incrWorkWeight() {

        mLastWeight = mCurrWeight;
        mCurrWeight += mWeightIncrSize;
    }

    public static double deloadWorkWeight(double currWeight) {

        currWeight -= Const.DEFAULT_DELOAD_PERCENTAGE * currWeight / 100;
        currWeight = roundToNextWeight(currWeight);

        return currWeight;
    }

    public static int nextModeLevel(int modeOption, int modeLevel) {

        // only change Mode if user selected variable Mode
        if (modeOption == Const.MODE_OPTION_VAR) {
            // only change Mode if there is another Mode available
            if (hasNextModelLevel(modeLevel)) {
                modeLevel += 1;
                return modeLevel;
            }
        }
        return modeLevel;
    }

    // last Mode Level 1x5 is not valid!!!
    public static boolean hasNextModelLevel(int modeLevel) {
        return modeLevel < Const.MODE_LEVEL.length-1 ? true : false;
    }

    public int getSets(int modeLevel) {

        return Const.WORK_SETS[modeLevel];
    }
    public int getReps(int modeLevel) {

        return Const.WORK_REPS[modeLevel];
    }

    // Getters
    public String getExerciseName() { return mExerciseName; }
    public int getSets() { return mSets; }
    public int getReps() { return mReps; }
    public double getCurrWorkWeight() { return mCurrWeight; }
    public double getLastWorkWeight() { return mLastWeight; }
    public int getFailedCnt() { return mFailedCnt; }
    public int getDeloadedCnt() { return mDeloadedCnt; }
    public int getDeloadPercent() { return mDeloadPercent; }
    public int getModeOption() { return mModeOption; }
    public double getWeightIncrSize() { return  mWeightIncrSize; }

    // Setters
    public void setExerciseName(String exerciseName) { mExerciseName = exerciseName; }
    public void setFailedCnt(int failed) { mFailedCnt = failed; }
    public void setDeloadedCnt(int deloaded) { mDeloadedCnt = deloaded; }
    public void setCurrWorkWeight(double currWorkWeight) { mCurrWeight = currWorkWeight; }
    public void setLastWorkWeight(double lastWorkWeight) { mLastWeight = lastWorkWeight; }
    public void setReps(int reps) { mReps = reps; }
    public void setSets(int sets) { mSets = sets; }
    public void setModeOption(int modeOption)  { mModeOption = modeOption; }
    public void setWeightIncrSize(double weightIncrSize) { mWeightIncrSize = weightIncrSize; }
}

