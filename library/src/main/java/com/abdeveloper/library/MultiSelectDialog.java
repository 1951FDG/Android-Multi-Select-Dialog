package com.abdeveloper.library;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

@SuppressWarnings("ReturnOfThis")
public class MultiSelectDialog extends AppCompatDialogFragment
        implements Filterable, ViewTreeObserver.OnGlobalLayoutListener, SearchView.OnQueryTextListener, View.OnClickListener,
        MultiSelectViewHolder.SelectionCallbackListener {

    // Default Values
    private String mHint = "";

    private int mMaxRecycledViews = Integer.MAX_VALUE;

    private int mMaxSelectionLimit;

    private String mMaxSelectionMessage = "";

    private int mMinSelectionLimit = 1;

    private String mMinSelectionMessage = "";

    private MultiSelectAdapter mMultiSelectAdapter;

    private MultiSelectFilter mMultiSelectFilter;

    private Collection<MultiSelectable> mMultiSelectItems;

    private String mNegativeText = "";

    private String mPositiveText = "";

    private Collection<Integer> mPostSelectedIds = Collections.emptyList();

    private Collection<Integer> mPreSelectedIds = Collections.emptyList();

    private int mRecyclerViewMinHeight;

    private SubmitCallbackListener mSubmitCallbackListener;

    private String mTitle = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mRecyclerViewMinHeight == 0) {
            if (mTitle.isEmpty()) {
                setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FullScreenDialogStyle);
            } else {
                setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
            }
            mMultiSelectAdapter = new MultiSelectAdapter(this);
            mMultiSelectAdapter.setHasStableIds(true);
            mMultiSelectAdapter.submitList(getList());
        }
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Dialog dialog = requireDialog();
        if (!mTitle.isEmpty()) {
            dialog.setTitle(mTitle);
        }
        return inflater.inflate(R.layout.multi_select_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MultiSelectRecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(mMultiSelectAdapter);
        recyclerView.setEmptyView(view.findViewById(R.id.stub));
        recyclerView.setHasFixedSize(true);
        if (mRecyclerViewMinHeight == 0) {
            ViewTreeObserver observer = recyclerView.getViewTreeObserver();
            observer.addOnGlobalLayoutListener(this);
        } else {
            recyclerView.setMinimumHeight(mRecyclerViewMinHeight);
        }
        RecycledViewPool recycledViewPool = recyclerView.getRecycledViewPool();
        recycledViewPool.setMaxRecycledViews(R.layout.multi_select_item, mMaxRecycledViews);
        SearchView searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(this);
        searchView.onActionViewExpanded();
        searchView.clearFocus();
        TextView dialogSubmit = view.findViewById(R.id.done);
        dialogSubmit.setOnClickListener(this);
        TextView dialogCancel = view.findViewById(R.id.cancel);
        dialogCancel.setOnClickListener(this);
        if (!mHint.isEmpty()) {
            searchView.setQueryHint(mHint);
        }
        if (!mPositiveText.isEmpty()) {
            dialogSubmit.setText(mPositiveText);
        }
        if (!mNegativeText.isEmpty()) {
            dialogCancel.setText(mNegativeText);
        }
    }

    @Override
    public void onStart() {
        mPostSelectedIds = Collections.checkedSortedSet(new TreeSet<>(mPreSelectedIds), Integer.class);
        if (BuildConfig.DEBUG && !mPostSelectedIds.equals(mPreSelectedIds)) {
            throw new AssertionError(String.format("expected same:<%s> was not:<%s>", mPreSelectedIds, mPostSelectedIds));
        }
        super.onStart();
    }

    @Override
    public boolean addToSelection(@NonNull Integer id) {
        return mPostSelectedIds.add(id);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (mMultiSelectFilter == null) {
            mMultiSelectFilter = new MultiSelectFilter();
        }
        return mMultiSelectFilter;
    }

    @Override
    public boolean isSelected(@NonNull Integer id) {
        return mPostSelectedIds.contains(id);
    }

    @Override
    public void onClick(@NonNull View v) {
        if (v.getId() == R.id.done) {
            Resources resources = getResources();
            int size = mPostSelectedIds.size();
            if (size >= mMinSelectionLimit) {
                if (size <= mMaxSelectionLimit) {
                    mPreSelectedIds = Collections.checkedSortedSet(new TreeSet<>(mPostSelectedIds), Integer.class);
                    if (mSubmitCallbackListener != null) {
                        ArrayList<Integer> selectedIds = new ArrayList<>(mPostSelectedIds);
                        ArrayList<String> selectedNames = getSelectNameList(getMultiSelectItems());
                        String dataString = getSelectedDataString(getMultiSelectItems());
                        mSubmitCallbackListener.onSelected(selectedIds, selectedNames, dataString);
                    }
                    dismiss();
                } else {
                    String youCan = resources.getString(R.string.you_can_only_select_up_to);
                    showMessage(resources, youCan, mMaxSelectionMessage, mMaxSelectionLimit);
                }
            } else {
                String pleaseSelect = resources.getString(R.string.please_select_at_least);
                showMessage(resources, pleaseSelect, mMinSelectionMessage, mMinSelectionLimit);
            }
        }
        if (v.getId() == R.id.cancel) {
            if (mSubmitCallbackListener != null) {
                mSubmitCallbackListener.onCancel();
            }
            dismiss();
        }
    }

    @Override
    public void onGlobalLayout() {
        Dialog dialog = requireDialog();
        View view = dialog.findViewById(R.id.recycler_view);
        if (view == null) {
            throw new IllegalArgumentException("ID does not reference a View inside this Dialog");
        }
        mRecyclerViewMinHeight = view.getHeight();
        view.setMinimumHeight(mRecyclerViewMinHeight);
        ViewTreeObserver observer = view.getViewTreeObserver();
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
        return mPostSelectedIds.remove(id);
    }

    @Nullable
    public MultiSelectAdapter getAdapter() {
        return mMultiSelectAdapter;
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
        return (mMultiSelectItems != null) ? mMultiSelectItems : Collections.unmodifiableCollection(new ArrayList<MultiSelectable>(0));
    }

    @NonNull
    public MultiSelectDialog setHint(@NonNull String str) {
        mHint = str;
        return this;
    }

    @NonNull
    public MultiSelectDialog setMaxRecycledViews(int max) {
        mMaxRecycledViews = max;
        return this;
    }

    @NonNull
    public MultiSelectDialog setMaxSelectionLimit(int limit) {
        mMaxSelectionLimit = limit;
        return this;
    }

    @NonNull
    public MultiSelectDialog setMaxSelectionMessage(@NonNull String message) {
        mMaxSelectionMessage = message;
        return this;
    }

    @NonNull
    public MultiSelectDialog setMinSelectionLimit(int limit) {
        mMinSelectionLimit = limit;
        return this;
    }

    @NonNull
    public MultiSelectDialog setMinSelectionMessage(@NonNull String message) {
        mMinSelectionMessage = message;
        return this;
    }

    @NonNull
    public MultiSelectDialog setMultiSelectList(@NonNull Collection<MultiSelectable> list) {
        mMultiSelectItems = Collections.unmodifiableCollection(new ArrayList<>(list));
        if (mMaxSelectionLimit == 0) {
            mMaxSelectionLimit = list.size();
        }
        return this;
    }

    @NonNull
    public MultiSelectDialog setNegativeText(@NonNull String message) {
        mNegativeText = message;
        return this;
    }

    @NonNull
    public MultiSelectDialog setPositiveText(@NonNull String message) {
        mPositiveText = message;
        return this;
    }

    @NonNull
    public MultiSelectDialog setPreSelectIDsList(@NonNull Collection<Integer> list) {
        mPreSelectedIds = Collections.checkedSortedSet(new TreeSet<>(list), Integer.class);
        return this;
    }

    @NonNull
    public MultiSelectDialog setSubmitListener(@Nullable SubmitCallbackListener callback) {
        mSubmitCallbackListener = callback;
        return this;
    }

    @NonNull
    public MultiSelectDialog setTitle(@NonNull String str) {
        mTitle = str;
        return this;
    }

    private ArrayList<String> getSelectNameList(Collection<MultiSelectable> list) {
        ArrayList<String> names = new ArrayList<>(list.size());
        for (MultiSelectable model : list) {
            int id = model.getId();
            if (mPostSelectedIds.contains(id)) {
                CharSequence name = model.getName();
                String str = name.toString();
                names.add(str);
            }
        }
        return names;
    }

    private String getSelectedDataString(Collection<MultiSelectable> list) {
        StringBuilder data = new StringBuilder(256);
        for (MultiSelectable model : list) {
            int id = model.getId();
            if (mPostSelectedIds.contains(id)) {
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
            //noinspection HardCodedStringLiteral
            message = String.format("%s %d %s", s, selectionLimit, (selectionLimit > 1) ? options : option);
        }
        Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
        toast.show();
    }

    public interface SubmitCallbackListener {

        void onCancel();

        void onSelected(@NonNull ArrayList<Integer> selectedIds, @NonNull ArrayList<String> selectedNames, @NonNull String dataString);
    }

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

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            MultiSelectAdapter adapter = getAdapter();
            if (adapter != null) {
                adapter.submitList((List<MultiSelectable>) results.values);
            }
        }
    }
}
