package com.example.charl.walkthisway;

/**
 * Created by charl on 16/03/2017.
 */


import android.preference.PreferenceActivity;

        import android.os.Bundle;
        import android.preference.PreferenceActivity;
        import android.preference.PreferenceFragment;

public class SettingsPreferences extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }

}
