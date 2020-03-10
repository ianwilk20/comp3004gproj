package com.example.fooderie;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class OptionsActivity extends AppCompatActivity {

    public static final String IMPERIAL_UNITS_CHECK_BOX = "options_iu";
    public static final String ALCOHOL_FREE_SWITCH = "options_af";
    public static final String LOW_CARB_SWITCH = "options_lc";
    public static final String LOW_FAT_SWITCH = "options_lf";
    public static final String PEANUT_FREE_SWITCH = "options_pf";
    public static final String TREE_NUT_FREE_SWITCH = "options_tnf";
    public static final String VEGAN_SWITCH = "options_vn";
    public static final String VEGETARIAN_SWITCH = "options_veg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new OptionsFragment())
                .commit();
    }
}