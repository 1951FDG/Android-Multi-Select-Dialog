package com.abdeveloper.library;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MultiSelectDialog extends AppCompatDialogFragment implements SearchView.OnQueryTextListener, View.OnClickListener {

    static ArrayList<Integer> selectedIdsForCallback = new ArrayList<>();

    private ArrayList<MultiSelectModel> mainListOfAdapter = new ArrayList<>();
    private MultiSelectAdapter multiSelectAdapter;
    //Default Values
    private CharSequence hint;
    private String title;
    private float titleSize;
    private String positiveText;
    private String negativeText;
    private SearchView searchView;
    private TextView dialogTitle, dialogSubmit, dialogCancel;
    private ArrayList<Integer> previouslySelectedIdsList = new ArrayList<>();


    private ArrayList<Integer> tempPreviouslySelectedIdsList = new ArrayList<>();
    private ArrayList<MultiSelectModel> tempMainListOfAdapter = new ArrayList<>();

    private SubmitCallbackListener submitCallbackListener;

    private int minSelectionLimit = 1;
    private String minSelectionMessage = null;
    private int maxSelectionLimit = 0;
    private String maxSelectionMessage = null;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Dialog dialog = new Dialog(requireActivity());
        Window window = dialog.getWindow();
        if (window == null)
            throw new NullPointerException();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        dialog.setContentView(R.layout.custom_multi_select);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        RecyclerViewEmptySupport recyclerView =  dialog.findViewById(R.id.recycler_view);
        searchView =  dialog.findViewById(R.id.search_view);
        dialogTitle =  dialog.findViewById(R.id.title);
        dialogSubmit =  dialog.findViewById(R.id.done);
        dialogCancel =  dialog.findViewById(R.id.cancel);

        recyclerView.setEmptyView(dialog.findViewById(R.id.list_empty1));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        dialogSubmit.setOnClickListener(this);
        dialogCancel.setOnClickListener(this);

        settingValues();

        mainListOfAdapter = setCheckedIDS(mainListOfAdapter, previouslySelectedIdsList);
        multiSelectAdapter = new MultiSelectAdapter(mainListOfAdapter, getContext());
        recyclerView.setAdapter(multiSelectAdapter);

        searchView.setOnQueryTextListener(this);
        searchView.onActionViewExpanded();
        searchView.clearFocus();

        if (hint != null && hint.length() == 0) {
            searchView.setQueryHint(null);
        }

        if (title != null && title.isEmpty()) {
            LinearLayout parent = (LinearLayout)dialogTitle.getParent();
            parent.setVisibility(View.GONE);
        }

        return dialog;
    }

    public MultiSelectDialog hint(@NonNull CharSequence hint) {
        this.hint = hint;
        return this;
    }

    public MultiSelectDialog title(@NonNull String title) {
        this.title = title;
        return this;
    }

    public MultiSelectDialog titleSize(float titleSize) {
        this.titleSize = titleSize;
        return this;
    }

    public MultiSelectDialog positiveText(@NonNull String message) {
        this.positiveText = message;
        return this;
    }

    public MultiSelectDialog negativeText(@NonNull String message) {
        this.negativeText = message;
        return this;
    }

    public MultiSelectDialog preSelectIDsList(ArrayList<Integer> list) {
        this.previouslySelectedIdsList = list;
        this.tempPreviouslySelectedIdsList = new ArrayList<>(previouslySelectedIdsList);
        return this;
    }

    public MultiSelectDialog multiSelectList(ArrayList<MultiSelectModel> list) {
        this.mainListOfAdapter = list;
        this.tempMainListOfAdapter = new ArrayList<>(mainListOfAdapter);
        if(maxSelectionLimit == 0)
            maxSelectionLimit = list.size();
        return this;
    }
	
    public MultiSelectDialog setMaxSelectionLimit(int limit){
        this.maxSelectionLimit = limit;
        return this;
    }
	
	public MultiSelectDialog setMaxSelectionMessage(String message) {
		this.maxSelectionMessage = message;
		return this;
	}
	
    public MultiSelectDialog setMinSelectionLimit(int limit){
        this.minSelectionLimit = limit;
        return this;
    }
	
	public MultiSelectDialog setMinSelectionMessage(String message) {
		this.minSelectionMessage = message;
		return this;
	}

    public MultiSelectDialog onSubmit(@NonNull SubmitCallbackListener callback) {
        this.submitCallbackListener = callback;
        return this;
    }

    private void settingValues() {
        if (hint != null) searchView.setQueryHint(hint);
        if (title != null) dialogTitle.setText(title);
        if (titleSize != 0.0f) dialogTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleSize);
        if (positiveText != null) dialogSubmit.setText(positiveText);
        if (negativeText != null) dialogCancel.setText(negativeText);
    }

    private ArrayList<MultiSelectModel> setCheckedIDS(ArrayList<MultiSelectModel> multiSelectData, ArrayList<Integer> listOfIdsSelected) {
        for (int i = 0; i < multiSelectData.size(); i++) {
            multiSelectData.get(i).setSelected(false);
            for (int j = 0; j < listOfIdsSelected.size(); j++) {
                if (listOfIdsSelected.get(j) == (multiSelectData.get(i).getId())) {
                    multiSelectData.get(i).setSelected(true);
                }
            }
        }
        return multiSelectData;
    }

    private ArrayList<MultiSelectModel> filter(ArrayList<MultiSelectModel> models, String query) {
        query = query.toLowerCase();
        final ArrayList<MultiSelectModel> filteredModelList = new ArrayList<>();
        if (query.equals("") | query.isEmpty()) {
            filteredModelList.addAll(models);
            return filteredModelList;
        }

        for (MultiSelectModel model : models) {
            final String name = model.getName().toLowerCase();
            if (name.contains(query)) {
                filteredModelList.add(model);
            }
        }


        return filteredModelList;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        selectedIdsForCallback = previouslySelectedIdsList;
        mainListOfAdapter = setCheckedIDS(mainListOfAdapter, selectedIdsForCallback);
        ArrayList<MultiSelectModel> filteredList = filter(mainListOfAdapter, newText);
        multiSelectAdapter.setData(filteredList, newText.toLowerCase(), multiSelectAdapter);
        return false;
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.done) {
            ArrayList<Integer> callBackListOfIds = selectedIdsForCallback;

            if (callBackListOfIds.size() >= minSelectionLimit) {
                if (callBackListOfIds.size() <= maxSelectionLimit) {

                    //to remember last selected ids which were successfully done
                    tempPreviouslySelectedIdsList = new ArrayList<>(callBackListOfIds);

                    if(submitCallbackListener !=null) {
                        submitCallbackListener.onSelected(callBackListOfIds, getSelectNameList(), getSelectedDataString());
                    }
                    dismiss();
                } else {
                    String youCan = getResources().getString(R.string.you_can_only_select_up_to);
                    String options = getResources().getString(R.string.options);
                    String option = getResources().getString(R.string.option);
                    String message;

                    if(this.maxSelectionMessage != null) {
                        message = maxSelectionMessage;
                    }
                    else {
                        if (maxSelectionLimit > 1)
                            message = youCan + " " + maxSelectionLimit + " " + options;
                        else
                            message = youCan + " " + maxSelectionLimit + " " + option;
                    }
                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                }
            } else {
                String pleaseSelect = getResources().getString(R.string.please_select_at_least);
                String options = getResources().getString(R.string.options);
                String option = getResources().getString(R.string.option);
                String message;

                if(this.minSelectionMessage != null) {
                    message = minSelectionMessage;
                }
                else {
                    if (minSelectionLimit > 1)
                        message = pleaseSelect + " " + minSelectionLimit + " " + options;
                    else
                        message = pleaseSelect + " " + minSelectionLimit + " " + option;
                }
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }

        if (view.getId() == R.id.cancel) {
            if(submitCallbackListener!=null){
                selectedIdsForCallback.clear();
                selectedIdsForCallback.addAll(tempPreviouslySelectedIdsList);
                submitCallbackListener.onCancel();
            }
            dismiss();
        }
    }

    private String getSelectedDataString() {
        StringBuilder data = new StringBuilder();
        for (int i = 0; i < tempMainListOfAdapter.size(); i++) {
            if (checkForSelection(tempMainListOfAdapter.get(i).getId())) {
                data.append(", ").append(tempMainListOfAdapter.get(i).getName());
            }
        }
        if (data.length() > 0) {
            return data.substring(1);
        } else {
            return "";
        }
    }

    private ArrayList<String> getSelectNameList() {
        ArrayList<String> names = new ArrayList<>();
        for(int i=0;i<tempMainListOfAdapter.size();i++){
            if(checkForSelection(tempMainListOfAdapter.get(i).getId())){
                names.add(tempMainListOfAdapter.get(i).getName());
            }
        }
        return names;
    }

    private boolean checkForSelection(Integer id) {
        for (int i = 0; i < MultiSelectDialog.selectedIdsForCallback.size(); i++) {
            if (id.equals(MultiSelectDialog.selectedIdsForCallback.get(i))) {
                return true;
            }
        }
        return false;
    }

   /* public void setCallbackListener(SubmitCallbackListener submitCallbackListener) {
        this.submitCallbackListener = submitCallbackListener;
    }*/

    public interface SubmitCallbackListener {
        void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String commonSeparatedData);
        void onCancel();
    }

}
