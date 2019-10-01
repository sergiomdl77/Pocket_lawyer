package com.example.t2s;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    protected static final int REQ_CODE_SPEECH_INPUT = 100; // Speech recognition constraint
    protected TextView mVoiceInputTv; // Displays conversation on screen
    protected ImageButton mSpeakBtn; // Button to begin recording voice
    protected ImageButton mSaveConversationBtn; // Button to save conversations to logger
    int onItemSelectedListenerCheck = 0; // Used to bypass OnItemSelectedListener bug on startup
    Spinner recordsSpinner, regionsSpinner; // Two dropdown menus on phone screen
    Context context; // Contexts are used in this application to access phone's local storage
    ConversationLogger cl; // Class which handles all conversation loading requirements
    String[] fileList; // List of files stored in the phone's internal storage
    LawyerAppMaster appUtils; // Class which handles all text processing, search, and database reqs
    String conversation = ""; // Running String which records voice inputs and legal data

    public MainActivity(){

    } // Used by Android Studio

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Android Studio interfacing
        context = getApplicationContext(); // Get current context (local files)
        try {
            appUtils = new LawyerAppMaster(context); // Initialize utility layer
        } catch (IOException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_main); // Display main screen on phone

        mVoiceInputTv = findViewById(R.id.voiceInput); // User Interface Layer

        cl = new ConversationLogger(mVoiceInputTv, context); // Initialize Record logger
        cl.introductionPage(); // Introduction page will display prompt on startup of app

        recordsSpinner = findViewById(R.id.recordsSpinner); // UI initialization
        recordsSpinner.setOnItemSelectedListener(this); // Attach listener
        recordSpinnerUpdate(); // Update the current files displayed in Previous Records spinner

        regionsSpinner = findViewById(R.id.regionsSpinner); // UI initialization
        regionsSpinner.setOnItemSelectedListener(this); // Attach listener
        regionsSpinnerUpdate(); // Update the current states displayed in Regions spinner

        mSpeakBtn = findViewById(R.id.btnSpeak); // UI button setup
        mSpeakBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceInput();
            }
        });

        mSaveConversationBtn = findViewById(R.id.btnSave); // More UI button setup
        mSaveConversationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!conversation.equals("")) { // Only save non empty conversations
                    cl.save(conversation); // Use utility
                    recordSpinnerUpdate(); // Update spinner to show new file
                    conversation = ""; // Clear conversation
                }
            }
        });
        cl.display(recordsSpinner.getItemAtPosition(0).toString()); // Displays the intro page
    }

    private void recordSpinnerUpdate(){
        // Retrieve the files list
        fileList = context.fileList();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, fileList);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        recordsSpinner.setAdapter(dataAdapter);
    }

    private void regionsSpinnerUpdate(){

        // Can contain all fifty states, other countries, or any legal region.
        String[] regionAbbrevs = new String[2];
        regionAbbrevs[0]= "VA";
        regionAbbrevs[1] = "MD";

        // Attach spinner to String labels
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, regionAbbrevs);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regionsSpinner.setAdapter(dataAdapter);

    }

    // https://stacktips.com/tutorials/android/speech-to-text-in-android
    protected void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            a.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Sourced from same link as startVoiceInput()
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    // After getting result from voice input, format it to be displayed
                    conversation += result.get(0);
                    conversation += "\n\n";
                    // Retrieve legal input from helper class
                    String legalResult = appUtils.curConversation.parse(result.get(0));
                    if (!legalResult.equals("")) { // Add legal input if non-null
                        conversation += "Relevant Law:\n" + legalResult;
                    }
                    else{ // Otherwise, specify no search results
                        conversation += "No Relevant Law";
                    }
                    conversation += "\n\n";
                    mVoiceInputTv.setText(conversation); // Display conversation
                }
                break;
            }
        }
    }

    // This function handles drop down menu logic
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        if(++onItemSelectedListenerCheck>2) { // Android SDK bug bypass
            switch (parent.getId()) { // Determine which spinner is calling the function
                case R.id.recordsSpinner:
                    String filename = parent.getItemAtPosition(position).toString();
                    cl.display(filename); // Load and display the requested file
                    break;
                case R.id.regionsSpinner: // Switch region
                    conversation = "";
                    mVoiceInputTv.setText(conversation);
                    // Switch database information in the case of a region switch
                    if (parent.getItemAtPosition(position).toString().equals("VA")) {
                        try {
                            appUtils.regionSwitcher("lawTextFile.txt",
                                    "dictionaryTextFile.txt");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (parent.getItemAtPosition(position).toString().equals("MD")) {
                        try {
                            appUtils.regionSwitcher("lawTextFileMD.txt",
                                    "dictionaryTextFileMD.txt");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    // Display home page upon region switch
                    cl.display(recordsSpinner.getItemAtPosition(0).toString());
                    break;
            }
        }
    }

    // Not used, but needs to be written when using spinners.
    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}