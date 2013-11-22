package com.fbhack.memoapp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;


public class MenuActivity extends Activity {
	private boolean mResumed;
	
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // Do nothing.
        }
    };
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("Memo", "ONCREATE");
		bindService(new Intent(this, MemoService.class), mConnection, 0);
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
