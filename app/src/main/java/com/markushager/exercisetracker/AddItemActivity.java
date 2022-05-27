package com.markushager.exercisetracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.markushager.exercisetracker.Const;


public class AddItemActivity extends AppCompatActivity {

    private final static String EMPTY_EXERCISE_MESSAGE = "Add Exercise";
    private EditText mTxtExercise;
    private Button mButtonAdd;
    private int mAddExercise = 0; // Default
    private String mExercise;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_item);

        Toolbar toolbar = findViewById(R.id.toolbar_add_item);
        setSupportActionBar(toolbar);

        mTxtExercise = findViewById(R.id.txtExercise);
        mButtonAdd = findViewById(R.id.buttonAddItem);
        mButtonAdd.setOnClickListener(new AddItemClick());

    }

    private void saveData() {

        Intent intent = new Intent();

        intent.putExtra(Const.KEY_EXERCISE, mExercise);
        intent.putExtra(Const.KEY_ADD_EXERCISE, Integer.toString(mAddExercise));

        setResult(RESULT_OK, intent);
    }

    @Override
    public void onBackPressed() {

        saveData();
        finish(); // enable Back Button
        super.onBackPressed();
    }

    private void addItemClicked() {

        try {
            if (mTxtExercise.getText().toString().equals(Const.EMPTY_STRING)) {
                Toast.makeText(getApplicationContext(), EMPTY_EXERCISE_MESSAGE,
                    Toast.LENGTH_LONG).show();
            } else {
                mExercise = mTxtExercise.getText().toString();
                mAddExercise = 1;
                onBackPressed();
            }
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    class AddItemClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            addItemClicked();
        }
    }
}
