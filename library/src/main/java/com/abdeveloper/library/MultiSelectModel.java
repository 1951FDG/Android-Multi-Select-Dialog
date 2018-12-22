package com.abdeveloper.library;

import android.text.SpannableString;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

public class MultiSelectModel {
    private final int id;
    private SpannableString name;
    private int resource;

    public MultiSelectModel(int uniqueId, @NonNull SpannableString str) {
        this(uniqueId, str, 0);
    }

    public MultiSelectModel(int uniqueId, @NonNull SpannableString str, @DrawableRes int resId) {
        id = uniqueId;
        name = str;
        resource = resId;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public SpannableString getName() {
        return name;
    }

    public void setName(@NonNull SpannableString str) {
        name = str;
    }

    public int getImageResource() {
        return resource;
    }
}
