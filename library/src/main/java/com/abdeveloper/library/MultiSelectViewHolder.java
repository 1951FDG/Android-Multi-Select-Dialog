package com.abdeveloper.library;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.widget.CompoundButtonCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class MultiSelectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public interface SelectionCallbackListener {

        boolean addToSelection(@NonNull Integer id);

        boolean isSelected(@NonNull Integer id);

        boolean removeFromSelection(@NonNull Integer id);
    }

    private final ImageView imageView;

    private final SelectionCallbackListener listener;

    private final TextView titleView;

    private final StyleSpan what = new StyleSpan(Typeface.BOLD);

    MultiSelectViewHolder(@NonNull View v, SelectionCallbackListener l) {
        super(v);
        listener = l;
        View checkboxView = v.findViewById(R.id.dialog_item_checkbox);
        imageView = v.findViewById(R.id.dialog_item_icon);
        titleView = v.findViewById(R.id.dialog_item_name);
        if (checkboxView instanceof ImageView) {
            ImageViewCompat.setImageTintList(((ImageView) checkboxView),
                    AppCompatResources.getColorStateList(checkboxView.getContext(), R.color.control_checkable_material));
        }
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (checkboxView instanceof CompoundButton) {
                CompoundButtonCompat.setButtonTintList(((CompoundButton) checkboxView),
                        AppCompatResources.getColorStateList(checkboxView.getContext(), R.color.control_checkable_material));
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.setClipToOutline(true);
        }
        Spannable.Factory spannableFactory = MultiSelectFactory.getInstance();
        titleView.setSpannableFactory(spannableFactory);
        v.setOnClickListener(this);
    }

    @Override
    public void onClick(@NonNull View v) {
        int id = (int) getItemId();
        boolean checked = ((Checkable) v).isChecked();
        if (listener != null) {
            if (checked) {
                listener.removeFromSelection(id);
            } else {
                listener.addToSelection(id);
            }
        }
        ((Checkable) v).setChecked(!checked);
    }

    void bind(MultiSelectable model) {
        int resId = 0;
        if (model instanceof Iconifiable) {
            resId = ((Iconifiable) model).getResId();
        }
        if (resId == 0) {
            if (imageView.getVisibility() != View.GONE) {
                imageView.setVisibility(View.GONE);
            }
        } else {
            imageView.setImageResource(resId);
        }
        CharSequence name = model.getName();
        if (name instanceof Spannable) {
            titleView.setText(name, TextView.BufferType.SPANNABLE);
            if (model instanceof Range) {
                CharSequence text = titleView.getText();
                int start = ((Range) model).getStart();
                int end = ((Range) model).getEnd();
                ((Spannable) text).setSpan(what, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } else {
            titleView.setText(name, TextView.BufferType.NORMAL);
        }
        if (listener != null) {
            int id = model.getId();
            boolean checked = listener.isSelected(id);
            ((Checkable) itemView).setChecked(checked);
        }
    }

    void bind(MultiSelectable model, List<Object> payloads) {
        Bundle bundle = (Bundle) payloads.get(0);
        int[] array = bundle.getIntArray(MultiSelectItemCallback.SPAN);
        if ((array != null) && (array.length != 0)) {
            CharSequence text = titleView.getText();
            if (text instanceof Spannable) {
                StyleSpan[] spans = ((Spannable) text).getSpans(0, text.length(), StyleSpan.class);
                if (spans.length > 0) {
                    ((Spannable) text).setSpan(spans[0], array[0], array[1], Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
    }
}
