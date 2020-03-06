package uk.ac.lshtm.mantra.android.settings;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import uk.ac.lshtm.mantra.android.R;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }
}
