package com.fbhack.memoapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.content.*;


public class MenuActivity extends Activity {
	private boolean mResumed;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		bindService(new Intent(this, MemoService.class), null, 0);
		setContentView(R.layout.activity_menu);
	}
	
	@Override
    protected void onResume() {
        super.onResume();
        mResumed = true;
        openOptionsMenu();
    }
	
    @Override
    protected void onPause() {
        super.onPause();
        mResumed = false;
    }
	
}
