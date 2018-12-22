package com.abdeveloper.sampleapplication;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectDialog.SubmitCallbackListener;
import com.abdeveloper.library.MultiSelectModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
        CharSequence[] countries = resources.getTextArray(R.array.pref_country_entries);
        //List of Countries with Name and Id
        ArrayList<MultiSelectModel> listOfCountries = getMultiSelectModels(countries);

        multiSelectDialog = new MultiSelectDialog()
                .hint(getResources().getString(R.string.multi_select_dialog_hint)) //setting hint for dialog
                .title(getResources().getString(R.string.multi_select_dialog_title)) //setting title for dialog
                .positiveText(getResources().getString(R.string.dialog_done_text))
                .negativeText(getResources().getString(R.string.dialog_cancel_text))
                .setMinSelectionLimit(0)
                .setMaxSelectionLimit(listOfCountries.size())
                .preSelectIDsList(selectedCountries) //List of ids that you need to be selected
                .multiSelectList(listOfCountries) // the multi select model list with ids and name
                .onSubmit(this);
    }

    @SuppressWarnings({"MagicNumber", "OverlyLongMethod"})
    private static ArrayList<MultiSelectModel> getMultiSelectModels(CharSequence[] countries) {
        ArrayList<MultiSelectModel> listOfCountries = new ArrayList<>(62);
        listOfCountries.add(new MultiSelectModel(0, new SpannableString(countries[0]), R.drawable.flag_al));
        listOfCountries.add(new MultiSelectModel(1, new SpannableString(countries[1]), R.drawable.flag_ar));
        listOfCountries.add(new MultiSelectModel(2, new SpannableString(countries[2]), R.drawable.flag_au));
        listOfCountries.add(new MultiSelectModel(3, new SpannableString(countries[3]), R.drawable.flag_at));
        listOfCountries.add(new MultiSelectModel(4, new SpannableString(countries[4]), R.drawable.flag_az));
        listOfCountries.add(new MultiSelectModel(5, new SpannableString(countries[5]), R.drawable.flag_be));
        listOfCountries.add(new MultiSelectModel(6, new SpannableString(countries[6]), R.drawable.flag_ba));
        listOfCountries.add(new MultiSelectModel(7, new SpannableString(countries[7]), R.drawable.flag_br));
        listOfCountries.add(new MultiSelectModel(8, new SpannableString(countries[8]), R.drawable.flag_bg));
        listOfCountries.add(new MultiSelectModel(9, new SpannableString(countries[9]), R.drawable.flag_ca));
        listOfCountries.add(new MultiSelectModel(10, new SpannableString(countries[10]), R.drawable.flag_cl));
        listOfCountries.add(new MultiSelectModel(11, new SpannableString(countries[11]), R.drawable.flag_cr));
        listOfCountries.add(new MultiSelectModel(12, new SpannableString(countries[12]), R.drawable.flag_hr));
        listOfCountries.add(new MultiSelectModel(13, new SpannableString(countries[13]), R.drawable.flag_cy));
        listOfCountries.add(new MultiSelectModel(14, new SpannableString(countries[14]), R.drawable.flag_cz));
        listOfCountries.add(new MultiSelectModel(15, new SpannableString(countries[15]), R.drawable.flag_dk));
        listOfCountries.add(new MultiSelectModel(16, new SpannableString(countries[16]), R.drawable.flag_eg));
        listOfCountries.add(new MultiSelectModel(17, new SpannableString(countries[17]), R.drawable.flag_ee));
        listOfCountries.add(new MultiSelectModel(18, new SpannableString(countries[18]), R.drawable.flag_fi));
        listOfCountries.add(new MultiSelectModel(19, new SpannableString(countries[19]), R.drawable.flag_fr));
        listOfCountries.add(new MultiSelectModel(20, new SpannableString(countries[20]), R.drawable.flag_ge));
        listOfCountries.add(new MultiSelectModel(21, new SpannableString(countries[21]), R.drawable.flag_de));
        listOfCountries.add(new MultiSelectModel(22, new SpannableString(countries[22]), R.drawable.flag_gr));
        listOfCountries.add(new MultiSelectModel(23, new SpannableString(countries[23]), R.drawable.flag_hk));
        listOfCountries.add(new MultiSelectModel(24, new SpannableString(countries[24]), R.drawable.flag_hu));
        listOfCountries.add(new MultiSelectModel(25, new SpannableString(countries[25]), R.drawable.flag_is));
        listOfCountries.add(new MultiSelectModel(26, new SpannableString(countries[26]), R.drawable.flag_in));
        listOfCountries.add(new MultiSelectModel(27, new SpannableString(countries[27]), R.drawable.flag_id));
        listOfCountries.add(new MultiSelectModel(28, new SpannableString(countries[28]), R.drawable.flag_ie));
        listOfCountries.add(new MultiSelectModel(29, new SpannableString(countries[29]), R.drawable.flag_il));
        listOfCountries.add(new MultiSelectModel(30, new SpannableString(countries[30]), R.drawable.flag_it));
        listOfCountries.add(new MultiSelectModel(31, new SpannableString(countries[31]), R.drawable.flag_jp));
        listOfCountries.add(new MultiSelectModel(32, new SpannableString(countries[32]), R.drawable.flag_lv));
        listOfCountries.add(new MultiSelectModel(33, new SpannableString(countries[33]), R.drawable.flag_lu));
        listOfCountries.add(new MultiSelectModel(34, new SpannableString(countries[34]), R.drawable.flag_mk));
        listOfCountries.add(new MultiSelectModel(35, new SpannableString(countries[35]), R.drawable.flag_my));
        listOfCountries.add(new MultiSelectModel(36, new SpannableString(countries[36]), R.drawable.flag_mx));
        listOfCountries.add(new MultiSelectModel(37, new SpannableString(countries[37]), R.drawable.flag_md));
        listOfCountries.add(new MultiSelectModel(38, new SpannableString(countries[38]), R.drawable.flag_nl));
        listOfCountries.add(new MultiSelectModel(39, new SpannableString(countries[39]), R.drawable.flag_nz));
        listOfCountries.add(new MultiSelectModel(40, new SpannableString(countries[40]), R.drawable.flag_no));
        listOfCountries.add(new MultiSelectModel(41, new SpannableString(countries[41]), R.drawable.flag_pl));
        listOfCountries.add(new MultiSelectModel(42, new SpannableString(countries[42]), R.drawable.flag_pt));
        listOfCountries.add(new MultiSelectModel(43, new SpannableString(countries[43]), R.drawable.flag_ro));
        listOfCountries.add(new MultiSelectModel(44, new SpannableString(countries[44]), R.drawable.flag_ru));
        listOfCountries.add(new MultiSelectModel(45, new SpannableString(countries[45]), R.drawable.flag_rs));
        listOfCountries.add(new MultiSelectModel(46, new SpannableString(countries[46]), R.drawable.flag_sg));
        listOfCountries.add(new MultiSelectModel(47, new SpannableString(countries[47]), R.drawable.flag_sk));
        listOfCountries.add(new MultiSelectModel(48, new SpannableString(countries[48]), R.drawable.flag_si));
        listOfCountries.add(new MultiSelectModel(49, new SpannableString(countries[49]), R.drawable.flag_za));
        listOfCountries.add(new MultiSelectModel(50, new SpannableString(countries[50]), R.drawable.flag_kr));
        listOfCountries.add(new MultiSelectModel(51, new SpannableString(countries[51]), R.drawable.flag_es));
        listOfCountries.add(new MultiSelectModel(52, new SpannableString(countries[52]), R.drawable.flag_se));
        listOfCountries.add(new MultiSelectModel(53, new SpannableString(countries[53]), R.drawable.flag_ch));
        listOfCountries.add(new MultiSelectModel(54, new SpannableString(countries[54]), R.drawable.flag_tw));
        listOfCountries.add(new MultiSelectModel(55, new SpannableString(countries[55]), R.drawable.flag_th));
        listOfCountries.add(new MultiSelectModel(56, new SpannableString(countries[56]), R.drawable.flag_tr));
        listOfCountries.add(new MultiSelectModel(57, new SpannableString(countries[57]), R.drawable.flag_ua));
        listOfCountries.add(new MultiSelectModel(58, new SpannableString(countries[58]), R.drawable.flag_ae));
        listOfCountries.add(new MultiSelectModel(59, new SpannableString(countries[59]), R.drawable.flag_gb));
        listOfCountries.add(new MultiSelectModel(60, new SpannableString(countries[60]), R.drawable.flag_us));
        listOfCountries.add(new MultiSelectModel(61, new SpannableString(countries[61]), R.drawable.flag_vn));
        return listOfCountries;
    }

    @Override
    public void onClick(@NonNull View v) {
        multiSelectDialog.show(getSupportFragmentManager(), "multiSelectDialog");
    }

    @Override
    public void onSelected(@NonNull ArrayList<Integer> selectedIds, @NonNull ArrayList<String> selectedNames, @NonNull String dataString) {
        //will return list of selected IDS
        int size = selectedIds.size();
        for (int i = 0; i < size; i++) {
            Toast.makeText(MainActivity.this, "Selected Ids : " + selectedIds.get(i) + '\n' +
                    "Selected Names : " + selectedNames.get(i) + '\n' +
                    "DataString : " + dataString, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCancel() {
        Log.d(TAG, "Dialog cancelled");
    }
}
