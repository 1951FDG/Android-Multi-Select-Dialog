package com.abdeveloper.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class MultiSelectRecyclerView extends RecyclerView {

    private View emptyView;

    @SuppressWarnings("ConstantConditions")
    private final AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            updateEmptyStatus(getAdapter().getItemCount() == 0);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            updateEmptyStatus(getAdapter().getItemCount() == 0);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            updateEmptyStatus(getAdapter().getItemCount() == 0);
        }
    };

    public MultiSelectRecyclerView(@NonNull Context context) {
        super(context);
    }

    public MultiSelectRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiSelectRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Nullable
    public View getEmptyView() {
        return emptyView;
    }

    public void setEmptyView(@Nullable View view) {
        emptyView = view;
        Adapter adapter = getAdapter();
        boolean empty = (adapter == null) || (adapter.getItemCount() == 0);
        updateEmptyStatus(empty);
    }

    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }
    }

    public void updateEmptyStatus(boolean empty) {
        if (empty) {
            if (emptyView != null) {
                emptyView.setVisibility(View.VISIBLE);
                setVisibility(View.INVISIBLE);
            } else {
                setVisibility(View.VISIBLE);
            }
        } else {
            if (emptyView != null) {
                emptyView.setVisibility(View.GONE);
            }
            setVisibility(View.VISIBLE);
        }
    }
}
