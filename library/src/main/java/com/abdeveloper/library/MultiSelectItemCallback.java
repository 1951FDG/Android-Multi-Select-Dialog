package com.abdeveloper.library;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

class MultiSelectItemCallback extends DiffUtil.ItemCallback<MultiSelectable> {

    static final String SPAN = "SPAN";

    @Override
    public boolean areItemsTheSame(@NonNull MultiSelectable oldItem, @NonNull MultiSelectable newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull MultiSelectable oldItem, @NonNull MultiSelectable newItem) {
        if ((oldItem instanceof Range) && (newItem instanceof Range)) {
            int start = ((Range) oldItem).getStart();
            int end = ((Range) oldItem).getEnd();
            return ((start == ((Range) newItem).getStart()) && (end == ((Range) newItem).getEnd()));
        }

        return true;
    }

    public Object getChangePayload(@NonNull MultiSelectable oldItem, @NonNull MultiSelectable newItem) {
        Bundle payload = new Bundle();

        if (newItem instanceof Range) {
            int[] value = { ((Range) newItem).getStart(), ((Range) newItem).getEnd() };
            payload.putIntArray(SPAN, value);
        }

        return payload;
    }
}
