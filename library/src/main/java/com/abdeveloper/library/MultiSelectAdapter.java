package com.abdeveloper.library;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

class MultiSelectAdapter extends ListAdapter<MultiSelectModel, MultiSelectViewHolder> {

    private static final DiffUtil.ItemCallback<MultiSelectModel> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<MultiSelectModel>() {

                private static final int TEXT = 0;

                @Override
                public boolean areItemsTheSame(MultiSelectModel oldItem, MultiSelectModel newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(MultiSelectModel oldItem, MultiSelectModel newItem) {
                    return (oldItem.getName().equals(newItem.getName()));
                }

                public Object getChangePayload(@NonNull MultiSelectModel oldItem, @NonNull MultiSelectModel newItem) {
                    return TEXT;
                }
            };
    private final MultiSelectViewHolder.SelectionCallbackListener mListener;

    MultiSelectAdapter(MultiSelectViewHolder.SelectionCallbackListener listener) {
        super(DIFF_CALLBACK);
        mListener = listener;
    }

    @SuppressWarnings("OverlyLongMethod")
    @NonNull
    @Override
    public MultiSelectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        /*
        View view = LayoutInflater.from(context).inflate(R.layout.multi_select_item, parent, false);
        ImageView imageView = view.findViewById(R.id.dialog_item_icon);
        TextView textView = view.findViewById(R.id.dialog_item_name);
        CheckBox checkBox = view.findViewById(R.id.dialog_item_checkbox);

        return new MultiSelectViewHolder(view, imageView, textView, checkBox);
        */

        Resources resources = context.getResources();

        TypedValue typedValue = new TypedValue();
        Resources.Theme contextTheme = context.getTheme();
        contextTheme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);

        LinearLayout root = new LinearLayout(context);
        LinearLayout.LayoutParams rootParams =
                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rootParams.width = LayoutParams.MATCH_PARENT;
        rootParams.height = resources.getDimensionPixelSize(R.dimen.main_container_height);
        root.setBackgroundResource(typedValue.resourceId);
        root.setOrientation(LinearLayout.HORIZONTAL);
        root.setLayoutParams(rootParams);

        ImageView imageView = new ImageView(context);
        LinearLayout.LayoutParams imageViewParams =
                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        imageViewParams.width = resources.getDimensionPixelSize(R.dimen.dialog_item_icon_size);
        imageViewParams.height = resources.getDimensionPixelSize(R.dimen.dialog_item_icon_size);
        imageViewParams.gravity = Gravity.CENTER_VERTICAL;
        imageViewParams.leftMargin = resources.getDimensionPixelSize(R.dimen.dialog_item_icon_left_margin);
        imageViewParams.rightMargin = resources.getDimensionPixelSize(R.dimen.dialog_item_icon_right_margin);
        imageView.setBackgroundResource(R.drawable.icon_background_material);
        imageView.setClickable(false);
        imageView.setScaleType(ScaleType.CENTER_CROP);
        root.addView(imageView);
        imageView.setLayoutParams(imageViewParams);

        TextView textView = new TextView(context);
        LinearLayout.LayoutParams textViewParams =
                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textViewParams.width = 0;
        textViewParams.gravity = Gravity.CENTER_VERTICAL;
        textViewParams.weight = 1.0F;
        textView.setEllipsize(TruncateAt.END);
        textView.setIncludeFontPadding(false);
        textView.setMaxLines(1);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.font_size_subheading));
        root.addView(textView);
        textView.setLayoutParams(textViewParams);

        CheckBox checkBox = new CheckBox(context);
        LinearLayout.LayoutParams checkBoxParams =
                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        checkBoxParams.gravity = Gravity.CENTER_VERTICAL;
        checkBoxParams.leftMargin = resources.getDimensionPixelSize(R.dimen.dialog_item_icon_right_margin);
        checkBoxParams.rightMargin = resources.getDimensionPixelSize(R.dimen.dialog_item_icon_left_margin);
        checkBox.setClickable(false);
        root.addView(checkBox);
        checkBox.setLayoutParams(checkBoxParams);

        return new MultiSelectViewHolder(root, imageView, textView, checkBox);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiSelectViewHolder holder, int position) {
        holder.bind(getItem(position), mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiSelectViewHolder holder, int position,
                                 @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            holder.bind(getItem(position), mListener, payloads);
        }
    }

    @Override
    public long getItemId(int position) {
        MultiSelectModel model = getItem(position);
        return model.getId();
    }
}
