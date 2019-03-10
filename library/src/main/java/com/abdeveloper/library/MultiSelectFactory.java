package com.abdeveloper.library;

import android.text.Spannable;

class MultiSelectFactory extends Spannable.Factory {

    private static final MultiSelectFactory sInstance = new MultiSelectFactory();

    public static MultiSelectFactory getInstance() {
        return sInstance;
    }

    @Override
    public Spannable newSpannable(CharSequence source) {
        return (Spannable) source;
    }
}
