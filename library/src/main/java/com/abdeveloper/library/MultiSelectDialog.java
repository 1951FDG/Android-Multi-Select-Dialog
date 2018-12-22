package com.abdeveloper.library;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

public class MultiSelectDialog extends AppCompatDialogFragment implements ViewTreeObserver.OnGlobalLayoutListener, SearchView.OnQueryTextListener, View.OnClickListener, MultiSelectViewHolder.SelectionCallbackListener {
    private final ArrayList<MultiSelectModel> mainListOfAdapter = new ArrayList<>();
    private final ArrayList<Integer> preSelectedIdsList = new ArrayList<>();
    private final ArrayList<Integer> postSelectedIdsList = new ArrayList<>();
    private MultiSelectAdapter multiSelectAdapter;
    // Default Values
    private String hint = "";
    private String title = "";
    private String positiveText = "";
    private String negativeText = "";
    private SubmitCallbackListener submitCallbackListener;

    private int minSelectionLimit = 1;
    private String minSelectionMessage = "";
    private int maxSelectionLimit;
    private String maxSelectionMessage = "";

    private int recyclerViewMinHeight;

    @ColorInt
    private int color;

    @SuppressWarnings("ObjectAllocationInLoop")
    private static ArrayList<MultiSelectModel> filter(ArrayList<MultiSelectModel> list, String query, int color) {
        int queryLength = query.length();
        ArrayList<MultiSelectModel> filteredModelList = new ArrayList<>();

        for (MultiSelectModel model : list) {
            SpannableString name = model.getName();
            String text = name.toString();
            String lowerCaseText = text.toLowerCase();
            int queryStart = lowerCaseText.indexOf(query, 0);
            int queryEnd = queryStart + queryLength;
            if (queryStart > -1) {
                if (queryLength > 1) {
                    int uniqueId = model.getId();
                    int resId = model.getImageResource();
                    int flags = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
                    SpannableString spannableString = new SpannableString(text);
                    spannableString.setSpan(colorSpan, queryStart, queryEnd, flags);

                    filteredModelList.add(new MultiSelectModel(uniqueId, spannableString, resId));
                } else {
                    filteredModelList.add(model);
                }
            }
        }

        return filteredModelList;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (recyclerViewMinHeight == 0) {
            if (title.isEmpty()) {
                setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FullScreenDialogStyle);

            } else {
                setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
            }

            multiSelectAdapter = new MultiSelectAdapter(this);
            multiSelectAdapter.setHasStableIds(true);
            multiSelectAdapter.submitList(mainListOfAdapter);

            if (color == 0) {
                Context context = getContext();
                if (context == null)
                    throw new NullPointerException();

                color = ContextCompat.getColor(context, R.color.colorAccent);
            }
        }
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.custom_multi_select, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerViewEmptySupport recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(multiSelectAdapter);
        recyclerView.setEmptyView(view.findViewById(R.id.list_empty1));
        recyclerView.setHasFixedSize(true);

        if (recyclerViewMinHeight == 0) {
            ViewTreeObserver observer = recyclerView.getViewTreeObserver();
            observer.addOnGlobalLayoutListener(this);
        } else {
            recyclerView.setMinimumHeight(recyclerViewMinHeight);
        }

        SearchView searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(this);
        searchView.onActionViewExpanded();
        searchView.clearFocus();

        TextView dialogSubmit = view.findViewById(R.id.done);
        dialogSubmit.setOnClickListener(this);

        TextView dialogCancel = view.findViewById(R.id.cancel);
        dialogCancel.setOnClickListener(this);

        if (!hint.isEmpty()) searchView.setQueryHint(hint);
        if (!positiveText.isEmpty()) dialogSubmit.setText(positiveText);
        if (!negativeText.isEmpty()) dialogCancel.setText(negativeText);
    }

    @Override
    public void onStart() {
        Dialog dialog = getDialog();

        Window window = dialog.getWindow();
        if (window == null)
            throw new NullPointerException();

        if (!title.isEmpty()) {
            dialog.setTitle(title);
        }

        WindowManager.LayoutParams attrs = window.getAttributes();
        attrs.width = LayoutParams.MATCH_PARENT;
        window.setAttributes(attrs);

        super.onStart();
    }

    @NonNull
    public MultiSelectDialog hint(@NonNull String str) {
        hint = str;
        return this;
    }

    @NonNull
    public MultiSelectDialog title(@NonNull String str) {
        title = str;
        return this;
    }

    @NonNull
    public MultiSelectDialog positiveText(@NonNull String message) {
        positiveText = message;
        return this;
    }

    @NonNull
    public MultiSelectDialog negativeText(@NonNull String message) {
        negativeText = message;
        return this;
    }

    @NonNull
    public MultiSelectDialog preSelectIDsList(@NonNull ArrayList<Integer> list) {
        preSelectedIdsList.clear();
        preSelectedIdsList.addAll(list);

        postSelectedIdsList.clear();
        postSelectedIdsList.addAll(list);
        return this;
    }

    @NonNull
    public MultiSelectDialog multiSelectList(@NonNull ArrayList<MultiSelectModel> list) {
        mainListOfAdapter.clear();
        mainListOfAdapter.addAll(list);

        if (maxSelectionLimit == 0)
            maxSelectionLimit = list.size();
        return this;
    }

    @NonNull
    public MultiSelectDialog setMaxSelectionLimit(int limit) {
        maxSelectionLimit = limit;
        return this;
    }

    @NonNull
    public MultiSelectDialog setMaxSelectionMessage(@NonNull String message) {
        maxSelectionMessage = message;
        return this;
    }

    @NonNull
    public MultiSelectDialog setMinSelectionLimit(int limit) {
        minSelectionLimit = limit;
        return this;
    }

    @NonNull
    public MultiSelectDialog setMinSelectionMessage(@NonNull String message) {
        minSelectionMessage = message;
        return this;
    }

    @NonNull
    public MultiSelectDialog onSubmit(@Nullable SubmitCallbackListener callback) {
        submitCallbackListener = callback;
        return this;
    }

    private void showMessage(Resources resources, String s, String selectionMessage, int selectionLimit) {
        String options = resources.getString(R.string.options);
        String option = resources.getString(R.string.option);
        String message;

        if (selectionMessage.isEmpty()) {
            if (selectionLimit > 1) {
                message = s + ' ' + selectionLimit + ' ' + options;
            } else {
                message = s + ' ' + selectionLimit + ' ' + option;
            }
        } else {
            message = selectionMessage;
        }

        Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
        toast.show();
    }

    private String getSelectedDataString(ArrayList<MultiSelectModel> list) {
        StringBuilder data = new StringBuilder();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            MultiSelectModel model = list.get(i);
            if (postSelectedIdsList.contains(model.getId())) {
                data.append(", ");
                SpannableString name = model.getName();
                String str = name.toString();
                data.append(str);
            }
        }
        return (data.length() > 0) ? data.substring(1) : "";
    }

    private ArrayList<String> getSelectNameList(ArrayList<MultiSelectModel> list) {
        ArrayList<String> names = new ArrayList<>();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            MultiSelectModel model = list.get(i);
            if (postSelectedIdsList.contains(model.getId())) {
                SpannableString name = model.getName();
                String str = name.toString();
                names.add(str);
            }
        }
        return names;
    }

    @Override
    public void onGlobalLayout() {
        Dialog dialog = getDialog();

        Window window = dialog.getWindow();
        if (window == null)
            throw new NullPointerException();

        RecyclerViewEmptySupport recyclerView = window.findViewById(R.id.recycler_view);
        if (recyclerView == null)
            throw new NullPointerException();

        recyclerViewMinHeight = recyclerView.getHeight();
        recyclerView.setMinimumHeight(recyclerViewMinHeight);

        ViewTreeObserver observer = recyclerView.getViewTreeObserver();
        observer.removeOnGlobalLayoutListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(@NonNull String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(@NonNull String newText) {
        if (isVisible()) {
            ArrayList<MultiSelectModel> filteredList = mainListOfAdapter;
            if (!newText.isEmpty()) {
                String lowerCaseQuery = newText.toLowerCase();
                filteredList = filter(filteredList, lowerCaseQuery, color);
            }

            multiSelectAdapter.submitList(filteredList);
        }

        return true;
    }

    @Override
    public void onClick(@NonNull View v) {
        ArrayList<Integer> selectedIdsList = postSelectedIdsList;

        if (v.getId() == R.id.done) {
            Resources resources = getResources();
            if (selectedIdsList.size() >= minSelectionLimit) {
                if (selectedIdsList.size() <= maxSelectionLimit) {
                    // to remember last selected ids which were successfully done
                    preSelectedIdsList.clear();
                    preSelectedIdsList.addAll(selectedIdsList);

                    if (submitCallbackListener != null) {
                        ArrayList<String> selectedNames = getSelectNameList(mainListOfAdapter);
                        String dataString = getSelectedDataString(mainListOfAdapter);
                        submitCallbackListener.onSelected(selectedIdsList, selectedNames, dataString);
                    }

                    dismiss();
                } else {
                    String youCan = resources.getString(R.string.you_can_only_select_up_to);
                    showMessage(resources, youCan, maxSelectionMessage, maxSelectionLimit);
                }
            } else {
                String pleaseSelect = resources.getString(R.string.please_select_at_least);
                showMessage(resources, pleaseSelect, minSelectionMessage, minSelectionLimit);
            }
        }

        if (v.getId() == R.id.cancel) {
            if (submitCallbackListener != null) {
                selectedIdsList.clear();
                selectedIdsList.addAll(preSelectedIdsList);
                submitCallbackListener.onCancel();
            }

            dismiss();
        }
    }

    @Override
    public boolean isSelected(@NonNull Integer id) {
        return postSelectedIdsList.contains(id);
    }

    @Override
    public boolean addToSelection(@NonNull Integer id) {
        return postSelectedIdsList.add(id);
    }

    @Override
    public boolean removeFromSelection(@NonNull Integer id) {
        return postSelectedIdsList.remove(id);
    }

    public interface SubmitCallbackListener {
        void onSelected(@NonNull ArrayList<Integer> selectedIds, @NonNull ArrayList<String> selectedNames, @NonNull String dataString);

        void onCancel();
    }
}
