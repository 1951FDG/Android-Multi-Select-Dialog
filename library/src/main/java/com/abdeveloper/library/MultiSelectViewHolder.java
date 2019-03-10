package com.abdeveloper.library;

import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class MultiSelectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public interface SelectionCallbackListener {

        boolean addToSelection(@NonNull Integer id);

        boolean isSelected(@NonNull Integer id);

        boolean removeFromSelection(@NonNull Integer id);
    }

    private final int color;

    private final Checkable dialog_item_checkbox;

    private final ImageView dialog_item_icon;

    private final TextView dialog_item_name;

    private SelectionCallbackListener itemViewClickListener;

    MultiSelectViewHolder(View view, ImageView v1, TextView v2, Checkable v3, int id) {
        super(view);
        dialog_item_icon = v1;
        dialog_item_name = v2;
        dialog_item_checkbox = v3;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog_item_icon.setClipToOutline(true);
        }
        color = id;
        Spannable.Factory spannableFactory = MultiSelectFactory.getInstance();
        dialog_item_name.setSpannableFactory(spannableFactory);
    }

    @Override
    public void onClick(View v) {
        int id = (int) getItemId();
        SelectionCallbackListener listener = itemViewClickListener;
        boolean checked = dialog_item_checkbox.isChecked();
        if (listener != null) {
            if (checked) {
                listener.removeFromSelection(id);
            } else {
                listener.addToSelection(id);
            }
        }
        dialog_item_checkbox.setChecked(!checked);
    }

    void bind(MultiSelectable model, SelectionCallbackListener listener) {
        int resId = 0;
        if (model instanceof Iconifiable) {
            resId = ((Iconifiable) model).getResId();
        }
        if (resId == 0) {
            if (dialog_item_icon.getVisibility() != View.GONE) {
                dialog_item_icon.setVisibility(View.GONE);
            }
        } else {
            dialog_item_icon.setImageResource(resId);
        }
        CharSequence name = model.getName();
        if (name instanceof Spannable) {
            dialog_item_name.setText(name, TextView.BufferType.SPANNABLE);
            if (model instanceof Range) {
                CharSequence text = dialog_item_name.getText();
                ForegroundColorSpan span = new ForegroundColorSpan(color);
                int start = ((Range) model).getStart();
                int end = ((Range) model).getEnd();
                ((Spannable) text).setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } else {
            dialog_item_name.setText(name, TextView.BufferType.NORMAL);
        }
        if (listener != null) {
            int id = model.getId();
            boolean checked = listener.isSelected(id);
            dialog_item_checkbox.setChecked(checked);
        }
        itemViewClickListener = listener;
        if (!itemView.hasOnClickListeners()) {
            itemView.setOnClickListener(this);
        }
    }

    void bind(MultiSelectable model, SelectionCallbackListener listener, List<Object> payloads) {
        Bundle bundle = (Bundle) payloads.get(0);
        int[] array = bundle.getIntArray(MultiSelectItemCallback.SPAN);
        if ((array != null) && (array.length != 0)) {
            CharSequence text = dialog_item_name.getText();
            if (text instanceof Spannable) {
                ForegroundColorSpan[] spans = ((Spannable) text).getSpans(0, text.length(), ForegroundColorSpan.class);
                if (spans.length > 0) {
                    ((Spannable) text).setSpan(spans[0], array[0], array[1], Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
    }

}
