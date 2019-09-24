package com.abdeveloper.library;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

public class MultiSelectDialog extends AppCompatDialogFragment
        implements Filterable, ViewTreeObserver.OnGlobalLayoutListener, SearchView.OnQueryTextListener, View.OnClickListener,
        MultiSelectViewHolder.SelectionCallbackListener {

    protected class MultiSelectFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<MultiSelectable> list;
            int queryLength = constraint.length();
            if (queryLength > 0) {
                Collection<MultiSelectable> collection = getMultiSelectItems();
                list = new ArrayList<>(collection.size());
                for (MultiSelectable model : collection) {
                    CharSequence name = model.getName();
                    String text = name.toString();
                    String lowerCaseText = text.toLowerCase();
                    int queryStart = lowerCaseText.indexOf(constraint.toString());
                    int queryEnd = queryStart + queryLength;
                    if (queryStart > -1) {
                        MultiSelectable clone = model.clone();
                        if (queryLength > 1) {
                            if (clone instanceof Range) {
                                ((Range) clone).setStart(queryStart);
                                ((Range) clone).setEnd(queryEnd);
                            }
                        }
                        list.add(clone);
                    }
                }
            } else {
                list = getList();
            }
            FilterResults results = new FilterResults();
            results.count = list.size();
            results.values = list;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            MultiSelectAdapter adapter = getAdapter();
            if (adapter != null) {
                adapter.submitList((List<MultiSelectable>) results.values);
            }
        }
    }

    public interface SubmitCallbackListener {

        void onCancel();

        void onSelected(@NonNull ArrayList<Integer> selectedIds, @NonNull ArrayList<String> selectedNames, @NonNull String dataString);
    }

    // Default Values
    private String hint = "";

    private int maxSelectionLimit;

    private String maxSelectionMessage = "";

    private int minSelectionLimit = 1;

    private String minSelectionMessage = "";

    private MultiSelectAdapter multiSelectAdapter;

    private MultiSelectFilter multiSelectFilter;

    private Collection<MultiSelectable> multiSelectItems;

    private String negativeText = "";

    private String positiveText = "";

    private Collection<Integer> postSelectedIds;

    private Collection<Integer> preSelectedIds;

    private int recyclerViewMinHeight;

    private SubmitCallbackListener submitCallbackListener;

    private String title = "";

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
            multiSelectAdapter.submitList(getList());
        }
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.multi_select_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MultiSelectRecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(multiSelectAdapter);
        recyclerView.setEmptyView(view.findViewById(R.id.stub));
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
        if (!hint.isEmpty()) {
            searchView.setQueryHint(hint);
        }
        if (!positiveText.isEmpty()) {
            dialogSubmit.setText(positiveText);
        }
        if (!negativeText.isEmpty()) {
            dialogCancel.setText(negativeText);
        }
    }

    @Override
    public void onStart() {
        Dialog dialog = getDialog();
        Window window = dialog.getWindow();
        if (window == null) {
            throw new NullPointerException();
        }
        if (!title.isEmpty()) {
            dialog.setTitle(title);
        }
        WindowManager.LayoutParams attrs = window.getAttributes();
        attrs.width = LayoutParams.MATCH_PARENT;
        window.setAttributes(attrs);
        super.onStart();
    }

    @Override
    public boolean addToSelection(@NonNull Integer id) {
        return postSelectedIds.add(id);
    }

    @Nullable
    public MultiSelectAdapter getAdapter() {
        return multiSelectAdapter;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (multiSelectFilter == null) {
            multiSelectFilter = new MultiSelectFilter();
        }
        return multiSelectFilter;
    }

    @NonNull
    public List<MultiSelectable> getList() {
        Collection<MultiSelectable> collection = getMultiSelectItems();
        List<MultiSelectable> list = new ArrayList<>(collection.size());
        for (MultiSelectable model : collection) {
            MultiSelectable clone = model.clone();
            list.add(clone);
        }
        return list;
    }

    @NonNull
    public Collection<MultiSelectable> getMultiSelectItems() {
        return (multiSelectItems != null) ? multiSelectItems : new ArrayList<MultiSelectable>(0);
    }

    @Override
    public boolean isSelected(@NonNull Integer id) {
        return postSelectedIds.contains(id);
    }

    @Override
    public void onClick(@NonNull View v) {
        if (v.getId() == R.id.done) {
            Resources resources = getResources();
            int size = postSelectedIds.size();
            if (size >= minSelectionLimit) {
                if (size <= maxSelectionLimit) {
                    // to remember last selected ids which were successfully done
                    preSelectedIds = Collections.unmodifiableCollection(postSelectedIds);
                    if (submitCallbackListener != null) {
                        ArrayList<Integer> selectedIds = new ArrayList<>(postSelectedIds);
                        ArrayList<String> selectedNames = getSelectNameList(getMultiSelectItems());
                        String dataString = getSelectedDataString(getMultiSelectItems());
                        submitCallbackListener.onSelected(selectedIds, selectedNames, dataString);
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
                postSelectedIds.clear();
                postSelectedIds.addAll(preSelectedIds);
                submitCallbackListener.onCancel();
            }
            dismiss();
        }
    }

    @Override
    public void onGlobalLayout() {
        Dialog dialog = getDialog();
        Window window = dialog.getWindow();
        if (window == null) {
            throw new NullPointerException();
        }
        MultiSelectRecyclerView recyclerView = window.findViewById(R.id.recycler_view);
        if (recyclerView == null) {
            throw new NullPointerException();
        }
        recyclerViewMinHeight = recyclerView.getHeight();
        recyclerView.setMinimumHeight(recyclerViewMinHeight);
        ViewTreeObserver observer = recyclerView.getViewTreeObserver();
        observer.removeOnGlobalLayoutListener(this);
    }

    @Override
    public boolean onQueryTextChange(@NonNull String newText) {
        if (isVisible()) {
            String lowerCaseQuery = newText.toLowerCase();
            Filter filter = getFilter();
            filter.filter(lowerCaseQuery, null);
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(@NonNull String query) {
        return false;
    }

    @Override
    public boolean removeFromSelection(@NonNull Integer id) {
        return postSelectedIds.remove(id);
    }

    @NonNull
    public MultiSelectDialog setHint(@NonNull String str) {
        hint = str;
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
    public MultiSelectDialog setMultiSelectList(@NonNull Collection<MultiSelectable> list) {
        multiSelectItems = Collections.unmodifiableCollection(list);
        if (maxSelectionLimit == 0) {
            maxSelectionLimit = list.size();
        }
        return this;
    }

    @NonNull
    public MultiSelectDialog setNegativeText(@NonNull String message) {
        negativeText = message;
        return this;
    }

    @NonNull
    public MultiSelectDialog setPositiveText(@NonNull String message) {
        positiveText = message;
        return this;
    }

    @NonNull
    public MultiSelectDialog setPreSelectIDsList(@NonNull Collection<Integer> list) {
        postSelectedIds = Collections.checkedSortedSet(new TreeSet<>(list), Integer.class);
        preSelectedIds = Collections.unmodifiableCollection(postSelectedIds);
        return this;
    }

    @NonNull
    public MultiSelectDialog setSubmitListener(@Nullable SubmitCallbackListener callback) {
        submitCallbackListener = callback;
        return this;
    }

    @NonNull
    public MultiSelectDialog setTitle(@NonNull String str) {
        title = str;
        return this;
    }

    private ArrayList<String> getSelectNameList(Collection<MultiSelectable> list) {
        ArrayList<String> names = new ArrayList<>(list.size());
        for (MultiSelectable model : list) {
            int id = model.getId();
            if (postSelectedIds.contains(id)) {
                CharSequence name = model.getName();
                String str = name.toString();
                names.add(str);
            }
        }
        return names;
    }

    private String getSelectedDataString(Collection<MultiSelectable> list) {
        StringBuilder data = new StringBuilder();
        for (MultiSelectable model : list) {
            int id = model.getId();
            if (postSelectedIds.contains(id)) {
                data.append(", ");
                CharSequence name = model.getName();
                String str = name.toString();
                data.append(str);
            }
        }
        return (data.length() > 0) ? data.substring(1) : "";
    }

    private void showMessage(Resources resources, String s, String selectionMessage, int selectionLimit) {
        String options = resources.getString(R.string.options);
        String option = resources.getString(R.string.option);
        String message = selectionMessage;
        if (message.isEmpty()) {
            if (selectionLimit > 1) {
                message = s + ' ' + selectionLimit + ' ' + options;
            } else {
                message = s + ' ' + selectionLimit + ' ' + option;
            }
        }
        Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
        toast.show();
    }
}
