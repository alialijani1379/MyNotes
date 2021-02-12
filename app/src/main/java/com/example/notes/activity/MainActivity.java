package com.example.notes.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.notes.R;
import com.example.notes.adapter.NoteAdapter;
import com.example.notes.adapter.NotesAdapter;
import com.example.notes.customobject.TextViewCustom;
import com.example.notes.databinding.ActivityMainBinding;
import com.example.notes.entities.Note;
import com.example.notes.viewmodel.NotesViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //<editor-fold desc="--Declaration--">
    public static final int REQUEST_CODE_ADD_NOTE = 1;
    private ActivityMainBinding mainBinding;
    private TextViewCustom txtTime;
    private RecyclerView recyclerView;
    private FloatingActionButton fbAdd;
    private List<Note> notes;
    private NotesAdapter notesAdapter;
    private NotesViewModel notesViewModel;
    private NoteAdapter adapter;
    //</editor-fold>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        bindViews(mainBinding);
        setTime();
        fbAdd.setOnClickListener(this);

        setUpRecyclerView();
        notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);
        notesViewModel.getAllNotes().observe(this, notes -> adapter.setNotes(notes));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK) {
            String title = data.getStringExtra(CreateNoteActivity.TITLE);
            String subtitle = data.getStringExtra(CreateNoteActivity.SUBTITLE);
            String note = data.getStringExtra(CreateNoteActivity.NOTE);
            String dateTime = data.getStringExtra(CreateNoteActivity.DATE_TIME);
            String imagePath = data.getStringExtra(CreateNoteActivity.IMAGE_PATH);
            String color = data.getStringExtra(CreateNoteActivity.COLOR);
            String webLink = data.getStringExtra(CreateNoteActivity.WEB_LINK);
            Note note1 = new Note(title, dateTime, subtitle, note, imagePath, color, webLink);
            NotesViewModel notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);
            notesViewModel.insert(note1);
            Toast.makeText(this, "Insert OK", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Insert NOT OK", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpRecyclerView() {
        notes = new ArrayList<>();
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setHasFixedSize(true);
        recyclerView.smoothScrollToPosition(0);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new NoteAdapter(this, notes);
//        notesAdapter = new NotesAdapter(this, notes);
        recyclerView.setAdapter(adapter);
//        notesAdapter.submitList(notes);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fb_add_notes:
                Intent createNoteActivity = new Intent(this, CreateNoteActivity.class);
                startActivityForResult(createNoteActivity, REQUEST_CODE_ADD_NOTE);
                break;
            case R.id.img_add_image:

                break;
            case R.id.img_add_web_link:

                break;
            case R.id.img_add_notes:

                break;
        }
    }

    private void setTime() {
        Calendar calendar = Calendar.getInstance();

        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        if (timeOfDay >= 0 && timeOfDay < 12) {
            //morning
            txtTime.setText(R.string.good_morning);
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            //afternoon
            txtTime.setText(R.string.good_afternoon);
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            //evening
            txtTime.setText(R.string.good_evening);
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            //night
            txtTime.setText(R.string.good_night);
        }
    }

    private void bindViews(ActivityMainBinding mainBinding) {
        txtTime = mainBinding.txtTime;
        recyclerView = mainBinding.rvNotes;
        fbAdd = mainBinding.fbAddNotes;
    }

}