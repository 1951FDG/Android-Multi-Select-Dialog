package com.abdeveloper.library;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class MultiSelectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public interface SelectionCallbackListener {

        boolean addToSelection(@NonNull Integer id);

        boolean isSelected(@NonNull Integer id);

        boolean removeFromSelection(@NonNull Integer id);
    }

    private final AppCompatCheckBox dialog_item_checkbox;

    private final ImageView dialog_item_icon;

    private final TextView dialog_item_name;

    private final SelectionCallbackListener itemViewClickListener;

    private final StyleSpan what = new StyleSpan(Typeface.BOLD);

    MultiSelectViewHolder(@NonNull View itemView, SelectionCallbackListener listener) {
        super(itemView);
        itemViewClickListener = listener;
        dialog_item_checkbox = itemView.findViewById(R.id.dialog_item_checkbox);
        dialog_item_icon = itemView.findViewById(R.id.dialog_item_icon);
        dialog_item_name = itemView.findViewById(R.id.dialog_item_name);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            dialog_item_checkbox.setSupportButtonTintList(
                    AppCompatResources.getColorStateList(dialog_item_checkbox.getContext(), R.color.control_checkable_material));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog_item_icon.setClipToOutline(true);
        }
        Spannable.Factory spannableFactory = MultiSelectFactory.getInstance();
        dialog_item_name.setSpannableFactory(spannableFactory);
        itemView.setOnClickListener(this);
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

    void bind(MultiSelectable model) {
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
                int start = ((Range) model).getStart();
                int end = ((Range) model).getEnd();
                ((Spannable) text).setSpan(what, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } else {
            dialog_item_name.setText(name, TextView.BufferType.NORMAL);
        }
        SelectionCallbackListener listener = itemViewClickListener;
        if (listener != null) {
            int id = model.getId();
            boolean checked = listener.isSelected(id);
            dialog_item_checkbox.setChecked(checked);
        }
    }

    void bind(MultiSelectable model, List<Object> payloads) {
        Bundle bundle = (Bundle) payloads.get(0);
        int[] array = bundle.getIntArray(MultiSelectItemCallback.SPAN);
        if ((array != null) && (array.length != 0)) {
            CharSequence text = dialog_item_name.getText();
            if (text instanceof Spannable) {
                StyleSpan[] spans = ((Spannable) text).getSpans(0, text.length(), StyleSpan.class);
                if (spans.length > 0) {
                    ((Spannable) text).setSpan(spans[0], array[0], array[1], Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
    }
}
