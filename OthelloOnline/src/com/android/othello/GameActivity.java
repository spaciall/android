package com.android.othello;

import android.app.Activity;
import android.os.Bundle;

public class GameActivity extends Activity {
    /** Called when the activity is first created. */
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        MyView view = new MyView(this);
        view.m_Parent = this;
        setContentView(view);
    }
    
}