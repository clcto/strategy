package com.clcto.strategy;

import android.app.Activity;
import android.os.Bundle;

public class Strategy extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView( new DisplayManager( this ) );
    }
}