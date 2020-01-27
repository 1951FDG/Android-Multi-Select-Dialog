package com.abdeveloper.library;

import android.text.Spannable;
import android.text.SpannableString;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

@SuppressWarnings({ "ParameterHidesMemberVariable", "TransientFieldInNonSerializableClass" })
public class MultiSelectModel implements MultiSelectable, Iconable, Range {

    private final int id;

    private final transient int resId;

    private transient int end;

    private transient int start;

    private CharSequence name;

    public MultiSelectModel(int id, @NonNull CharSequence name) {
        this(id, name, 0);
    }

    public MultiSelectModel(int id, @NonNull CharSequence name, int resId) {
        this.id = id;
        this.resId = resId;
        this.name = name;
    }

    @SuppressWarnings("unused")
    private MultiSelectModel() {
        id = 0;
        resId = 0;
    }

    @SuppressWarnings("FinalMethod")
    @Nullable
    @Override
    public final MultiSelectModel clone() {
        try {
            MultiSelectModel clone = (MultiSelectModel) super.clone();
            CharSequence name = clone.getName();
            if (name instanceof Spannable) {
                clone.setName(new SpannableString(name));
            }
            return clone;
        } catch (CloneNotSupportedException ignored) {
        }
        return null;
    }

    @Override
    public int getEnd() {
        return end;
    }

    @Override
    public void setEnd(int end) {
        this.end = end;
    }

    @Override
    public int getId() {
        return id;
    }

    @NonNull
    @Override
    public CharSequence getName() {
        return name;
    }

    @Override
    public void setName(@NonNull CharSequence charSequence) {
        name = charSequence;
    }

    @Override
    public int getResId() {
        return resId;
    }

    @Override
    public int getStart() {
        return start;
    }

    @Override
    public void setStart(int start) {
        this.start = start;
    }
}
