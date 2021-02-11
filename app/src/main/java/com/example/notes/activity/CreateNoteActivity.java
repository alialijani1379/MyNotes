package com.example.notes.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.notes.R;
import com.example.notes.customobject.TextViewCustom;
import com.example.notes.databinding.ActivityCreateNoteBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateNoteActivity extends AppCompatActivity implements View.OnClickListener {

    //<editor-fold desc="--Declaration--">
    private ActivityCreateNoteBinding createNoteBinding;
    private ImageView imgBack;
    private ImageView imgDone;
    private TextViewCustom txtDate;
    private EditText edtNoteTitle;
    private EditText edtNoteSubtitle;
    private EditText edtNote;
    //</editor-fold>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNoteBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_note);
        bindViews(createNoteBinding);
        imgBack.setOnClickListener(this);
        txtDate.setText(new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault()).format(new Date()));

    }

    private void saveNote() {
        String title = edtNoteTitle.getText().toString().trim();
        String noteSubtitle = edtNoteSubtitle.getText().toString().trim();
        String note = edtNote.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, "Title can't be empty", Toast.LENGTH_SHORT).show();
            return;
        } else if (noteSubtitle.isEmpty() && note.isEmpty()) {
            Toast.makeText(this, "Note can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }



    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.img_back:
                onBackPressed();
                break;
        }
    }

    private void bindViews(ActivityCreateNoteBinding createNoteBinding) {
        imgBack = createNoteBinding.imgBack;
        imgDone = createNoteBinding.imgDone;
        txtDate = createNoteBinding.txtDate;
        edtNoteTitle = createNoteBinding.edtNoteTitle;
        edtNoteSubtitle = createNoteBinding.edtNoteTitle;
        edtNote = createNoteBinding.edtNote;
    }

}