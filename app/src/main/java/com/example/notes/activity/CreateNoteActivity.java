package com.example.notes.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.example.notes.R;
import com.example.notes.customobject.TextViewCustom;
import com.example.notes.databinding.ActivityCreateNoteBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateNoteActivity extends AppCompatActivity implements View.OnClickListener {

    //<editor-fold desc="--Declaration--">
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String SUBTITLE = "subtitle";
    public static final String NOTE = "note";
    public static final String DATE_TIME = "date";
    public static final String IMAGE_PATH = "image_path";
    public static final String COLOR = "color";
    public static final String WEB_LINK = "web_link";
    public static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    public static final int REQUEST_CODE_SELECT_IMAGE = 2;
    private ActivityCreateNoteBinding createNoteBinding;
    private ImageView imgBack;
    private ImageView imgDone;
    private TextView txtDate;
    private EditText edtNoteTitle;
    private EditText edtNoteSubtitle;
    private ImageView imgNote;
    private LinearLayout layoutUrl;
    private TextViewCustom txtUrl;
    private EditText edtNote;
    private View viewSubtitleIndicator;
    private String selectedNoteColor;
    private String selectedImagePath;
    private AlertDialog dialogAddUrl;
    private String color;
    //</editor-fold>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNoteBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_note);
        bindViews(createNoteBinding);

        imgBack.setOnClickListener(this);
        imgDone.setOnClickListener(this);
        txtDate.setText(new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault()).format(new Date()));
        selectedNoteColor = "#535353";
        selectedImagePath = "";
        setUpdateNote();
        initBottomSheet();
        setSubtitleIndicatorColor();

    }

    private void setUpdateNote() {
        Intent intent = getIntent();
        intent.hasExtra(ID);
        String title = intent.getStringExtra(MainActivity.TITLE_U);
        String subtitle = intent.getStringExtra(MainActivity.SUBTITLE_U);
        String note = intent.getStringExtra(MainActivity.NOTE_U);
        String date = intent.getStringExtra(MainActivity.DATE_U);
        String link = intent.getStringExtra(MainActivity.WEB_LINK_U);
        String img = intent.getStringExtra(MainActivity.IMAGE_PATH_U);
        color = intent.getStringExtra(MainActivity.COLOR_U);
        edtNoteTitle.setText(title);
        edtNoteSubtitle.setText(subtitle);
        edtNote.setText(note);
        txtDate.setText(date);
        txtUrl.setText(link);
        txtUrl.setVisibility(View.VISIBLE);
        imgNote.setImageBitmap(BitmapFactory.decodeFile(img));
        if (img != null) {
            imgNote.setVisibility(View.VISIBLE);
        }
        selectedImagePath = img;
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
        data.putExtra(COLOR, selectedNoteColor);
        data.putExtra(IMAGE_PATH, selectedImagePath);
        if (layoutUrl.getVisibility() == View.VISIBLE) {
            data.putExtra(WEB_LINK, txtUrl.getText().toString());
        }
        int id = getIntent().getIntExtra(ID, -1);
        if (id != -1) {
            data.putExtra(ID, id);
        }

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

        bottomSheet.findViewById(R.id.txt_miscellaneous).setOnClickListener(v -> {
            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        final ImageView imageColor1 = bottomSheet.findViewById(R.id.img_color1);
        final ImageView imageColor2 = bottomSheet.findViewById(R.id.img_color2);
        final ImageView imageColor3 = bottomSheet.findViewById(R.id.img_color3);
        final ImageView imageColor4 = bottomSheet.findViewById(R.id.img_color4);
        final ImageView imageColor5 = bottomSheet.findViewById(R.id.img_color5);

        bottomSheet.findViewById(R.id.view_color1).setOnClickListener(v -> {
            selectedNoteColor = "#535353";
            imageColor1.setImageResource(R.drawable.ic_baseline_done);
            imageColor2.setImageResource(0);
            imageColor3.setImageResource(0);
            imageColor4.setImageResource(0);
            imageColor5.setImageResource(0);
            setSubtitleIndicatorColor();
        });

        bottomSheet.findViewById(R.id.view_color2).setOnClickListener(v -> {
            selectedNoteColor = "#F44336";
            imageColor1.setImageResource(0);
            imageColor2.setImageResource(R.drawable.ic_baseline_done);
            imageColor3.setImageResource(0);
            imageColor4.setImageResource(0);
            imageColor5.setImageResource(0);
            setSubtitleIndicatorColor();
        });

        bottomSheet.findViewById(R.id.view_color3).setOnClickListener(v -> {
            selectedNoteColor = "#0377AC";
            imageColor1.setImageResource(0);
            imageColor2.setImageResource(0);
            imageColor3.setImageResource(R.drawable.ic_baseline_done);
            imageColor4.setImageResource(0);
            imageColor5.setImageResource(0);
            setSubtitleIndicatorColor();
        });

        bottomSheet.findViewById(R.id.view_color4).setOnClickListener(v -> {
            selectedNoteColor = "#76BC25";
            imageColor1.setImageResource(0);
            imageColor2.setImageResource(0);
            imageColor3.setImageResource(0);
            imageColor4.setImageResource(R.drawable.ic_baseline_done);
            imageColor5.setImageResource(0);
            setSubtitleIndicatorColor();
        });

        bottomSheet.findViewById(R.id.view_color5).setOnClickListener(v -> {
            selectedNoteColor = "#FF9800";
            imageColor1.setImageResource(0);
            imageColor2.setImageResource(0);
            imageColor3.setImageResource(0);
            imageColor4.setImageResource(0);
            imageColor5.setImageResource(R.drawable.ic_baseline_done);
            setSubtitleIndicatorColor();
        });

//        switch (color) {
//            case "#535353":
//                bottomSheet.findViewById(R.id.view_color1).performClick();
//                break;
//            case "#F44336":
//                bottomSheet.findViewById(R.id.view_color2).performClick();
//                break;
//            case "#0377AC":
//                bottomSheet.findViewById(R.id.view_color3).performClick();
//                break;
//            case "#76BC25":
//                bottomSheet.findViewById(R.id.view_color4).performClick();
//                break;
//            case "#FF9800":
//                bottomSheet.findViewById(R.id.view_color5).performClick();
//                break;
//        }

        bottomSheet.findViewById(R.id.layout_add_img).setOnClickListener(v -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CreateNoteActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}
                        , REQUEST_CODE_STORAGE_PERMISSION);
            } else {
                selectImage();
            }
        });

        bottomSheet.findViewById(R.id.layout_add_url).setOnClickListener(v -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            showAddUrlDialog();

        });

    }

    private void setSubtitleIndicatorColor() {
        GradientDrawable gradientDrawable = (GradientDrawable) viewSubtitleIndicator.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectedNoteColor));
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else {
                Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        imgNote.setImageBitmap(bitmap);
                        imgNote.setVisibility(View.VISIBLE);
                        selectedImagePath = getPathFromUri(selectedImageUri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private String getPathFromUri(Uri uri) {
        String filePath;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            filePath = uri.getPath();
        } else {
            cursor.moveToNext();
            int index = cursor.getColumnIndex("_data");
            filePath = cursor.getString(index);
            cursor.close();
        }
        return filePath;
    }

    private void showAddUrlDialog() {
        if (dialogAddUrl == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = LayoutInflater.from(this).inflate(R.layout.item_add_url, null);
            if (view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }

            final EditText edtUrl = view.findViewById(R.id.edt_url);
            edtUrl.requestFocus();

            TextViewCustom txtAdd = view.findViewById(R.id.txt_add_url);
            TextViewCustom txtCancel = view.findViewById(R.id.txt_cancel);

            txtAdd.setOnClickListener(v -> {
                if (edtUrl.getText().toString().trim().isEmpty()) {
                    Toast.makeText(CreateNoteActivity.this, R.string.enter_url, Toast.LENGTH_SHORT).show();
                } else if (!Patterns.WEB_URL.matcher(edtUrl.getText().toString().trim()).matches()) {
                    Toast.makeText(CreateNoteActivity.this, R.string.enter_valid_url, Toast.LENGTH_SHORT).show();
                } else {
                    txtUrl.setText(edtUrl.getText().toString().trim());
                    layoutUrl.setVisibility(View.VISIBLE);
                    dialogAddUrl.dismiss();
                }
            });

            txtCancel.setOnClickListener(v -> dialogAddUrl.dismiss());
            builder.setView(view);
            dialogAddUrl = builder.create();
            if (dialogAddUrl.getWindow() != null) {
                dialogAddUrl.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
        }
        dialogAddUrl.show();
    }

    private void bindViews(ActivityCreateNoteBinding createNoteBinding) {
        imgBack = createNoteBinding.imgBack;
        imgDone = createNoteBinding.imgDone;
        txtDate = createNoteBinding.txtDate;
        edtNoteTitle = createNoteBinding.edtNoteTitle;
        edtNoteSubtitle = createNoteBinding.edtNotesSubtitle;
        imgNote = createNoteBinding.imgNote;
        layoutUrl = createNoteBinding.layoutUrl;
        txtUrl = createNoteBinding.txtUrl;
        edtNote = createNoteBinding.edtNote;
        viewSubtitleIndicator = createNoteBinding.viewSubtitleIndicator;

    }

}