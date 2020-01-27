package com.abdeveloper.sampleapplication;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectDialog.SubmitCallbackListener;
import com.abdeveloper.library.MultiSelectModel;
import com.abdeveloper.library.MultiSelectable;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SubmitCallbackListener {

    private static final String TAG = "Cancel";

    private MultiSelectDialog multiSelectDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Button button = findViewById(R.id.show_dialog);
        button.setOnClickListener(this);
        //preselected Ids of Country List
        ArrayList<Integer> selectedCountries = new ArrayList<>(4);
        selectedCountries.add(1);
        selectedCountries.add(3);
        selectedCountries.add(4);
        selectedCountries.add(7);
        Resources resources = getResources();
        CharSequence[] countries = resources.getTextArray(R.array.names);
        //List of Countries with Name and Id
        ArrayList<MultiSelectable> listOfCountries = getMultiSelectModels(countries, R.array.icons);
        multiSelectDialog = new MultiSelectDialog()
                .setHint(resources.getString(R.string.multi_select_dialog_hint)) //setting hint for dialog
                .setTitle(resources.getString(R.string.multi_select_dialog_title)) //setting title for dialog
                .setPositiveText(resources.getString(R.string.dialog_done_text))
                .setNegativeText(resources.getString(R.string.dialog_cancel_text))
                .setMinSelectionLimit(0)
                .setMaxSelectionLimit(listOfCountries.size())
                .setPreSelectIDsList(selectedCountries) //List of ids that you need to be selected
                .setMultiSelectList(listOfCountries) // the multi select model list with ids and name
                .setSubmitListener(this);
        button.performClick();
    }

    @Override
    public void onCancel() {
        Log.d(TAG, "Dialog cancelled");
    }

    @Override
    public void onClick(@NonNull View v) {
        multiSelectDialog.show(getSupportFragmentManager(), "multiSelectDialog");
    }

    @Override
    public void onSelected(@NonNull ArrayList<Integer> selectedIds, @NonNull ArrayList<String> selectedNames, @NonNull String dataString) {
        Toast.makeText(this,
                "Selected Ids : " + selectedIds + '\n' + "Selected Names : " + selectedNames + '\n' + "DataString : " + dataString,
                Toast.LENGTH_LONG).show();
    }

    private ArrayList<MultiSelectable> getMultiSelectModels(CharSequence[] countries, @ArrayRes int id) {
        Resources res = getResources();
        TypedArray icons = res.obtainTypedArray(id);
        int length = icons.length();
        ArrayList<MultiSelectable> listOfCountries = new ArrayList<>(length);
        for (int index = 0; index < length; index++) {
            int resid = icons.getResourceId(index, 0);
            SpannableString string = new SpannableString(countries[index]);
            listOfCountries.add(new MultiSelectModel(index, string, resid));
        }
        icons.recycle();
        return listOfCountries;
    }
}
