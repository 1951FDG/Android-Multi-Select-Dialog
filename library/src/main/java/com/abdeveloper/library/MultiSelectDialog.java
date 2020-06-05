package com.abdeveloper.library;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.PluralsRes;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

@SuppressWarnings("ReturnOfThis")
public class MultiSelectDialog extends AppCompatDialogFragment
        implements DialogInterface.OnClickListener, SearchView.OnQueryTextListener, MultiSelectViewHolder.SelectionCallbackListener,
        Filterable {

    // Default Values
    private String mHint = "";

    // Use this instance of the interface to deliver action events
    @Nullable
    private SubmitCallbackListener mListener = null;

    private int mMaxRecycledViews = Integer.MAX_VALUE;

    private int mMaxSelectionLimit;

    private String mMaxSelectionMessage = "";

    private int mMinSelectionLimit = 1;

    private String mMinSelectionMessage = "";

    private MultiSelectAdapter mMultiSelectAdapter;

    private MultiSelectFilter mMultiSelectFilter;

    private Collection<MultiSelectable> mMultiSelectItems;

    @StringRes
    private int mNegativeText = android.R.string.cancel;

    @StringRes
    private int mPositiveText = android.R.string.ok;

    private Collection<Integer> mPostSelectedIds = Collections.emptyList();

    private Collection<Integer> mPreSelectedIds = Collections.emptyList();

    private String mTitle = "";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            mListener = (SubmitCallbackListener) context;
        } catch (ClassCastException ignored) {
            throw new ClassCastException(context + " must implement SubmitCallbackListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mTitle.isEmpty()) {
            setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FullScreenDialogStyle);
        } else {
            setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
        }

        mMultiSelectAdapter = new MultiSelectAdapter(this);
        mMultiSelectAdapter.setHasStableIds(true);
        mMultiSelectAdapter.submitList(getList());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), getTheme());
        if (!mTitle.isEmpty()) {
            builder.setTitle(mTitle);
        }
        LayoutInflater inflater = LayoutInflater.from(builder.getContext());
        View view = inflater.inflate(R.layout.multi_select_dialog, null, false);
        onViewCreated(view, null);
        builder.setView(view);
        builder.setPositiveButton(mPositiveText, this);
        builder.setNegativeButton(mNegativeText, this);
        return builder.create();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MultiSelectRecyclerView recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setAdapter(mMultiSelectAdapter);
        recyclerView.setEmptyView(view.findViewById(R.id.stub));
        recyclerView.setHasFixedSize(true);
        RecycledViewPool recycledViewPool = recyclerView.getRecycledViewPool();
        recycledViewPool.setMaxRecycledViews(R.layout.multi_select_item, mMaxRecycledViews);
        SearchView searchView = view.findViewById(R.id.search);
        searchView.setOnQueryTextListener(this);
        searchView.onActionViewExpanded();
        searchView.clearFocus();
        if (!mHint.isEmpty()) {
            searchView.setQueryHint(mHint);
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
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    @Override
    public void onClick(@Nullable DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            Resources res = getResources();
            int size = mPostSelectedIds.size();
            if (size >= mMinSelectionLimit) {
                if (size <= mMaxSelectionLimit) {
                    mPreSelectedIds = Collections.checkedSortedSet(new TreeSet<>(mPostSelectedIds), Integer.class);
                    if (mListener != null) {
                        ArrayList<Integer> selectedIds = new ArrayList<>(mPostSelectedIds);
                        ArrayList<String> selectedNames = getSelectNameList(getMultiSelectItems());
                        String dataString = getSelectedDataString(getMultiSelectItems());
                        mListener.onSelected(selectedIds, selectedNames, dataString);
                    }
                } else {
                    showMessage(res, R.plurals.max_selection_message, mMaxSelectionMessage, mMaxSelectionLimit);
                }
            } else {
                showMessage(res, R.plurals.min_selection_message, mMinSelectionMessage, mMinSelectionLimit);
            }
        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
            if (mListener != null) {
                mListener.onCancel();
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(@NonNull String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(@NonNull String newText) {
        String lowerCaseQuery = newText.toLowerCase(Locale.ENGLISH);
        Filter filter = getFilter();
        filter.filter(lowerCaseQuery, null);
        return true;
    }

    @Override
    public boolean addToSelection(@NonNull Integer id) {
        return mPostSelectedIds.add(id);
    }

    @Override
    public boolean isSelected(@NonNull Integer id) {
        return mPostSelectedIds.contains(id);
    }

    @Override
    public boolean removeFromSelection(@NonNull Integer id) {
        return mPostSelectedIds.remove(id);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (mMultiSelectFilter == null) {
            mMultiSelectFilter = new MultiSelectFilter();
        }
        return mMultiSelectFilter;
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
    public MultiSelectDialog setNegativeText(@StringRes int message) {
        mNegativeText = message;
        return this;
    }

    @NonNull
    public MultiSelectDialog setPositiveText(@StringRes int message) {
        mPositiveText = message;
        return this;
    }

    @NonNull
    public MultiSelectDialog setPreSelectIDsList(@NonNull Collection<Integer> list) {
        mPreSelectedIds = Collections.checkedSortedSet(new TreeSet<>(list), Integer.class);
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

    private void showMessage(Resources res, @PluralsRes int id, String selectionMessage, int selectionLimit) {
        String message = selectionMessage;
        if (message.isEmpty()) {
            message = res.getQuantityString(id, selectionLimit, selectionLimit);
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
                    String lowerCaseText = text.toLowerCase(Locale.ENGLISH);
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
