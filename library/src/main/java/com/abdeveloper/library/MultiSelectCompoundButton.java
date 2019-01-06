package com.abdeveloper.library;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.CompoundButton;

import androidx.annotation.RequiresApi;

public class MultiSelectCompoundButton extends CompoundButton {

    public MultiSelectCompoundButton(Context context) {
        super(context);
    }

    public MultiSelectCompoundButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiSelectCompoundButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MultiSelectCompoundButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
