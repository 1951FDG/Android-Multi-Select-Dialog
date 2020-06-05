package com.abdeveloper.sampleapplication;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectDialog.SubmitCallbackListener;
import com.abdeveloper.library.MultiSelectModel;
import com.abdeveloper.library.MultiSelectable;
import com.abdeveloper.sampleapplication.databinding.MainActivityBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SubmitCallbackListener {

    private static final String TAG = "Cancel";

    private ArrayList<MultiSelectable> mCountries;

    private ArrayList<Integer> mSelectedCountries;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources res = getResources();
        mCountries = getMultiSelectModels(res.getTextArray(R.array.names), R.array.icons);
        mSelectedCountries = new ArrayList<>(4);
        mSelectedCountries.add(1);
        mSelectedCountries.add(3);
        mSelectedCountries.add(4);
        mSelectedCountries.add(7);

        MainActivityBinding binding = MainActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.button.setOnClickListener(this);
        binding.button.performClick();
    }

    @Override
    public void onClick(@NonNull View v) {
        MultiSelectDialog dialog = new MultiSelectDialog();
        dialog.setHint(getString(R.string.multi_select_dialog_hint));
        dialog.setTitle(getString(R.string.multi_select_dialog_title));
        dialog.setPositiveText(R.string.dialog_done_text);
        dialog.setNegativeText(R.string.dialog_cancel_text);
        dialog.setMinSelectionLimit(1);
        dialog.setMaxSelectionLimit(10);
        dialog.setPreSelectIDsList(mSelectedCountries);
        dialog.setMultiSelectList(mCountries);
        dialog.show(getSupportFragmentManager(), null);
    }

    @Override
    public void onCancel() {
        Log.d(TAG, "Dialog cancelled");
    }

    @Override
    public void onSelected(@NonNull ArrayList<Integer> selectedIds, @NonNull ArrayList<String> selectedNames, @NonNull String dataString) {
        mSelectedCountries = selectedIds;
        String text = String.format("Selected Ids : %s\nSelected Names : %s\nDataString : %s", selectedIds, selectedNames, dataString);
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        toast.show();
    }

    private ArrayList<MultiSelectable> getMultiSelectModels(CharSequence[] countries, @ArrayRes int id) {
        Resources res = getResources();
        TypedArray icons = res.obtainTypedArray(id);
        int length = icons.length();
        ArrayList<MultiSelectable> listOfCountries = new ArrayList<>(length);
        for (int index = 0; index < length; index++) {
            int resId = icons.getResourceId(index, 0);
            SpannableString spannableString = new SpannableString(countries[index]);
            listOfCountries.add(new MultiSelectModel(index, spannableString, resId));
        }
        icons.recycle();
        return listOfCountries;
    }
}
