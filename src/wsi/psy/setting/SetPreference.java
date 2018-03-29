package wsi.psy.setting;

import wsi.psy.R;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SetPreference extends PreferenceActivity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setpreference);
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String per = mPreferences.getString("per_time", "");
    }
}
