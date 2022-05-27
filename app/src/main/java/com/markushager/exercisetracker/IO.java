package com.markushager.exercisetracker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;


public class IO {
    public static final String TAG = IO.class.getSimpleName();

    private String mFilename;
    // Application path: Files there can only be accessed by the application
    private File mInternalPath;
    // Phone or SD path: Files there can be accessed by other applications
    // and Explorer
    //private File external_path; // SD card path
    private List<String> mBuffer = null;
    private com.markushager.exercisetracker.MainActivity mMainActivity = null;
    private File mFile;

    //  -------------------------------------------------------------------------------------------
    //  Constructor
    //  -------------------------------------------------------------------------------------------
    public IO (com.markushager.exercisetracker.MainActivity main, String filename) {
        this.mMainActivity = main;
        this.mFilename = filename;
        this.mFile = new File(main.getExternalFilesDir(null), filename);
    }

    //  -------------------------------------------------------------------------------------------
    // read complete file content
    //  -------------------------------------------------------------------------------------------
    public boolean readFile() throws IOException {
        boolean success = true;
        BufferedReader reader = null;
        String line;
        int idx = 1;

        try {
            mFilename = mFile.toString();
            reader = new BufferedReader(new FileReader(mFile));
            mBuffer = new ArrayList<String>();
            //Log.d(TAG, "start reading from file: " + mFile.toString());
            while ((line = reader.readLine()) != null) {
                mBuffer.add(line);
                //Log.d(TAG, idx++ + ": " + line);
            }
            //Log.d(TAG, "end reading from file: " + mFile.toString());
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage());
            success = false;
        }
        finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            }
            catch (Exception e) {
                Log.e(TAG, e.getMessage());
                success = false;
            }
            return success;
        }
    }

    //  -------------------------------------------------------------------------------------------
    //  write data into a file
    //  append: if true, than data will be append to existing file
    //  -------------------------------------------------------------------------------------------
    public boolean writeFile(List<String> list, boolean append) throws IOException {
        boolean success;
        BufferedWriter writer = null;
        String newline = "\n";
        int idx = 1;

        try {
            mFilename = mFile.toString();
            if (!mFile.exists())
                mFile.createNewFile();
            writer = new BufferedWriter(new FileWriter(mFile, append));
            //Log.d(TAG, "start writing to file: " + mFile.toString());
            for(String line : list) {
                writer.write(line + newline);
                //Log.d(TAG, idx++ + ": " + line);
            }
            //Log.d(TAG, "end writing to file: " + mFile.toString());
            // Refresh the data so it can seen when the device is plugged in a
            // computer. You may have to unplug and replug the device to see the
            // latest changes. This is not necessary if the user should not modify
            // the files.
            /*MediaScannerConnection.scanFile(this, new String[]{testFile.toString()}, null, null);*/
            Log.d(TAG, "filename: " + mFile.toString());
            success = true;
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage());
            success = false;
        }
        finally {
            writer.flush();
            writer.close();
        }

        return success;
    }

    public boolean fileExists() {
        if (mFile.exists())
            return true;
        else
            return false;
    }

    //  -------------------------------------------------------------------------------------------
    //  getters, setters
    //  -------------------------------------------------------------------------------------------
    public List<String> getBuffer() { return mBuffer; }
    public String getFilename() { return mFilename; }
}

