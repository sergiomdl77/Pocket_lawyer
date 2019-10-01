package com.example.t2s;

import android.content.Context;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

// Save, Load, and Display Files from the phone's internal storage to the phone screen
public class ConversationLogger {

    // UI and File context variables received from Main class
    protected TextView mVoiceInputTv;
    Context context;

    public ConversationLogger(TextView inputVoiceInputTv, Context context){
        this.mVoiceInputTv = inputVoiceInputTv;
        this.context = context;
    }

    // Introduction page will display a prompt for the user on the main screen
    public void introductionPage(){
        if (context.fileList().length == 0) {
            String conversation = "Conversations will appear here.";
            save(conversation);
        }
    }

    // https://developer.android.com/training/data-storage/files
    // Save a file to internal storage on the phone. The contents of the file are the inputs
    // as well as the legal results in each conversation.
    public /*String*/ void save(String conversation){
        String filename;
        if (context.fileList().length == 0){
            filename = "introduction.dat";
        }
        else {
            filename = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss'.dat'").format(new Date());
        }
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(conversation.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Same source as save()
    // Load and display a file with the file name retrieved from the Main class
    public void display(String filename){
        FileInputStream inputStream;
        try{
            inputStream = context.openFileInput(filename);
            StringBuffer fileContent = new StringBuffer("");
            byte[] buffer = new byte[1024];
            // https://stackoverflow.com/questions/9095610/android-fileinputstream-read-txt-file-to-string
            int n;
            while ((n=inputStream.read(buffer)) != -1){
                fileContent.append(new String(buffer, 0, n));
            }
            String result = fileContent.toString();
            mVoiceInputTv.setText(result); // Display result
            inputStream.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
