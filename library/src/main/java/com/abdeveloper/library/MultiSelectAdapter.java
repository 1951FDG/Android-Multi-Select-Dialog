package com.abdeveloper.library;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.ListAdapter;

class MultiSelectAdapter extends ListAdapter<MultiSelectable, MultiSelectViewHolder> {

    private final MultiSelectViewHolder.SelectionCallbackListener mListener;

    MultiSelectAdapter(MultiSelectViewHolder.SelectionCallbackListener listener) {
        super(new MultiSelectItemCallback());
        mListener = listener;
    }

    @NonNull
    @Override
    public MultiSelectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        return getViewHolder(context, R.layout.multi_select_item, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiSelectViewHolder holder, int position) {
        holder.bind(getItem(position), mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiSelectViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            holder.bind(getItem(position), mListener, payloads);
        }
    }

    @Override
    public long getItemId(int position) {
        MultiSelectable model = getItem(position);
        return model.getId();
    }

    private MultiSelectViewHolder getViewHolder(@NonNull Context context, @LayoutRes int viewHolderResId, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(viewHolderResId, parent, false);
        ImageView imageView = root.findViewById(R.id.dialog_item_icon);
        TextView textView = root.findViewById(R.id.dialog_item_name);
        AppCompatCheckBox checkBox = root.findViewById(R.id.dialog_item_checkbox);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            checkBox.setSupportButtonTintList(
                    AppCompatResources.getColorStateList(checkBox.getContext(), R.color.control_checkable_material));
        }
        TypedValue tv = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.colorAccent, tv, true);
        return new MultiSelectViewHolder(root, imageView, textView, checkBox, tv.data);
    }
}
