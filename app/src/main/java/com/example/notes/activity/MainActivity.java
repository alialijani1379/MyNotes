package com.example.notes.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.R;
import com.example.notes.customobject.TextViewCustom;
import com.example.notes.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //<editor-fold desc="--Declaration--">
    public static final int REQUEST_CODE_ADD_NOTE = 1;
    private ActivityMainBinding mainBinding;
    private TextViewCustom txtTime;
    private RecyclerView recyclerView;
    private FloatingActionButton fbAdd;
    //</editor-fold>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        bindViews(mainBinding);
        setTime();
        fbAdd.setOnClickListener(this);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fb_add_notes:
                Intent createNoteActivity = new Intent(this, CreateNoteActivity.class);
                startActivityForResult(createNoteActivity, REQUEST_CODE_ADD_NOTE);
//                startActivity(createNoteActivity);
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