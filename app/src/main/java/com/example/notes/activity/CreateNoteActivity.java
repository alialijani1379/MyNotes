package com.example.notes.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notes.R;

public class CreateNoteActivity extends AppCompatActivity {

    //<editor-fold desc="--Declaration--">
    public static final int REQUEST_CODE_ADD_NOTE = 1;

    private ImageView imgBack;
    private ImageView imgDone;
    private EditText edtNoteTitle;
    private EditText edtNoteSubtitle;
    private EditText edtNote;
    //</editor-fold>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

    }
}