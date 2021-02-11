package com.example.notes.customobject;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

public class EditTextCustom extends AppCompatEditText {

    private Typeface typeface;

    public EditTextCustom(@NonNull Context context) {
        super(context);
    }

    public EditTextCustom(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextCustom(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (typeface == null) {
            typeface = Typeface.createFromAsset(getContext().getAssets(), "iran_sans_mobile_fa.ttf");
            setTypeface(typeface);
        }
    }
}
