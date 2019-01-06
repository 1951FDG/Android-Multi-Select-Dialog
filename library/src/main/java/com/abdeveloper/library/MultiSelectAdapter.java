package com.abdeveloper.library;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
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
        // return getViewHolder(context);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiSelectViewHolder holder, int position) {
        holder.bind(getItem(position), mListener);
    }

    @Override
    public void onBindViewHolder(
            @NonNull MultiSelectViewHolder holder, int position, @NonNull List<Object> payloads) {
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
        CompoundButton checkBox = root.findViewById(R.id.dialog_item_checkbox);

        TypedValue outValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.colorAccent, outValue, true);

        return new MultiSelectViewHolder(root, imageView, textView, checkBox, outValue.data);
    }

    private MultiSelectViewHolder getViewHolder(@NonNull Context context) {
        Resources resources = context.getResources();
        Resources.Theme theme = context.getTheme();

        int[] attrs;
        attrs = new int[]{
                        R.attr.listPreferredItemHeight,
                        R.attr.listPreferredItemPaddingLeft,
                        R.attr.listPreferredItemPaddingRight,
                        R.attr.selectableItemBackground,
                        R.attr.textAppearanceListItem,
                        R.attr.colorAccent
                };

        TypedArray array = theme.obtainStyledAttributes(attrs);

        int listPreferredItemHeight = array.getDimensionPixelSize(0, 0);
        int listPreferredItemPaddingLeft = array.getDimensionPixelSize(1, 0);
        int listPreferredItemPaddingRight = array.getDimensionPixelSize(2, 0);
        int selectableItemBackground = array.getResourceId(3, 0);
        int textAppearanceListItem = array.getResourceId(4, 0);
        int colorAccent = array.getColor(5, 0);

        LinearLayout root = new LinearLayout(context);
        LinearLayout.LayoutParams rootParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rootParams.width = LayoutParams.MATCH_PARENT;
        rootParams.height = listPreferredItemHeight;
        root.setBackgroundResource(selectableItemBackground);
        root.setMinimumHeight(resources.getDimensionPixelSize(R.dimen.dialog_item_min_height));
        root.setOrientation(LinearLayout.HORIZONTAL);
        root.setPadding(listPreferredItemPaddingLeft, 0, listPreferredItemPaddingRight, 0);
        root.setLayoutParams(rootParams);

        ImageView imageView = new ImageView(context);
        LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        imageViewParams.width = resources.getDimensionPixelSize(R.dimen.dialog_item_image_size);
        imageViewParams.height = resources.getDimensionPixelSize(R.dimen.dialog_item_image_size);
        imageViewParams.gravity = Gravity.CENTER_VERTICAL;
        imageViewParams.leftMargin = resources.getDimensionPixelSize(R.dimen.dialog_item_left_margin);
        imageView.setBackgroundResource(R.drawable.icon_background_material);
        imageView.setScaleType(ScaleType.CENTER_CROP);
        root.addView(imageView);
        imageView.setLayoutParams(imageViewParams);

        TextView textView = new TextView(context);
        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textViewParams.width = 0;
        textViewParams.gravity = Gravity.CENTER_VERTICAL;
        textViewParams.leftMargin = resources.getDimensionPixelSize(R.dimen.dialog_item_left_margin);
        textViewParams.weight = 1.0F;
        textView.setEllipsize(TruncateAt.END);
        textView.setIncludeFontPadding(false);
        textView.setMaxLines(1);
        textView.setTextAppearance(textView.getContext(), textAppearanceListItem);
        root.addView(textView);
        textView.setLayoutParams(textViewParams);

        CompoundButton checkBox = new MultiSelectCompoundButton(context);
        LinearLayout.LayoutParams checkBoxParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        checkBoxParams.width = resources.getDimensionPixelSize(R.dimen.dialog_item_button_size);
        checkBoxParams.height = resources.getDimensionPixelSize(R.dimen.dialog_item_button_size);
        checkBoxParams.gravity = Gravity.CENTER_VERTICAL;
        checkBoxParams.leftMargin = resources.getDimensionPixelSize(R.dimen.layout_margin);
        checkBoxParams.rightMargin = resources.getDimensionPixelSize(R.dimen.layout_margin);
        checkBox.setBackgroundResource(R.drawable.btn_check_material);
        checkBox.setClickable(false);
        root.addView(checkBox);
        checkBox.setLayoutParams(checkBoxParams);

        array.recycle();

        return new MultiSelectViewHolder(root, imageView, textView, checkBox, colorAccent);
    }
}
