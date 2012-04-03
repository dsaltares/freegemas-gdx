package com.siondream.freegemas;

import com.badlogic.gdx.backends.android.AndroidApplication;

import android.os.Bundle;

public class FreegemasActivity extends AndroidApplication {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize(new Freegemas(), false);
    }
}