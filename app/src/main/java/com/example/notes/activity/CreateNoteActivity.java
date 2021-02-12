package com.example.notes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.notes.R;
import com.example.notes.customobject.TextViewCustom;
import com.example.notes.databinding.ActivityCreateNoteBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateNoteActivity extends AppCompatActivity implements View.OnClickListener {

    //<editor-fold desc="--Declaration--">
    public static final String TITLE = "title";
    public static final String SUBTITLE = "subtitle";
    public static final String NOTE = "note";
    public static final String DATE_TIME = "date";
    public static final String IMAGE_PATH = "image_path";
    public static final String COLOR = "color";
    public static final String WEB_LINK = "web_link";
    private ActivityCreateNoteBinding createNoteBinding;
    private ImageView imgBack;
    private ImageView imgDone;
    private TextViewCustom txtDate;
    private EditText edtNoteTitle;
    private EditText edtNoteSubtitle;
    private EditText edtNote;
    private String selectedNoteColor;
    //</editor-fold>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNoteBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_note);
        bindViews(createNoteBinding);
        initBottomSheet();
        imgBack.setOnClickListener(this);
        imgDone.setOnClickListener(this);
        txtDate.setText(new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault()).format(new Date()));

        selectedNoteColor = "#333333";

    }

    private void saveNote() {
        String title = edtNoteTitle.getText().toString().trim();
        String noteSubtitle = edtNoteSubtitle.getText().toString().trim();
        String note = edtNote.getText().toString().trim();
        String dateTime = txtDate.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, "Title can't be empty", Toast.LENGTH_SHORT).show();
            return;
        } else if (noteSubtitle.isEmpty() && note.isEmpty()) {
            Toast.makeText(this, "Note can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = getIntent();
        data.putExtra(TITLE, title);
        data.putExtra(SUBTITLE, noteSubtitle);
        data.putExtra(NOTE, note);
        data.putExtra(DATE_TIME, dateTime);

        setResult(RESULT_OK, data);
        finish();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.img_done:
                saveNote();
                break;
        }
    }

    private void initBottomSheet() {
        final LinearLayout bottomSheet = findViewById(R.id.bottom_sheet);
        final BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheet.findViewById(R.id.txt_miscellaneous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
    }

    private void bindViews(ActivityCreateNoteBinding createNoteBinding) {
        imgBack = createNoteBinding.imgBack;
        imgDone = createNoteBinding.imgDone;
        txtDate = createNoteBinding.txtDate;
        edtNoteTitle = createNoteBinding.edtNoteTitle;
        edtNoteSubtitle = createNoteBinding.edtNotesSubtitle;
        edtNote = createNoteBinding.edtNote;
    }

}