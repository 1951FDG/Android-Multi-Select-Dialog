package com.abdeveloper.sampleapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "Cancel";

    Button show_dialog_btn;

    MultiSelectDialog multiSelectDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        show_dialog_btn = findViewById(R.id.show_dialog);
        show_dialog_btn.setOnClickListener(this);

        //preselected Ids of Country List
        final ArrayList<Integer> alreadySelectedCountries = new ArrayList<>();
        alreadySelectedCountries.add(1);
        alreadySelectedCountries.add(3);
        alreadySelectedCountries.add(4);
        alreadySelectedCountries.add(7);

        //List of Countries with Name and Id
        ArrayList<MultiSelectModel> listOfCountries= new ArrayList<>();
        listOfCountries.add(new MultiSelectModel(0, "Albania", R.drawable.flag_al));
        listOfCountries.add(new MultiSelectModel(1, "Argentina", R.drawable.flag_ar));
        listOfCountries.add(new MultiSelectModel(2, "Australia", R.drawable.flag_au));
        listOfCountries.add(new MultiSelectModel(3, "Austria", R.drawable.flag_at));
        listOfCountries.add(new MultiSelectModel(4, "Azerbaijan", R.drawable.flag_az));
        listOfCountries.add(new MultiSelectModel(5, "Belgium", R.drawable.flag_be));
        listOfCountries.add(new MultiSelectModel(6, "Bosnia and Herzegovina", R.drawable.flag_ba));
        listOfCountries.add(new MultiSelectModel(7, "Brazil", R.drawable.flag_br));
        listOfCountries.add(new MultiSelectModel(8, "Bulgaria", R.drawable.flag_bg));
        listOfCountries.add(new MultiSelectModel(9, "Canada", R.drawable.flag_ca));
        listOfCountries.add(new MultiSelectModel(10, "Chile", R.drawable.flag_cl));
        listOfCountries.add(new MultiSelectModel(11, "Costa Rica", R.drawable.flag_cr));
        listOfCountries.add(new MultiSelectModel(12, "Croatia", R.drawable.flag_hr));
        listOfCountries.add(new MultiSelectModel(13, "Cyprus", R.drawable.flag_cy));
        listOfCountries.add(new MultiSelectModel(14, "Czech Republic", R.drawable.flag_cz));
        listOfCountries.add(new MultiSelectModel(15, "Denmark", R.drawable.flag_dk));
        listOfCountries.add(new MultiSelectModel(16, "Egypt", R.drawable.flag_eg));
        listOfCountries.add(new MultiSelectModel(17, "Estonia", R.drawable.flag_ee));
        listOfCountries.add(new MultiSelectModel(18, "Finland", R.drawable.flag_fi));
        listOfCountries.add(new MultiSelectModel(19, "France", R.drawable.flag_fr));
        listOfCountries.add(new MultiSelectModel(20, "Georgia", R.drawable.flag_ge));
        listOfCountries.add(new MultiSelectModel(21, "Germany", R.drawable.flag_de));
        listOfCountries.add(new MultiSelectModel(22, "Greece", R.drawable.flag_gr));
        listOfCountries.add(new MultiSelectModel(23, "Hong Kong", R.drawable.flag_hk));
        listOfCountries.add(new MultiSelectModel(24, "Hungary", R.drawable.flag_hu));
        listOfCountries.add(new MultiSelectModel(25, "Iceland", R.drawable.flag_is));
        listOfCountries.add(new MultiSelectModel(26, "India", R.drawable.flag_in));
        listOfCountries.add(new MultiSelectModel(27, "Indonesia", R.drawable.flag_id));
        listOfCountries.add(new MultiSelectModel(28, "Ireland", R.drawable.flag_ie));
        listOfCountries.add(new MultiSelectModel(29, "Israel", R.drawable.flag_il));
        listOfCountries.add(new MultiSelectModel(30, "Italy", R.drawable.flag_it));
        listOfCountries.add(new MultiSelectModel(31, "Japan", R.drawable.flag_jp));
        listOfCountries.add(new MultiSelectModel(32, "Latvia", R.drawable.flag_lv));
        listOfCountries.add(new MultiSelectModel(33, "Luxembourg", R.drawable.flag_lu));
        listOfCountries.add(new MultiSelectModel(34, "Macedonia", R.drawable.flag_mk));
        listOfCountries.add(new MultiSelectModel(35, "Malaysia", R.drawable.flag_my));
        listOfCountries.add(new MultiSelectModel(36, "Mexico", R.drawable.flag_mx));
        listOfCountries.add(new MultiSelectModel(37, "Moldova", R.drawable.flag_md));
        listOfCountries.add(new MultiSelectModel(38, "Netherlands", R.drawable.flag_nl));
        listOfCountries.add(new MultiSelectModel(39, "New Zealand", R.drawable.flag_nz));
        listOfCountries.add(new MultiSelectModel(40, "Norway", R.drawable.flag_no));
        listOfCountries.add(new MultiSelectModel(41, "Poland", R.drawable.flag_pl));
        listOfCountries.add(new MultiSelectModel(42, "Portugal", R.drawable.flag_pt));
        listOfCountries.add(new MultiSelectModel(43, "Romania", R.drawable.flag_ro));
        listOfCountries.add(new MultiSelectModel(44, "Russia", R.drawable.flag_ru));
        listOfCountries.add(new MultiSelectModel(45, "Serbia", R.drawable.flag_rs));
        listOfCountries.add(new MultiSelectModel(46, "Singapore", R.drawable.flag_sg));
        listOfCountries.add(new MultiSelectModel(47, "Slovakia", R.drawable.flag_sk));
        listOfCountries.add(new MultiSelectModel(48, "Slovenia", R.drawable.flag_si));
        listOfCountries.add(new MultiSelectModel(49, "South Africa", R.drawable.flag_za));
        listOfCountries.add(new MultiSelectModel(50, "South Korea", R.drawable.flag_kr));
        listOfCountries.add(new MultiSelectModel(51, "Spain", R.drawable.flag_es));
        listOfCountries.add(new MultiSelectModel(52, "Sweden", R.drawable.flag_se));
        listOfCountries.add(new MultiSelectModel(53, "Switzerland", R.drawable.flag_ch));
        listOfCountries.add(new MultiSelectModel(54, "Taiwan", R.drawable.flag_tw));
        listOfCountries.add(new MultiSelectModel(55, "Thailand", R.drawable.flag_th));
        listOfCountries.add(new MultiSelectModel(56, "Turkey", R.drawable.flag_tr));
        listOfCountries.add(new MultiSelectModel(57, "Ukraine", R.drawable.flag_ua));
        listOfCountries.add(new MultiSelectModel(58, "United Arab Emirates", R.drawable.flag_ae));
        listOfCountries.add(new MultiSelectModel(59, "United Kingdom", R.drawable.flag_uk));
        listOfCountries.add(new MultiSelectModel(60, "United States", R.drawable.flag_us));
        listOfCountries.add(new MultiSelectModel(61, "Vietnam", R.drawable.flag_vn));



        //MultiSelectModel
        multiSelectDialog = new MultiSelectDialog()
                .hint(getResources().getString(R.string.multi_select_dialog_hint)) //setting hint for dialog
                .title(getResources().getString(R.string.multi_select_dialog_title)) //setting title for dialog
                .titleSize(20)
                .positiveText(getResources().getString(R.string.dialog_done_text))
                .negativeText(getResources().getString(R.string.dialog_cancel_text))
                .setMinSelectionLimit(0)
                .setMaxSelectionLimit(listOfCountries.size())
                .preSelectIDsList(alreadySelectedCountries) //List of ids that you need to be selected
                .multiSelectList(listOfCountries) // the multi select model list with ids and name
                .onSubmit(new MultiSelectDialog.SubmitCallbackListener() {
                    @Override
                    public void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String dataString) {
                        //will return list of selected IDS
                        for (int i = 0; i < selectedIds.size(); i++) {
                            Toast.makeText(MainActivity.this, "Selected Ids : " + selectedIds.get(i) + "\n" +
                                    "Selected Names : " + selectedNames.get(i) + "\n" +
                                    "DataString : " + dataString, Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG,"Dialog cancelled");

                    }
                });


    }

    @Override
    public void onClick(View view) {
        multiSelectDialog.show(getSupportFragmentManager(), "multiSelectDialog");
    }
}
