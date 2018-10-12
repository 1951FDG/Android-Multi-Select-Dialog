package com.abdeveloper.library;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.TypedValue;
import android.view.Gravity;
//import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

class MultiSelectAdapter extends RecyclerView.Adapter<MultiSelectAdapter.MultiSelectDialogViewHolder> {

    private ArrayList<MultiSelectModel> mDataSet;
    private String mSearchQuery = "";
    private final Context mContext;

    MultiSelectAdapter(ArrayList<MultiSelectModel> dataSet, Context context) {
        this.mDataSet = dataSet;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MultiSelectDialogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(context).inflate(R.layout.multi_select_item, parent, false);
        //return new MultiSelectDialogViewHolder(view);

        Context context = parent.getContext();
        Resources resources = context.getResources();

        // Get selectable background
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);

        LinearLayout root = new LinearLayout(context);
        ViewGroup.LayoutParams root_LayoutParams =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        root_LayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        root_LayoutParams.height = resources.getDimensionPixelSize(R.dimen.main_container_height);
        root.setBackgroundResource(typedValue.resourceId);
        root.setOrientation(LinearLayout.HORIZONTAL);
        root.setLayoutParams(root_LayoutParams);

        ImageView dialog_item_icon = new ImageView(context);
        LinearLayout.LayoutParams dialog_item_icon_LayoutParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog_item_icon_LayoutParams.width = resources.getDimensionPixelSize(R.dimen.dialog_item_icon_size);
        dialog_item_icon_LayoutParams.height = resources.getDimensionPixelSize(R.dimen.dialog_item_icon_size);
        dialog_item_icon_LayoutParams.gravity = Gravity.CENTER_VERTICAL;
        dialog_item_icon_LayoutParams.leftMargin = resources.getDimensionPixelSize(R.dimen.dialog_item_icon_left_margin);
        dialog_item_icon_LayoutParams.rightMargin = resources.getDimensionPixelSize(R.dimen.dialog_item_icon_right_margin);
        dialog_item_icon.setBackground(resources.getDrawable(R.drawable.icon_background_material));
        dialog_item_icon.setClickable(false);
        dialog_item_icon.setContentDescription("@string/app_name");
        dialog_item_icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
        root.addView(dialog_item_icon);
        dialog_item_icon.setLayoutParams(dialog_item_icon_LayoutParams);

        LinearLayout linearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams linearLayout_LayoutParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout_LayoutParams.width = 0;
        linearLayout_LayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        linearLayout_LayoutParams.weight = 1;
        linearLayout.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        root.addView(linearLayout);
        linearLayout.setLayoutParams(linearLayout_LayoutParams);

        TextView dialog_item_name = new TextView(context);
        LinearLayout.LayoutParams dialog_item_name_LayoutParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog_item_name_LayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog_item_name.setEllipsize(TextUtils.TruncateAt.END);
        dialog_item_name.setIncludeFontPadding(false);
        dialog_item_name.setMaxLines(1);
        dialog_item_name.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.font_size_subheading));
        linearLayout.addView(dialog_item_name);
        dialog_item_name.setLayoutParams(dialog_item_name_LayoutParams);


        CheckBox dialog_item_checkbox = new CheckBox(context);
        LinearLayout.LayoutParams dialog_item_checkbox_LayoutParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog_item_checkbox_LayoutParams.gravity = Gravity.CENTER_VERTICAL;
        dialog_item_checkbox_LayoutParams.leftMargin = resources.getDimensionPixelSize(R.dimen.dialog_item_icon_right_margin);
        dialog_item_checkbox_LayoutParams.rightMargin = resources.getDimensionPixelSize(R.dimen.dialog_item_icon_left_margin);
        root.addView(dialog_item_checkbox);
        dialog_item_checkbox.setLayoutParams(dialog_item_checkbox_LayoutParams);

        return new MultiSelectDialogViewHolder(root, dialog_item_icon, dialog_item_name, dialog_item_checkbox);
    }

    @Override
    public void onBindViewHolder(@NonNull final MultiSelectDialogViewHolder holder, int position) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.dialog_item_icon.setClipToOutline(true);
        }
        holder.dialog_item_icon.setImageResource(mDataSet.get(position).getImageResource());

        if (!mSearchQuery.equals("") && mSearchQuery.length() > 1) {
            setHighlightedText(position, holder.dialog_name_item);
        } else {
            holder.dialog_name_item.setText(mDataSet.get(position).getName());
        }

        if (mDataSet.get(position).getSelected()) {

            if (!MultiSelectDialog.selectedIdsForCallback.contains(mDataSet.get(position).getId())) {
                MultiSelectDialog.selectedIdsForCallback.add(mDataSet.get(position).getId());
            }
        }

        if (checkForSelection(mDataSet.get(position).getId())) {
            holder.dialog_item_checkbox.setChecked(true);
        } else {
            holder.dialog_item_checkbox.setChecked(false);
        }

        /*holder.dialog_item_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.dialog_item_checkbox.isChecked()) {
                    MultiSelectDialog.selectedIdsForCallback.add(mDataSet.get(holder.getAdapterPosition()).getId());
                    holder.dialog_item_checkbox.setChecked(true);
                } else {
                    removeFromSelection(mDataSet.get(holder.getAdapterPosition()).getId());
                    holder.dialog_item_checkbox.setChecked(false);
                }
            }
        });*/


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!holder.dialog_item_checkbox.isChecked()) {
                    MultiSelectDialog.selectedIdsForCallback.add(mDataSet.get(holder.getAdapterPosition()).getId());
                    holder.dialog_item_checkbox.setChecked(true);
                    mDataSet.get(holder.getAdapterPosition()).setSelected(true);
                    notifyItemChanged(holder.getAdapterPosition());
                } else {
                    removeFromSelection(mDataSet.get(holder.getAdapterPosition()).getId());
                    holder.dialog_item_checkbox.setChecked(false);
                    mDataSet.get(holder.getAdapterPosition()).setSelected(false);
                    notifyItemChanged(holder.getAdapterPosition());
                }
            }
        });
    }

    private void setHighlightedText(int position, TextView textview) {
        String name = mDataSet.get(position).getName();
        SpannableString str = new SpannableString(name);
        int endLength = name.toLowerCase().indexOf(mSearchQuery) + mSearchQuery.length();
        ColorStateList highlightedColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{ContextCompat.getColor(mContext, R.color.colorAccent)});
        TextAppearanceSpan textAppearanceSpan = new TextAppearanceSpan(null, Typeface.NORMAL, -1, highlightedColor, null);
        str.setSpan(textAppearanceSpan, name.toLowerCase().indexOf(mSearchQuery), endLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textview.setText(str);
    }

    private void removeFromSelection(Integer id) {
        for (int i = 0; i < MultiSelectDialog.selectedIdsForCallback.size(); i++) {
            if (id.equals(MultiSelectDialog.selectedIdsForCallback.get(i))) {
                MultiSelectDialog.selectedIdsForCallback.remove(i);
            }
        }
    }


    private boolean checkForSelection(Integer id) {
        for (int i = 0; i < MultiSelectDialog.selectedIdsForCallback.size(); i++) {
            if (id.equals(MultiSelectDialog.selectedIdsForCallback.get(i))) {
                return true;
            }
        }
        return false;
    }


    /*//get selected name string separated by coma
    public String getDataString() {
        String data = "";
        for (int i = 0; i < mDataSet.size(); i++) {
            if (checkForSelection(mDataSet.get(i).getId())) {
                data = data + ", " + mDataSet.get(i).getName();
            }
        }
        if (data.length() > 0) {
            return data.substring(1);
        } else {
            return "";
        }
    }

    //get selected name array list
    public ArrayList<String> getSelectedNameList() {
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < mDataSet.size(); i++) {
            if (checkForSelection(mDataSet.get(i).getId())) {
                names.add(mDataSet.get(i).getName());
            }
        }
        //  return names.toArray(new String[names.size()]);
        return names;
    }*/


    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    void setData(ArrayList<MultiSelectModel> data, String query, MultiSelectAdapter multiSelectAdapter) {
        this.mDataSet = data;
        this.mSearchQuery = query;
        multiSelectAdapter.notifyDataSetChanged();
    }

    class MultiSelectDialogViewHolder extends RecyclerView.ViewHolder {
        private final ImageView dialog_item_icon;
        private final TextView dialog_name_item;
        private final CheckBox dialog_item_checkbox;

        MultiSelectDialogViewHolder(@NonNull View view) {
            super(view);
            dialog_item_icon = view.findViewById(R.id.dialog_item_icon);
            dialog_name_item = view.findViewById(R.id.dialog_item_name);
            dialog_item_checkbox = view.findViewById(R.id.dialog_item_checkbox);
        }

        MultiSelectDialogViewHolder(@NonNull View view, ImageView view1, TextView view2, CheckBox view3) {
            super(view);
            this.dialog_item_icon = view1;
            this.dialog_name_item = view2;
            this.dialog_item_checkbox = view3;
        }
    }
}
