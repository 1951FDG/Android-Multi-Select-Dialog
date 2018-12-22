package com.abdeveloper.library;

import android.os.Build;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class MultiSelectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final ImageView dialog_item_icon;
    private final TextView dialog_item_name;
    private final CheckBox dialog_item_checkbox;
    private SelectionCallbackListener itemViewClickListener;

    MultiSelectViewHolder(@NonNull View view, ImageView v1, TextView v2, CheckBox v3) {
        super(view);
        dialog_item_icon = v1;
        dialog_item_name = v2;
        dialog_item_checkbox = v3;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog_item_icon.setClipToOutline(true);
        }
    }

    void bind(MultiSelectModel model, SelectionCallbackListener listener) {
        if (!itemView.hasOnClickListeners()) {
            itemView.setOnClickListener(this);
        }

        dialog_item_icon.setImageResource(model.getImageResource());

        dialog_item_name.setText(model.getName());

        if (listener != null) {
            boolean checked = listener.isSelected(model.getId());
            dialog_item_checkbox.setChecked(checked);
        }

        itemViewClickListener = listener;
    }

    void bind(MultiSelectModel model, SelectionCallbackListener listener, List<Object> payloads) {
        for (Object data : payloads) {
            if ((int) data == 0) {
                dialog_item_name.setText(model.getName());
            }
        }
    }

    @Override
    public void onClick(View v) {
        Integer id = (int) getItemId();
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

    @SuppressWarnings("UnusedReturnValue")
    interface SelectionCallbackListener {
        boolean removeFromSelection(@NonNull Integer id);

        boolean isSelected(@NonNull Integer id);

        boolean addToSelection(@NonNull Integer id);
    }
}
